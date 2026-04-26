package cuchaz.ships.mixins.early.minecraft;

import net.minecraft.entity.EntityLivingBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import cuchaz.ships.core.ShipIntermediary;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase {

    /**
     * Replaces EntityLadderAdapter's HEAD injection into isOnLadder.
     * Returns true early if the entity is on a ship ladder.
     */
    @Inject(method = "isOnLadder", at = @At("HEAD"), cancellable = true)
    private void onIsOnLadder(CallbackInfoReturnable<Boolean> cir) {
        if (ShipIntermediary.isEntityOnShipLadder((EntityLivingBase) (Object) this)) {
            cir.setReturnValue(true);
        }
    }
}
