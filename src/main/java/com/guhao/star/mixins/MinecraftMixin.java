package com.guhao.star.mixins;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = Minecraft.class,remap = false)
public interface MinecraftMixin {
    /*
    @Accessor("timer")
    @Final
    Timer getTimer();
    @Final
    @Accessor("timer")
    @Mutable
    void setTimer(Timer var1);

     */
}
