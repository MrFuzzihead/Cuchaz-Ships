package cuchaz.ships.mixins.early.minecraft;

import net.minecraft.entity.player.EntityPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityPlayer.class)
public interface IMixinEntityPlayer {

    @Accessor("sleeping")
    void setSleeping(boolean sleeping);

    @Accessor("sleepTimer")
    void setSleepTimer(int sleepTimer);
}
