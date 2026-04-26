package cuchaz.ships.mixins.early.minecraft;

import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import cuchaz.ships.ShipWorld;
import cuchaz.ships.core.ShipIntermediary;

@Mixin(World.class)
public abstract class MixinWorld {

    /**
     * Replaces WorldAdapter's getEntitiesWithinAABBExcludingEntity injection.
     * Appends ships within the AABB to the result list before returning.
     * Skips ShipWorld instances to avoid infinite recursion.
     */
    @SuppressWarnings("unchecked")
    @Inject(
        method = "getEntitiesWithinAABBExcludingEntity(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;Lnet/minecraft/command/IEntitySelector;)Ljava/util/List;",
        at = @At("RETURN"))
    private void onGetEntitiesWithinAABBExcludingEntity(Entity excluded, AxisAlignedBB box, IEntitySelector selector,
        CallbackInfoReturnable<List> cir) {
        if ((Object) this instanceof ShipWorld) {
            return;
        }
        ShipIntermediary.getShipsWithinAABB(cir.getReturnValue(), (World) (Object) this, box, selector);
    }

    /**
     * Replaces WorldAdapter's checkBlockCollision injection.
     * If the vanilla result is false, also checks ship collision and returns true if found.
     * Skips ShipWorld instances to avoid recursive ship lookups.
     */
    @Inject(method = "checkBlockCollision", at = @At("RETURN"), cancellable = true)
    private void onCheckBlockCollision(AxisAlignedBB box, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof ShipWorld) {
            return;
        }
        if (!cir.getReturnValue()) {
            if (ShipIntermediary.checkBlockCollision((World) (Object) this, box)) {
                cir.setReturnValue(true);
            }
        }
    }
}
