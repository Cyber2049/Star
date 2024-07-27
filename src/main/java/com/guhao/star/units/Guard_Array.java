package com.guhao.star.units;

import com.guhao.GuHaoAnimations;
import net.minecraft.world.damagesource.DamageSource;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

import java.util.ArrayList;
import java.util.List;

public class Guard_Array {

    static final StaticAnimation[] GUARD;
    static final StaticAnimation[] PARRY;
    //static final StaticAnimation[] EXECUTE;
    static List<StaticAnimation> EXECUTE = new ArrayList<>();
    static {//无视格挡
         GUARD = new StaticAnimation[]{
                 /*
                 Animations.SPEAR_DASH,
                 Animations.BLADE_RUSH_FINISHER,
                 Animations.REVELATION_ONEHAND,
                 Animations.REVELATION_TWOHAND,
                 Animations.WRATHFUL_LIGHTING,
                 Animations.KATANA_SHEATH_DASH,
                 Animations.LETHAL_SLICING_ONCE1,
                  */
                 GuHaoAnimations.NB_ATTACK,
                 GuHaoAnimations.GUHAO_BATTOJUTSU_DASH,
                 GuHaoAnimations.BIU,
                 GuHaoAnimations.GUHAO_BIU
         };//只能完美
         PARRY = new StaticAnimation[]{
         };

         EXECUTE.add(Animations.BIPED_KNEEL);
         EXECUTE.add(Animations.WITHER_NEUTRALIZED);
         EXECUTE.add(Animations.VEX_NEUTRALIZED);
         EXECUTE.add(Animations.SPIDER_NEUTRALIZED);
         EXECUTE.add(Animations.DRAGON_NEUTRALIZED);
         EXECUTE.add(Animations.ENDERMAN_NEUTRALIZED);
         EXECUTE.add(Animations.BIPED_COMMON_NEUTRALIZED);
         EXECUTE.add(Animations.GREATSWORD_GUARD_BREAK);
    }

    public StaticAnimation[] getGuard() {
        return GUARD;
    }
    public StaticAnimation[] getParry() {
        return PARRY;
    }
    public List<StaticAnimation> getExecute() {
        return EXECUTE;
    }
    public EpicFightDamageSource getEpicFightDamageSources(DamageSource damageSource) {
        if (damageSource instanceof EpicFightDamageSource epicfightDamageSource) {
            return epicfightDamageSource;
        } else {
            return null;
        }
    }
}