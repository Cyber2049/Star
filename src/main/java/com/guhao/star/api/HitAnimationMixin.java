package com.guhao.star.api;

import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.damagesource.StunType;

public interface HitAnimationMixin {
    StaticAnimation getHitAnimation(StunType stunType);
}