package com.guhao.star.mixins;

import com.guhao.star.regirster.ParticleType;
import com.guhao.star.regirster.Sounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.property.MoveCoordFunctions;
import yesman.epicfight.api.animation.types.*;
import yesman.epicfight.api.animation.types.grappling.GrapplingAttackAnimation;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.utils.HitEntityList;
import yesman.epicfight.api.utils.TimePairList;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.ArrayList;

@Mixin(value = Animations.class, remap = false)
public class AnimationMixin {
    @Shadow
    public static StaticAnimation BIPED_COMMON_NEUTRALIZED;
    @Shadow
    public static StaticAnimation EVISCERATE_SECOND;
    @Shadow
    public static StaticAnimation BLADE_RUSH_COMBO1;
    @Shadow
    public static StaticAnimation BLADE_RUSH_COMBO2;
    @Shadow
    public static StaticAnimation BLADE_RUSH_COMBO3;
    @Shadow
    public static StaticAnimation BLADE_RUSH_EXECUTE_BIPED;
    @Shadow
    public static StaticAnimation THE_GUILLOTINE;
    @Shadow
    public static StaticAnimation AXE_AIRSLASH;
    @Shadow
    public static StaticAnimation BIPED_MOB_UCHIGATANA1;
    @Shadow
    public static StaticAnimation GREATSWORD_AUTO1;
    @Shadow
    public static StaticAnimation SWEEPING_EDGE;
    @Shadow
    public static StaticAnimation SPEAR_TWOHAND_AUTO1;
    @Shadow
    public static StaticAnimation LONGSWORD_LIECHTENAUER_AUTO3;
    @Shadow
    public static StaticAnimation DANCING_EDGE;
    @Shadow
    public static StaticAnimation DAGGER_DUAL_AUTO1;
    @Shadow
    public static StaticAnimation LONGSWORD_AUTO1;
    @Shadow
    public static StaticAnimation UCHIGATANA_AUTO1;


    @Inject(at = @At("TAIL"), method = "build")
    private static void rebuild(CallbackInfo ci) {
        HumanoidArmature biped = Armatures.BIPED;
        BIPED_COMMON_NEUTRALIZED = new LongHitAnimation(0.05F, "biped/skill/guard_break1", biped)
                .addEvents(AnimationEvent.TimeStampedEvent.create(0.0001F, RECOVERY, AnimationEvent.Side.SERVER));
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        EVISCERATE_SECOND = (new AttackAnimation(0.15F, 0.0F, 0.0F, 0.0F, 0.4F, (Collider)null, biped.toolR, "biped/skill/eviscerate_second", biped))
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.adder(1.3F))
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(1.7F))
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.EVISCERATE)
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.EVISCERATE)
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.4F);
        BLADE_RUSH_COMBO1 = (new AttackAnimation(0.1F, 0.0F, 0.2F, 0.25F, 0.85F, ColliderPreset.BIPED_BODY_COLLIDER, biped.rootJoint, "biped/skill/blade_rush_combo1", biped)).addProperty(AnimationProperty.AttackPhaseProperty.HIT_PRIORITY, HitEntityList.Priority.TARGET).addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(20.0F)).addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(1.15F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.adder(4.0F)).addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_ON_LINK, false).addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(new float[]{0.0F, 0.35F})).addProperty(AnimationProperty.ActionAnimationProperty.COORD_UPDATE_TIME, TimePairList.create(new float[]{0.0F, 0.25F})).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_DEST_LOCATION_BEGIN).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, MoveCoordFunctions.TRACE_DEST_LOCATION).addProperty(AnimationProperty.ActionAnimationProperty.COORD_GET, MoveCoordFunctions.WORLD_COORD).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE).newTimePair(0.0F, 0.65F).addStateRemoveOld(EntityState.CAN_SKILL_EXECUTION, false);
        BLADE_RUSH_COMBO2 = (new AttackAnimation(0.1F, 0.0F, 0.2F, 0.25F, 0.85F, ColliderPreset.BIPED_BODY_COLLIDER, biped.rootJoint, "biped/skill/blade_rush_combo2", biped)).addProperty(AnimationProperty.AttackPhaseProperty.HIT_PRIORITY, HitEntityList.Priority.TARGET).addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(20.0F)).addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(1.15F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.adder(4.0F)).addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_ON_LINK, false).addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(new float[]{0.0F, 0.35F})).addProperty(AnimationProperty.ActionAnimationProperty.COORD_UPDATE_TIME, TimePairList.create(new float[]{0.0F, 0.25F})).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_DEST_LOCATION_BEGIN).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, MoveCoordFunctions.TRACE_DEST_LOCATION).addProperty(AnimationProperty.ActionAnimationProperty.COORD_GET, MoveCoordFunctions.WORLD_COORD).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE).newTimePair(0.0F, 0.65F).addStateRemoveOld(EntityState.CAN_SKILL_EXECUTION, false);
        BLADE_RUSH_COMBO3 = (new AttackAnimation(0.1F, 0.0F, 0.25F, 0.35F, 0.85F, ColliderPreset.BIPED_BODY_COLLIDER, biped.rootJoint, "biped/skill/blade_rush_combo3", biped)).addProperty(AnimationProperty.AttackPhaseProperty.HIT_PRIORITY, HitEntityList.Priority.TARGET).addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(20.0F)).addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(1.15F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.adder(4.0F)).addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_ON_LINK, false).addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(new float[]{0.0F, 0.35F})).addProperty(AnimationProperty.ActionAnimationProperty.COORD_UPDATE_TIME, TimePairList.create(new float[]{0.0F, 0.25F})).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_DEST_LOCATION_BEGIN).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, MoveCoordFunctions.TRACE_DEST_LOCATION).addProperty(AnimationProperty.ActionAnimationProperty.COORD_GET, MoveCoordFunctions.WORLD_COORD).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE).newTimePair(0.0F, 0.6F).addStateRemoveOld(EntityState.CAN_SKILL_EXECUTION, false);
        BLADE_RUSH_EXECUTE_BIPED = (new GrapplingAttackAnimation(0.5F, 1.5F, "biped/skill/blade_rush_execute", biped)).addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(1.35F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.5F)).addProperty(AnimationProperty.ActionAnimationProperty.COORD_UPDATE_TIME, TimePairList.create(0.0F, 0.5F)).addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(new float[]{0.0F, 0.95F})).addEvents(new AnimationEvent.TimeStampedEvent[]{AnimationEvent.TimeStampedEvent.create(0.1F, (entitypatch, animation, params) -> {
            LivingEntity grapplingTarget = entitypatch.getGrapplingTarget();
            if (grapplingTarget != null) {
                entitypatch.playSound(EpicFightSounds.BLADE_HIT, 0.0F, 0.0F);
            }

        }, AnimationEvent.Side.CLIENT), AnimationEvent.TimeStampedEvent.create(0.3F, (entitypatch, animation, params) -> {
            LivingEntity grapplingTarget = entitypatch.getGrapplingTarget();
            if (grapplingTarget != null) {
                entitypatch.playSound(EpicFightSounds.BLADE_HIT, 0.0F, 0.0F);
            }

        }, AnimationEvent.Side.CLIENT)});
        THE_GUILLOTINE = (new AttackAnimation(0.15F, 0.2F, 0.7F, 0.75F, 1.1F, (Collider)null, biped.toolR, "biped/skill/the_guillotine", biped)).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE);
        AXE_AIRSLASH = new AirSlashAnimation(0.1F, 0.3F, 0.4F, 0.65F, (Collider)null, biped.toolR, "biped/combat/axe_airslash", biped);
        BIPED_MOB_UCHIGATANA1 = (new AttackAnimation(0.05F, 0.3F, 0.2F, 0.3F, 0.7F, (Collider)null, biped.toolR, "biped/combat/mob_uchigatana1", biped)).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        GREATSWORD_AUTO1 = (new BasicAttackAnimation(0.3F, 0.15F, 0.25F, 0.65F, (Collider)null, biped.toolR, "biped/combat/greatsword_auto1", biped)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.0F);
        SWEEPING_EDGE = (new AttackAnimation(0.1F, 0.0F, 0.15F, 0.3F, 0.64F, (Collider)null, biped.toolR, "biped/skill/sweeping_edge", biped)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F).addProperty(AnimationProperty.AttackAnimationProperty.EXTRA_COLLIDERS, 1).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        SPEAR_TWOHAND_AUTO1 = (new BasicAttackAnimation(0.1F, 0.2F, 0.3F, 0.36F, (Collider)null, biped.toolR, "biped/combat/spear_twohand_auto1", biped)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.2F);
        LONGSWORD_LIECHTENAUER_AUTO3 = (new BasicAttackAnimation(0.25F, 0.1F, 0.2F, 0.54F, (Collider)null, biped.toolR, "biped/combat/longsword_liechtenauer_auto3", biped)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.2F);
        DAGGER_DUAL_AUTO1 = (new BasicAttackAnimation(0.05F, 0.1F, 0.2F, 0.25F, (Collider)null, biped.toolR, "biped/combat/dagger_dual_auto1", biped)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.4F);
        LONGSWORD_AUTO1 = (new BasicAttackAnimation(0.1F, 0.25F, 0.35F, 0.5F, null, biped.toolR, "biped/combat/longsword_auto1", biped)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.2F);
        UCHIGATANA_AUTO1 = (new BasicAttackAnimation(0.08F, 0.15F, 0.25F, 0.3F, null, biped.toolR, "biped/combat/uchigatana_auto1", biped)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.0F);
        DANCING_EDGE = new AttackAnimation(0.1F, "biped/skill/dancing_edge", biped,
                new AttackAnimation.Phase(0.0F, 0.2F, 0.31F, 0.4F, 0.4F, biped.toolR, null),
                new AttackAnimation.Phase(0.4F, 0.5F, 0.61F, 0.65F, 0.65F, InteractionHand.OFF_HAND, biped.toolL, null),
                new AttackAnimation.Phase(0.65F, 0.75F, 0.85F, 1.15F, Float.MAX_VALUE, biped.toolR, null))
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F)
                .addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true);
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

