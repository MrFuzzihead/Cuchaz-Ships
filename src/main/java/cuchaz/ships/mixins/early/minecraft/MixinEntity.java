package cuchaz.ships.mixins.early.minecraft;

import net.minecraft.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import cuchaz.ships.core.ShipIntermediary;

@Mixin(Entity.class)
public abstract class MixinEntity {

    /**
     * Replaces EntityMoveAdapter's HEAD injection into moveEntity.
     * Calls ShipIntermediary.preEntityMove before the entity moves.
     */
    @Inject(method = "moveEntity", at = @At("HEAD"))
    private void onPreMoveEntity(double dx, double dy, double dz, CallbackInfo ci) {
        ShipIntermediary.preEntityMove((Entity) (Object) this, dx, dy, dz);
    }

    /**
     * Replaces EntityMoveAdapter's RETURN injection into moveEntity.
     * Calls ShipIntermediary.postEntityMove after the entity moves.
     */
    @Inject(method = "moveEntity", at = @At("RETURN"))
    private void onPostMoveEntity(double dx, double dy, double dz, CallbackInfo ci) {
        ShipIntermediary.postEntityMove((Entity) (Object) this, dx, dy, dz);
    }

    /**
     * Replaces EntityDistanceAdapter's HEAD injection into getDistanceSqToEntity.
     * If either entity is a ship, returns the ship-aware distance instead of the vanilla result.
     */
    @Inject(method = "getDistanceSqToEntity", at = @At("HEAD"), cancellable = true)
    private void onGetDistanceSqToEntity(Entity dest, CallbackInfoReturnable<Double> cir) {
        double result = ShipIntermediary.getDistanceSqToEntity((Entity) (Object) this, dest);
        if (result >= 0) {
            cir.setReturnValue(result);
        }
    }
}
