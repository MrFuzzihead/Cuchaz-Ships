package cuchaz.ships.mixins.early.minecraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnderChest;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import cuchaz.ships.core.ShipIntermediary;

@Mixin(TileEntityEnderChest.class)
public abstract class MixinTileEntityEnderChest {

    /**
     * Replaces TileEntityInventoryAdapter's getDistanceSq redirect in isUseableByPlayer (func_145971_a).
     * Routes through ShipIntermediary so ship-relative distance is used when needed.
     * Note: TileEntityEnderChest uses the SRG name func_145971_a as the MCP mapping is absent.
     */
    @Redirect(
        method = "func_145971_a",
        remap = false,
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;getDistanceSq(DDD)D"))
    private double redirectGetDistanceSq(EntityPlayer player, double x, double y, double z) {
        return ShipIntermediary.getEntityDistanceSq(player, x, y, z, (TileEntity) (Object) this);
    }
}
