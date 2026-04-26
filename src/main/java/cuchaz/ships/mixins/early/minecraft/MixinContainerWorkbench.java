package cuchaz.ships.mixins.early.minecraft;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import cuchaz.ships.EntityShip;
import cuchaz.ships.ShipLocator;
import cuchaz.ships.ShipWorld;
import cuchaz.ships.core.ShipIntermediary;

@Mixin(ContainerWorkbench.class)
public abstract class MixinContainerWorkbench {

    @Unique
    private World ships$world;

    /**
     * Captures the correct world for the distance check.
     * <p>
     * {@code EntityPlayerMP.displayGUIWorkbench} creates the container with
     * {@code player.worldObj} (the real world), not the {@link ShipWorld} that
     * {@code BlockWorkbench.onBlockActivated} received. So when a workbench is on a
     * launched ship, the real world has no crafting table at the ship-local block
     * coordinates. We detect this and find the ShipWorld that actually contains the
     * crafting table, storing it for the distance redirect below.
     */
    @Redirect(
        method = "canInteractWith",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlock(III)Lnet/minecraft/block/Block;"))
    private Block redirectGetBlock(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (block == Blocks.crafting_table) {
            this.ships$world = world;
            return block;
        }
        // The real world doesn't have the block here; check all ship worlds
        List<EntityShip> ships = ShipLocator.getShips(world);
        for (EntityShip ship : ships) {
            ShipWorld shipWorld = ship.getShipWorld();
            if (shipWorld.getBlock(x, y, z) == Blocks.crafting_table) {
                this.ships$world = shipWorld;
                return Blocks.crafting_table;
            }
        }
        return block;
    }

    /**
     * Routes through ShipIntermediary so ship-relative distance is used when the
     * workbench is on a ship.
     */
    @Redirect(
        method = "canInteractWith",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;getDistanceSq(DDD)D"))
    private double redirectGetDistanceSq(EntityPlayer player, double x, double y, double z) {
        return ShipIntermediary.getEntityDistanceSq(player, x, y, z, this.ships$world);
    }
}
