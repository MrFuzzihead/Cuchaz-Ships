package cuchaz.ships.mixins.early.minecraft;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.ServerConfigurationManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import cuchaz.ships.core.ShipIntermediary;

@Mixin(ServerConfigurationManager.class)
public abstract class MixinServerConfigurationManager {

    /**
     * Replaces ServerConfigurationManagerAdapter's injection into respawnPlayer.
     * Notifies ShipIntermediary after the new player is fully set up so berth spawn can be applied.
     */
    @Inject(method = "respawnPlayer", at = @At("RETURN"))
    private void onRespawnPlayer(EntityPlayerMP oldPlayer, int dimension, boolean b,
        CallbackInfoReturnable<EntityPlayerMP> cir) {
        ShipIntermediary.onPlayerRespawn(oldPlayer, cir.getReturnValue(), dimension);
    }
}
