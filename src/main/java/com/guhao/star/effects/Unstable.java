package com.guhao.star.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import org.jetbrains.annotations.NotNull;

public class Unstable extends MobEffect {
    public Unstable() {
        super(MobEffectCategory.HARMFUL, -13261);
    }

    @Override
    public @NotNull String getDescriptionId() {
        return "effect.star.unstable";
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}
