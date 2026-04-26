package cuchaz.ships.mixins.early.minecraft;

import net.minecraft.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface IMixinEntity {

    @Invoker
    void callUpdateFallState(double distanceFallenThisTick, boolean isOnGround);

    @Invoker
    void callSetSize(float width, float height);
}
