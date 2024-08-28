package com.guhao.star.mixins;

import net.minecraftforge.fml.common.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.damagesource.ExtraDamageInstance;
import yesman.epicfight.world.damagesource.SourceTags;
import yesman.epicfight.world.damagesource.StunType;

import java.util.Set;

@Mixin(value = EpicFightSkills.class,remap = false)
@Mod.EventBusSubscriber(modid= EpicFightMod.MODID)
public class EpicFightSkillsMixin {
    @Shadow
    public static Skill WRATHFUL_LIGHTING;
    @Shadow
    public static Skill TSUNAMI;
    @Shadow
    public static Skill EVERLASTING_ALLEGIANCE;
    @Shadow
    public static Skill BLADE_RUSH;
    @Shadow
    public static Skill IMPACT_GUARD;
    @Inject(method = "buildSkillEvent",at = @At("TAIL"))
    private static void buildSkillEvent(SkillBuildEvent onBuild, CallbackInfo ci) {
        WeaponInnateSkill wrathfulLighting = onBuild.build(EpicFightMod.MODID, "wrathful_lighting");
        wrathfulLighting.newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(1))
                .newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(12.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(3))
                .addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(100.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.KNOCKDOWN)
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(SourceTags.WEAPON_INNATE))
                .registerPropertiesToAnimation();
        WRATHFUL_LIGHTING = wrathfulLighting;
        WeaponInnateSkill tsunami = onBuild.build("epicfight", "tsunami");
        tsunami.newProperty().addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(100.0F)).addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create())).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(SourceTags.WEAPON_INNATE)).addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create(new float[0]))).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.KNOCKDOWN).registerPropertiesToAnimation();
        TSUNAMI = tsunami;
        WeaponInnateSkill everlastAllegiance = onBuild.build("epicfight", "everlasting_allegiance");
        everlastAllegiance.newProperty().addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(30.0F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.4F)).addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create())).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(SourceTags.WEAPON_INNATE)).addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create(new float[0]))).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD).registerPropertiesToAnimation();
        EVERLASTING_ALLEGIANCE = everlastAllegiance;
        WeaponInnateSkill bladeRush = onBuild.build(EpicFightMod.MODID, "blade_rush");
        bladeRush.newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(1))
                .addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(SourceTags.WEAPON_INNATE))
                .newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(SourceTags.EXECUTION, SourceTags.WEAPON_INNATE))
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_RUSH_FINISHER)
                .registerPropertiesToAnimation();
        BLADE_RUSH = bladeRush;
        IMPACT_GUARD = null;
    }
}
