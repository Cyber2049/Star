package com.guhao.star.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import org.jetbrains.annotations.NotNull;

public class Execute extends MobEffect {
    public Execute() {
        super(MobEffectCategory.BENEFICIAL, -13261);
    }
    @Override
    public @NotNull String getDescriptionId() {
        return "effect.star.execute";
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
