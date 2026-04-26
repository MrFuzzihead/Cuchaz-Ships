package cuchaz.ships.mixins.early.minecraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import cuchaz.ships.core.ShipIntermediary;

@Mixin(TileEntityChest.class)
public abstract class MixinTileEntityChest {

    /**
     * Replaces TileEntityInventoryAdapter's getDistanceSq redirect in isUseableByPlayer.
     * Routes through ShipIntermediary so ship-relative distance is used when needed.
     */
    @Redirect(
        method = "isUseableByPlayer",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;getDistanceSq(DDD)D"))
    private double redirectGetDistanceSq(EntityPlayer player, double x, double y, double z) {
        return ShipIntermediary.getEntityDistanceSq(player, x, y, z, (TileEntity) (Object) this);
    }
}
