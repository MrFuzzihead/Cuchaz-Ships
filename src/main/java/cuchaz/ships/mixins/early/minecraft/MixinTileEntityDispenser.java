package cuchaz.ships.mixins.early.minecraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import cuchaz.ships.core.ShipIntermediary;

@Mixin(TileEntityDispenser.class)
public abstract class MixinTileEntityDispenser {

    /**
     * Replaces TileEntityInventoryAdapter's getDistanceSq redirect in isUseableByPlayer.
     * Routes through ShipIntermediary so ship-relative distance is used when needed.
     * TileEntityDropper extends TileEntityDispenser and inherits this method.
     */
    @Redirect(
        method = "isUseableByPlayer",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;getDistanceSq(DDD)D"))
    private double redirectGetDistanceSq(EntityPlayer player, double x, double y, double z) {
        return ShipIntermediary.getEntityDistanceSq(player, x, y, z, (TileEntity) (Object) this);
    }
}
