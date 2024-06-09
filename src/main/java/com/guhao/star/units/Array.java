package com.guhao.star.units;

import com.guhao.GuHaoAnimations;
import net.minecraft.world.damagesource.DamageSource;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

public class Array {

    static final StaticAnimation[] GUARD;
    static final StaticAnimation[] PARRY;
    static {//无视格挡
         GUARD = new StaticAnimation[]{
                 Animations.SPEAR_DASH,
                 Animations.BLADE_RUSH_FINISHER,
                 Animations.REVELATION_ONEHAND,
                 Animations.REVELATION_TWOHAND,
                 Animations.WRATHFUL_LIGHTING,
                 Animations.KATANA_SHEATH_DASH,
                 Animations.LETHAL_SLICING_ONCE1,
                 GuHaoAnimations.NB_ATTACK,
                 GuHaoAnimations.GUHAO_BATTOJUTSU_DASH,
                 GuHaoAnimations.BIU
         };//无视完美
         PARRY = new StaticAnimation[]{
         };
    }


    public StaticAnimation[] getGuard() {
        return GUARD;
    }
    public StaticAnimation[] getParry() {
        return PARRY;
    }
    public EpicFightDamageSource getEpicFightDamageSources(DamageSource damageSource) {
        if (damageSource instanceof EpicFightDamageSource epicfightDamageSource) {
            return epicfightDamageSource;
        } else {
            return null;
        }
    }
}