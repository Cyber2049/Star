package com.guhao.star.effects;


import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class defense extends MobEffect {
    public defense() {
        super(MobEffectCategory.HARMFUL, -13261);
    }

    @Override
    public String getDescriptionId() {
        return "effect.sword.defense";
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}
