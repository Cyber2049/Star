package com.guhao.star.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;

@Mixin(value = Animations.class,remap = false,priority = 114514)
public class AnimationsMixin {
    @Shadow
    public static StaticAnimation BIPED_DEATH;
    @Shadow
    public static StaticAnimation CREEPER_DEATH;
}
