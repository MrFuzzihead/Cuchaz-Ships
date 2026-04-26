package cuchaz.ships.mixins.early.minecraft;

import net.minecraft.client.renderer.EntityRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cuchaz.ships.core.ShipIntermediaryClient;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {

    /**
     * Replaces EntityRendererAdapter's RETURN injection into getMouseOver.
     * Calls ShipIntermediaryClient.onFoundHit so ship block hit detection runs after the vanilla ray trace.
     */
    @Inject(method = "getMouseOver", at = @At("RETURN"))
    private void onGetMouseOver(float partialTick, CallbackInfo ci) {
        ShipIntermediaryClient.onFoundHit();
    }
}
