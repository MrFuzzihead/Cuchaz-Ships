/*******************************************************************************
 * Copyright (c) 2013 jeff.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Contributors:
 * jeff - initial API and implementation
 ******************************************************************************/
package cuchaz.ships.core;

import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import cuchaz.ships.Collider;
import cuchaz.ships.EntityShip;
import cuchaz.ships.PlayerRespawner;
import cuchaz.ships.ShipLocator;
import cuchaz.ships.ShipWorld;

public class ShipIntermediary {

    public static World translateWorld(World world, InventoryPlayer inventory) {
        // are we looking at a ship?
        EntityShip ship = ShipLocator.getFromPlayerLook(inventory.player);
        if (ship != null) {
            return ship.getShipWorld();
        }

        // otherwise, just pass through the original world
        return world;
    }

    public static double getEntityDistanceSq(EntityPlayer player, double tileEntityX, double tileEntityY,
        double tileEntityZ, TileEntity tileEntity) {
        return translateDistance(tileEntity.getWorldObj(), player, tileEntityX, tileEntityY, tileEntityZ);
    }

    public static double getEntityDistanceSq(EntityPlayer player, double x, double y, double z, World world) {
        return translateDistance(world, player, x, y, z);
    }

    public static void preEntityMove(Entity entity, double dx, double dy, double dz) {
        // just forward to the collider
        Collider.preEntityMove(entity, dx, dy, dz);
    }

    public static void postEntityMove(Entity entity, double dx, double dy, double dz) {
        // just forward to the collider
        Collider.postEntityMove(entity, dx, dy, dz);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static List getShipsWithinAABB(List out, World world, AxisAlignedBB box, IEntitySelector selector) {
        for (EntityShip ship : ShipLocator.findShipsInBox(world, box)) {
            if (selector == null || selector.isEntityApplicable(ship)) {
                out.add(ship);
            }
        }
        return out;
    }

    public static boolean checkBlockCollision(World world, AxisAlignedBB box) {
        for (EntityShip ship : ShipLocator.findShipsInBox(world, box)) {
            if (ship.getCollider()
                .isColliding(box)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEntityOnShipLadder(EntityLivingBase entity) {
        // just forward to the collider
        return Collider.isEntityOnShipLadder(entity);
    }

    public static double getDistanceSqToEntity(Entity src, Entity dest) {
        // is either entity a ship?
        if (src instanceof EntityShip) {
            return ((EntityShip) src).getCollider()
                .getDistanceSqToEntity(dest);
        } else if (dest instanceof EntityShip) {
            return ((EntityShip) dest).getCollider()
                .getDistanceSqToEntity(src);
        }

        // returning a negative number signals that the original distance function should be executed
        return -1;
    }

    public static void onPlayerWakeUp(EntityPlayer player, boolean wasSleepSuccessful) {
        PlayerRespawner.onPlayerWakeUp(player, wasSleepSuccessful);
    }

    public static void onPlayerRespawn(EntityPlayerMP oldPlayer, EntityPlayerMP newPlayer, int dimension) {
        PlayerRespawner.onPlayerRespawn(oldPlayer, newPlayer, dimension);
    }

    public static boolean isPlayerInBed(EntityPlayer player) {
        return PlayerRespawner.isPlayerInBerth(player);
    }

    private static double translateDistance(World world, EntityPlayer player, double x, double y, double z) {
        // is the block on a ship?
        if (world != null && world instanceof ShipWorld) {
            EntityShip ship = ((ShipWorld) world).getShip();

            // transform the coordinates to world space!
            Vec3 v = Vec3.createVectorHelper(x, y, z);
            ship.blocksToShip(v);
            ship.shipToWorld(v);

            return player.getDistanceSq(v.xCoord, v.yCoord, v.zCoord);
        } else {
            // no ship? just return the original result
            return player.getDistanceSq(x, y, z);
        }
    }
}
