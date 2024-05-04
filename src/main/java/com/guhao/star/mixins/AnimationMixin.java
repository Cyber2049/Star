package com.guhao.star.mixins;

import com.guhao.star.regirster.ParticleType;
import com.guhao.star.regirster.Sounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.types.LongHitAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.ArrayList;

@Mixin(value = Animations.class, remap = false)
public class AnimationMixin {
    @Shadow
    public static StaticAnimation BIPED_COMMON_NEUTRALIZED;
    @Shadow
    public static StaticAnimation SPEAR_DASH;
    @Shadow
    public static StaticAnimation BLADE_RUSH_FINISHER;
    @Shadow
    public static StaticAnimation REVELATION_ONEHAND;
    @Shadow
    public static StaticAnimation REVELATION_TWOHAND;
    @Shadow
    public static StaticAnimation WRATHFUL_LIGHTING;
    @Shadow
    public static StaticAnimation KATANA_SHEATH_DASH;
    @Shadow
    public static StaticAnimation LETHAL_SLICING_ONCE1;

    @Inject(at = @At("TAIL"), method = "build")
    private static void rebuild(CallbackInfo ci) {
        HumanoidArmature biped = Armatures.BIPED;
        BIPED_COMMON_NEUTRALIZED = new LongHitAnimation(0.05F, "biped/skill/guard_break1", biped)
                .addEvents(AnimationEvent.TimeStampedEvent.create(0.0001F, RECOVERY, AnimationEvent.Side.SERVER));
        /*
        SPEAR_DASH = new DashAttackAnimation(0.1F, 0.25F, 0.3F, 0.4F, 0.8F, null, biped.toolR, "biped/combat/spear_dash", biped)
                .addEvents(AnimationEvent.TimeStampedEvent.create(0.01F, DANGER, AnimationEvent.Side.BOTH));
        BLADE_RUSH_FINISHER = (new AttackAnimation(0.15F, 0.0F, 0.1F, 0.16F, 0.65F, ColliderPreset.BLADE_RUSH_FINISHER, biped.rootJoint, "biped/new/blade_rush_finisher", biped)).addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, true).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_RUSH_FINISHER).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.BLADE_RUSH_SKILL).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE)
                .addEvents(AnimationEvent.TimeStampedEvent.create(0.01F, DANGER, AnimationEvent.Side.BOTH));
        REVELATION_ONEHAND = (new AttackAnimation(0.05F, 0.0F, 0.05F, 0.1F, 0.35F, ColliderPreset.FIST, biped.legR, "biped/skill/revelation_normal", biped)).addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(SourceTags.COUNTER)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.NEUTRALIZE).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(1.0F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(0.5F)).addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.setter(0.0F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(2.0F)).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_LOCROT_TARGET).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, MoveCoordFunctions.TRACE_LOCROT_TARGET).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE)
                .addEvents(AnimationEvent.TimeStampedEvent.create(0.01F, DANGER, AnimationEvent.Side.BOTH));
        REVELATION_TWOHAND = (new AttackAnimation(0.1F, 0.0F, 0.05F, 0.1F, 0.35F, ColliderPreset.FIST_FIXED, biped.rootJoint, "biped/skill/revelation_twohand", biped)).addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT).addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(SourceTags.COUNTER)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.NEUTRALIZE).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(1.0F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(0.5F)).addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.setter(0.0F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(2.0F)).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_LOCROT_TARGET).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, MoveCoordFunctions.TRACE_LOCROT_TARGET).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE)
                .addEvents(AnimationEvent.TimeStampedEvent.create(0.01F, DANGER, AnimationEvent.Side.BOTH));
        WRATHFUL_LIGHTING = (new AttackAnimation(0.15F, "biped/skill/wrathful_lighting", biped, new AttackAnimation.Phase[]{new AttackAnimation.Phase(0.0F, 0.0F, 0.3F, 0.36F, 1.0F, Float.MAX_VALUE, biped.toolR, (Collider)null), new AttackAnimation.Phase(InteractionHand.MAIN_HAND, biped.rootJoint, null)})).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE)
                .addEvents(AnimationEvent.TimeStampedEvent.create(0.01F, DANGER, AnimationEvent.Side.BOTH))
                .addEvents(AnimationEvent.TimeStampedEvent.create(0.35F, Animations.ReusableSources.SUMMON_THUNDER, AnimationEvent.Side.SERVER));
        KATANA_SHEATH_DASH = (new DashAttackAnimation(0.06F, 0.05F, 0.05F, 0.11F, 0.65F, ColliderPreset.LETHAL_SLICING1, biped.rootJoint, "biped/new/katana_sheath_dash", biped)).addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(30.0F)).addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, true).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.0F)).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(3.0F)).addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)
                .addEvents(AnimationEvent.TimeStampedEvent.create(0.01F, DANGER, AnimationEvent.Side.BOTH));
        LETHAL_SLICING_ONCE1 = (new AttackAnimation(0.015F, 0.0F, 0.0F, 0.1F, 0.85F, ColliderPreset.LETHAL_SLICING1, biped.rootJoint, "biped/new/lethal_slicing_once1", biped)).addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.0F)).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE)
                .addEvents(AnimationEvent.TimeStampedEvent.create(0.01F, DANGER, AnimationEvent.Side.BOTH));

         */
    }
    
    private static final AnimationEvent.AnimationEventConsumer RECOVERY = (entitypatch, animation, params) -> {
        Entity entity = entitypatch.getOriginal();
        float recovery_val = (float) entity.getPersistentData().getDouble("recovery");
        LevelAccessor world = entity.getLevel();
        for (Entity player : new ArrayList<>(world.players())) {
            LazyOptional<EntityPatch> optional = player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY);
            optional.ifPresent(patch -> {
                if (patch instanceof PlayerPatch<?> PlayerPatch) {
                    PlayerPatch.setStamina(PlayerPatch.getStamina() + recovery_val);
                }
            });
        }
    };
    private static final AnimationEvent.AnimationEventConsumer DANGER = (entitypatch, animation, params) -> {
        entitypatch.playSound(Sounds.DANGER,0,0);
        Entity entity = entitypatch.getOriginal();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        if (entity.getLevel() instanceof ServerLevel world) {
            world.sendParticles(ParticleType.DANGER.get(), x, (y + 2.5), z, 1, x, (y + 2.5), z, 0);
        }
    };
}

