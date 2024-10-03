package com.guhao.star.units;

import com.guhao.GuHaoAnimations;
import com.guhao.star.efmex.StarAnimations;
import net.minecraft.world.damagesource.DamageSource;
import reascer.wom.gameasset.WOMAnimations;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Guard_Array {

    static final StaticAnimation[] GUARD;
    static final StaticAnimation[] PARRY;
    static final StaticAnimation[] DODGE;
    static final StaticAnimation[] CANDODGE;
    //static final StaticAnimation[] EXECUTE;
    static final List<StaticAnimation> EXECUTE = new ArrayList<>();
    static {//无视格挡
         GUARD = new StaticAnimation[]{
                 Animations.TSUNAMI_REINFORCED,
                 Animations.WRATHFUL_LIGHTING,
                 Animations.REVELATION_TWOHAND,
                 Animations.BATTOJUTSU_DASH,
                 StarAnimations.LETHAL_SLICING_ONCE1,
                 StarAnimations.KATANA_SHEATH_DASH,
                 WOMAnimations.TORMENT_AUTO_1,
                 WOMAnimations.RUINE_DASH,
                 WOMAnimations.SOLAR_QUEMADURA,
                 WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_LAYED_LEFT,
                 WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_LAYED_RIGHT,
                 WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_4,
                 WOMAnimations.GESETZ_SPRENGKOPF,
                 WOMAnimations.SOLAR_AUTO_2_POLVORA,
                 WOMAnimations.ENDERBLASTER_ONEHAND_SHOOT_DASH,
                 WOMAnimations.SOLAR_AUTO_2_POLVORA,

////////////////////////////////////////////////////////////////
                 GuHaoAnimations.NB_ATTACK,
                 GuHaoAnimations.GUHAO_BATTOJUTSU_DASH,
                 GuHaoAnimations.BIU,
                 GuHaoAnimations.GUHAO_BIU,
                 GuHaoAnimations.BLOOD_JUDGEMENT
         };//只能完美
         PARRY = new StaticAnimation[]{
                 Animations.UCHIGATANA_DASH,
                 Animations.TACHI_DASH,
                 Animations.SPEAR_DASH,
                 Animations.LONGSWORD_DASH,
                 Animations.REVELATION_ONEHAND,
                 StarAnimations.BLADE_RUSH_FINISHER,
                 WOMAnimations.HERRSCHER_AUTO_2,
                 WOMAnimations.STAFF_KINKONG,
                 WOMAnimations.SOLAR_HORNO,
                 WOMAnimations.ENDERBLASTER_ONEHAND_SHOOT_3,
                 WOMAnimations.GESETZ_AUTO_3,
                 WOMAnimations.RUINE_REDEMPTION,
                 WOMAnimations.RUINE_COMET,
                 WOMAnimations.AGONY_AUTO_1,
                 WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_4
         };
        DODGE = new StaticAnimation[]{
                StarAnimations.LETHAL_SLICING_ONCE1,
                StarAnimations.KATANA_SHEATH_DASH,
                WOMAnimations.TORMENT_AUTO_1,
                WOMAnimations.RUINE_DASH,
                WOMAnimations.SOLAR_QUEMADURA,
                WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_LAYED_LEFT,
                WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_LAYED_RIGHT,
                WOMAnimations.ENDERBLASTER_ONEHAND_SHOOT_DASH,
                WOMAnimations.SOLAR_AUTO_2_POLVORA
        };
        CANDODGE = new StaticAnimation[]{
                Animations.UCHIGATANA_DASH,
                Animations.TACHI_DASH,
                Animations.SPEAR_DASH,
                Animations.LONGSWORD_DASH,
                Animations.REVELATION_ONEHAND,
                StarAnimations.BLADE_RUSH_FINISHER,
                WOMAnimations.HERRSCHER_AUTO_2,
                WOMAnimations.STAFF_KINKONG,
                WOMAnimations.SOLAR_HORNO,
                WOMAnimations.ENDERBLASTER_ONEHAND_SHOOT_3,
                WOMAnimations.GESETZ_AUTO_3,
                WOMAnimations.RUINE_REDEMPTION,
                WOMAnimations.SOLAR_AUTO_2_POLVORA,



                Animations.TSUNAMI_REINFORCED,
                Animations.WRATHFUL_LIGHTING,
                Animations.REVELATION_TWOHAND,
                WOMAnimations.GESETZ_SPRENGKOPF,
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
    public static EpicFightDamageSource getEpicFightDamageSources(DamageSource damageSource) {
        if (damageSource instanceof EpicFightDamageSource epicfightDamageSource) {
            return epicfightDamageSource;
        } else {
            return null;
        }
    }
    public static boolean isNoGuard(StaticAnimation staticAnimation) {
        return Arrays.asList(GUARD).contains(staticAnimation);
    }
    public static boolean isNoParry(StaticAnimation staticAnimation) {
        return Arrays.asList(PARRY).contains(staticAnimation);
    }
    public static boolean isNoDodge(StaticAnimation staticAnimation) {
        return Arrays.asList(DODGE).contains(staticAnimation);
    }
    public static boolean canDodge(StaticAnimation staticAnimation) {
        return Arrays.asList(CANDODGE).contains(staticAnimation);
    }
}