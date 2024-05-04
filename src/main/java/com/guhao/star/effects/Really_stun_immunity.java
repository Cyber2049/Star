package com.guhao.star.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import org.jetbrains.annotations.NotNull;

public class Really_stun_immunity extends MobEffect {
    public Really_stun_immunity() {
        super(MobEffectCategory.BENEFICIAL, -13261);
    }

    @Override
    public @NotNull String getDescriptionId() {
        return "effect.star.really_stun_immunity";
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}
