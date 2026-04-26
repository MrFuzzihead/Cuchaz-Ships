package cuchaz.ships.mixins.early.minecraft;

import net.minecraft.entity.player.EntityPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import cuchaz.ships.core.ShipIntermediary;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayerInject {

    /**
     * Replaces EntityPlayerAdapter's HEAD injection into wakeUpPlayer.
     * Notifies ShipIntermediary when a player wakes up so berth state can be updated.
     * The third boolean parameter is wasSleepSuccessful.
     */
    @Inject(method = "wakeUpPlayer", at = @At("HEAD"))
    private void onWakeUpPlayer(boolean setSleepTimer, boolean updateWorld, boolean wasSleepSuccessful,
        CallbackInfo ci) {
        ShipIntermediary.onPlayerWakeUp((EntityPlayer) (Object) this, wasSleepSuccessful);
    }

    /**
     * Replaces EntityPlayerAdapter's HEAD injection into isInBed.
     * Returns true early if the player is sleeping in a ship berth.
     */
    @Inject(method = "isInBed", at = @At("HEAD"), cancellable = true)
    private void onIsInBed(CallbackInfoReturnable<Boolean> cir) {
        if (ShipIntermediary.isPlayerInBed((EntityPlayer) (Object) this)) {
            cir.setReturnValue(true);
        }
    }
}
