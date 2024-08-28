package com.guhao.star.efmex;

import com.dfdyz.epicacg.client.camera.CameraAnimation;
import com.dfdyz.epicacg.event.CameraEvents;
import com.guhao.star.api.animation.types.BasicAttackWinAnimation;
import com.guhao.star.api.animation.types.DodgeAttackAnimation;
import com.guhao.star.regirster.Effect;
import com.guhao.star.regirster.Sounds;
import com.guhao.star.units.BattleUnit;
import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import reascer.wom.world.damagesources.WOMExtraDamageInstance;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.property.AnimationProperty.AttackAnimationProperty;
import yesman.epicfight.api.animation.property.AnimationProperty.AttackPhaseProperty;
import yesman.epicfight.api.animation.types.*;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
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
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.SourceTags;
import yesman.epicfight.world.damagesource.StunType;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static com.guhao.star.Star.MODID;

@OnlyIn(Dist.CLIENT)
public class StarAnimations {
    public static StaticAnimation BIPED_PHANTOM_ASCENT_FORWARD_NEW;
    public static StaticAnimation BIPED_PHANTOM_ASCENT_BACKWARD_NEW;
    public static StaticAnimation EXECUTE;
    public static StaticAnimation EXECUTE_SEKIRO;
    public static StaticAnimation EXECUTED_SEKIRO;
    public static StaticAnimation FIRE_BALL;
    public static StaticAnimation AB_FIRE_BALL;
    //////////////////////////////////////////////////////////////
    public static StaticAnimation YAMATO_STEP_FORWARD;
    public static StaticAnimation YAMATO_STEP_BACKWARD;
    public static StaticAnimation YAMATO_STEP_LEFT;
    public static StaticAnimation YAMATO_STEP_RIGHT;
    public static StaticAnimation BLADE_RUSH_FINISHER;
    public static StaticAnimation LETHAL_SLICING_START;
    public static StaticAnimation MORTAL_BLADE;
    public static StaticAnimation LETHAL_SLICING_ONCE;
    public static StaticAnimation LETHAL_SLICING_TWICE;
    public static StaticAnimation LETHAL_SLICING_ONCE1;
    public static StaticAnimation LETHAL_SLICING_THIRD;
    public static StaticAnimation LORD_OF_THE_STORM;
    public static StaticAnimation FIST_AUTO_1;
    public static StaticAnimation ZWEI_DASH;
    public static StaticAnimation ZWEI_AIRSLASH;
    public static StaticAnimation ZWEI_AUTO1;
    public static StaticAnimation ZWEI_AUTO2;
    public static StaticAnimation ZWEI_AUTO3;
    public static StaticAnimation VANILLA_LETHAL_SLICING_START;
    public static StaticAnimation KATANA_SHEATH_DASH;
    public static StaticAnimation KATANA_SHEATH_AIRSLASH;
    public static StaticAnimation KATANA_SHEATH_AUTO;
    public static StaticAnimation OLD_GREATSWORD_AUTO1;
    public static StaticAnimation OLD_GREATSWORD_AUTO2;
    public static StaticAnimation OLD_GREATSWORD_DASH;
    public static StaticAnimation OLD_GREATSWORD_AIRSLASH;
    public static StaticAnimation LION_CLAW;
    public static StaticAnimation DUAL_SLASH;
    public static StaticAnimation WIND_SLASH;
    public static StaticAnimation SPEAR_SLASH;
    public static StaticAnimation SWORD_SLASH;

    public static StaticAnimation YAMATO_AUTO1;
    public static StaticAnimation YAMATO_AUTO2;
    public static StaticAnimation YAMATO_AUTO3;
    public static StaticAnimation YAMATO_AUTO4;
    public static StaticAnimation YAMATO_IDLE;
    public static StaticAnimation YAMATO_WALK;
    public static StaticAnimation YAMATO_RUN;
    public static StaticAnimation YAMATO_GUARD;
    public static StaticAnimation YAMATO_GUARD_HIT;
    public static StaticAnimation YAMATO_ACTIVE_GUARD_HIT;
    public static StaticAnimation YAMATO_ACTIVE_GUARD_HIT2;
    public static StaticAnimation YAMATO_DASH;
    public static StaticAnimation YAMATO_AIRSLASH;
    public static StaticAnimation YAMATO_POWER1;
    public static StaticAnimation YAMATO_POWER2;
    public static StaticAnimation YAMATO_POWER3;
    public static StaticAnimation YAMATO_POWER3_REPEAT;
    public static StaticAnimation YAMATO_POWER3_FINISH;
    public static StaticAnimation YAMATO_COUNTER1;
    public static StaticAnimation YAMATO_COUNTER2;
    public static StaticAnimation YAMATO_POWER_DASH;
    public static StaticAnimation YAMATO_STRIKE1;
    public static StaticAnimation YAMATO_STRIKE2;
    public static StaticAnimation YAMATO_POWER0_1;
    public static StaticAnimation YAMATO_POWER0_2;

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(StarAnimations::registerAnimations);
        MinecraftForge.EVENT_BUS.register(new StarAnimations());
    }

    public static void registerAnimations(AnimationRegistryEvent event) {
        event.getRegistryMap().put(MODID, StarAnimations::build);
    }
    public static CameraAnimation EXEA;

    public static void LoadCamAnims() {
        EXEA = CameraAnimation.load(new ResourceLocation(MODID, "camera_animation/execute.json"));
    }
    public static void build() {
        HumanoidArmature biped = Armatures.BIPED;
        EXECUTE = (new AttackAnimation(0.01F, "biped/execute", biped,
//                new AttackAnimation.Phase(0.0F, 0.48F, 0.75F, Float.MAX_VALUE, Float.MAX_VALUE, biped.toolR, StarColliderPreset.EXECUTE),
                new AttackAnimation.Phase(0.0F, 1.2F, 1.25F, Float.MAX_VALUE, Float.MAX_VALUE, biped.toolR, StarColliderPreset.EXECUTE))
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.setter(100.0F))
                .addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(WOMExtraDamageInstance.WOM_TARGET_MAX_HEALTH.create(0.05F))))
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(4.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.NONE)
                .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
                .addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, false)
                .addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.RESET_PLAYER_COMBO_COUNTER, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.0F, 2.10F))
                .addEvents(AnimationProperty.StaticAnimationProperty.TIME_STAMPED_EVENTS, new AnimationEvent.TimeStampedEvent[]{
                        AnimationEvent.TimeStampedEvent.create(0.002F, (ep, anim, objs) -> BattleUnit.Star_Battle_utils.duang(ep), AnimationEvent.Side.SERVER),})
                .addEvents(AnimationProperty.StaticAnimationProperty.TIME_PERIOD_EVENTS, new AnimationEvent.TimePeriodEvent[]{
                        AnimationEvent.TimePeriodEvent.create(1.2F, 1.25F, (ep, anim, objs) -> BattleUnit.Star_Battle_utils.ex(ep), AnimationEvent.Side.SERVER)
                })
                .addEvents(AnimationProperty.StaticAnimationProperty.ON_END_EVENTS, AnimationEvent.create((entitypatch, animation, params) -> {
                    entitypatch.getOriginal().removeEffect(Effect.EXECUTED.get());
                    entitypatch.getOriginal().removeEffect(Effect.EXECUTE.get());
                }, AnimationEvent.Side.CLIENT))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, EXE)
                .addState(EntityState.MOVEMENT_LOCKED, true)
                .addState(EntityState.TURNING_LOCKED, true)
                .addState(EntityState.LOCKON_ROTATE, true)
                .addState(EntityState.UPDATE_LIVING_MOTION, false);
        EXECUTE_SEKIRO = (new AttackAnimation(0.01F, "biped/sekiro", biped,
                new AttackAnimation.Phase(0.0F, 2.45F, 2.55F, 3.15F, Float.MAX_VALUE, biped.toolR, StarColliderPreset.EXECUTE))
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.setter(100.0F))
                .addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(WOMExtraDamageInstance.WOM_TARGET_MAX_HEALTH.create(0.05F))))
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(4.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.NONE)
                .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
                .addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, false)
                .addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.RESET_PLAYER_COMBO_COUNTER, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.0F, 4.0F))
                .addEvents(AnimationProperty.StaticAnimationProperty.TIME_STAMPED_EVENTS, new AnimationEvent.TimeStampedEvent[]{
                        AnimationEvent.TimeStampedEvent.create(0f, (ep, anim, objs) -> CameraEvents.SetAnim(EXEA, ep.getOriginal(), true), AnimationEvent.Side.BOTH),
                        AnimationEvent.TimeStampedEvent.create(1.2f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.NEUTRALIZE_BOSSES),
                        AnimationEvent.TimeStampedEvent.create(1.955f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(Sounds.DUANG1),
                        AnimationEvent.TimeStampedEvent.create(2.45F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(Sounds.DUANG2),})
                .addEvents(AnimationProperty.StaticAnimationProperty.ON_END_EVENTS, AnimationEvent.create((entitypatch, animation, params) -> {
                    entitypatch.getOriginal().removeEffect(Effect.EXECUTED.get());
                    entitypatch.getOriginal().removeEffect(Effect.EXECUTE.get());
                }, AnimationEvent.Side.CLIENT))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, EXE)
                .addState(EntityState.MOVEMENT_LOCKED, true)
                .addState(EntityState.TURNING_LOCKED, true)
                .addState(EntityState.LOCKON_ROTATE, true)
                .addState(EntityState.UPDATE_LIVING_MOTION, false);
        EXECUTED_SEKIRO = (new LongHitAnimation(0.01F, "biped/sekiro_executed", biped)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, false)
                .addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, false)
                .addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.0F, 4.0F)))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, EXE)
                .addEvents(AnimationProperty.StaticAnimationProperty.ON_END_EVENTS, AnimationEvent.create((entitypatch, animation, params) -> {
                    entitypatch.getOriginal().removeEffect(Effect.EXECUTED.get());
                    entitypatch.getOriginal().removeEffect(Effect.EXECUTE.get());
                }, AnimationEvent.Side.BOTH))
                .addState(EntityState.MOVEMENT_LOCKED, true)
                .addState(EntityState.TURNING_LOCKED, true)
                .addState(EntityState.LOCKON_ROTATE, true)
                .addState(EntityState.UPDATE_LIVING_MOTION, false);

        BIPED_PHANTOM_ASCENT_FORWARD_NEW = new ActionAnimation(0.05F, 0.7F, "biped/phantom_ascent_forward_new", biped)
                .addStateRemoveOld(EntityState.MOVEMENT_LOCKED, false)
                .newTimePair(0.0F, 0.5F)
                .addStateRemoveOld(EntityState.INACTION, true)
                .addEvents(AnimationProperty.StaticAnimationProperty.TIME_STAMPED_EVENTS, new AnimationEvent.TimeStampedEvent[]{
                        AnimationEvent.TimeStampedEvent.create(0.002F, (ep, anim, objs) -> {
                            Vec3 pos = ep.getOriginal().position();
                            double x = pos.x;
                            double y = pos.y;
                            double z = pos.z;
                            Level world = ep.getOriginal().level;
                            {
                                final Vec3 _center = new Vec3(x, y, z);
                                List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.8 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                                for (Entity entityiterator : _entfound) {
                                    if (entityiterator instanceof Monster monster && ep.getOriginal() instanceof Player player) {
                                        monster.hurt(DamageSource.playerAttack(world.getNearestPlayer(player, -1)), 0.1f);
                                        AdvancedCustomHumanoidMobPatch<?> ep3 = EpicFightCapabilities.getEntityPatch(entityiterator, AdvancedCustomHumanoidMobPatch.class);
                                        LivingEntityPatch<?> ep2 = EpicFightCapabilities.getEntityPatch(entityiterator, LivingEntityPatch.class);
                                        if ((!(ep2 == null)) && ep2.getOriginal().hasEffect(Effect.UNSTABLE.get())) {
                                            ep2.applyStun(StunType.LONG, 2.5f);
                                            if (!(ep3 == null)) {
                                                ep3.setStamina(ep3.getStamina() - (ep3.getMaxStamina() * 0.05F + 5.0F));
                                                ep3.playSound(Sounds.PENG, 0.8f, 1.2f);
                                            }
                                        }
                                    }
                                }
                            }
                        }, AnimationEvent.Side.SERVER),})

                .addEvents(AnimationProperty.StaticAnimationProperty.ON_BEGIN_EVENTS, AnimationEvent.create((entitypatch, animation, params) -> {
                    Vec3 pos = entitypatch.getOriginal().position();
                    entitypatch.playSound(EpicFightSounds.ROLL, 0, 0);
                    entitypatch.getOriginal().level.addAlwaysVisibleParticle(EpicFightParticles.AIR_BURST.get(), pos.x, pos.y + entitypatch.getOriginal().getBbHeight() * 0.5D, pos.z, 0, -1, 2);
                }, AnimationEvent.Side.CLIENT))
                .addEvents(AnimationProperty.StaticAnimationProperty.ON_END_EVENTS, AnimationEvent.create((entitypatch, animation, params) -> {
                    if (entitypatch instanceof PlayerPatch playerpatch) {
                        playerpatch.changeModelYRot(0);
                    }
                }, AnimationEvent.Side.CLIENT));
        BIPED_PHANTOM_ASCENT_BACKWARD_NEW = new ActionAnimation(0.05F, 0.7F, "biped/phantom_ascent_backward_new", biped)
                .addStateRemoveOld(EntityState.MOVEMENT_LOCKED, false)
                .newTimePair(0.0F, 0.5F)
                .addStateRemoveOld(EntityState.INACTION, true)
                .addEvents(AnimationProperty.StaticAnimationProperty.TIME_STAMPED_EVENTS, new AnimationEvent.TimeStampedEvent[]{
                        AnimationEvent.TimeStampedEvent.create(0.002F, (ep, anim, objs) -> {
                            Vec3 pos = ep.getOriginal().position();
                            double x = pos.x;
                            double y = pos.y;
                            double z = pos.z;
                            Level world = ep.getOriginal().level;
                            {
                                final Vec3 _center = new Vec3(x, y, z);
                                List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.8 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                                for (Entity entityiterator : _entfound) {
                                    if (entityiterator instanceof Monster monster && ep.getOriginal() instanceof Player player) {
                                        monster.hurt(DamageSource.playerAttack(world.getNearestPlayer(player, -1)), 0.1f);
                                        AdvancedCustomHumanoidMobPatch<?> ep3 = EpicFightCapabilities.getEntityPatch(entityiterator, AdvancedCustomHumanoidMobPatch.class);
                                        LivingEntityPatch<?> ep2 = EpicFightCapabilities.getEntityPatch(entityiterator, LivingEntityPatch.class);
                                        if ((!(ep2 == null)) && ep2.getOriginal().hasEffect(Effect.UNSTABLE.get())) {
                                            ep2.applyStun(StunType.LONG, 2.5f);
                                            if (!(ep3 == null)) {
                                                ep3.setStamina(ep3.getStamina() - (ep3.getMaxStamina() * 0.05F + 5.0F));
                                                ep3.playSound(Sounds.PENG, 0.8f, 1.2f);
                                            }
                                        }
                                    }
                                }
                            }
                        }, AnimationEvent.Side.SERVER),})
                .addEvents(AnimationProperty.StaticAnimationProperty.ON_BEGIN_EVENTS, AnimationEvent.create((entitypatch, animation, params) -> {
                    Vec3 pos = entitypatch.getOriginal().position();
                    entitypatch.playSound(EpicFightSounds.ROLL, 0, 0);
                    entitypatch.getOriginal().level.addAlwaysVisibleParticle(EpicFightParticles.AIR_BURST.get(), pos.x, pos.y + entitypatch.getOriginal().getBbHeight() * 0.5D, pos.z, 0, -1, 2);
                }, AnimationEvent.Side.CLIENT))
                .addEvents(AnimationProperty.StaticAnimationProperty.ON_END_EVENTS, AnimationEvent.create((entitypatch, animation, params) -> {
                    if (entitypatch instanceof PlayerPatch playerpatch) {
                        playerpatch.changeModelYRot(0);
                    }
                }, AnimationEvent.Side.CLIENT));
        FIRE_BALL = (new AttackAnimation(0.01F, "biped/new/fire_ball", biped,
                new AttackAnimation.Phase(0.0F, 0.75F, 1.0F, 1.25F, Integer.MAX_VALUE, biped.toolR, null))
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
                .addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, false)
                .addEvents(AnimationProperty.StaticAnimationProperty.TIME_STAMPED_EVENTS, new AnimationEvent.TimeStampedEvent[]{
                        AnimationEvent.TimeStampedEvent.create(0.6F, (entitypatch, self, params) -> {
                            if ((LevelAccessor) entitypatch.getOriginal().level instanceof ServerLevel world)
                                world.sendParticles(ParticleTypes.FLAME, entitypatch.getOriginal().getX(), entitypatch.getOriginal().getY() + 2, entitypatch.getOriginal().getZ(), 10, 0.1, 0.1, 0.1, 1);
                        }, AnimationEvent.Side.BOTH),
                        AnimationEvent.TimeStampedEvent.create(1.0F, (livingEntityPatch, staticAnimation, objects) -> BattleUnit.fireball(livingEntityPatch), AnimationEvent.TimeStampedEvent.Side.SERVER)}));
        AB_FIRE_BALL = (new AttackAnimation(0.01F, "biped/new/ab_fire_ball", biped,
                new AttackAnimation.Phase(0.0F, 0.75F, 1.0F, 1.25F, Integer.MAX_VALUE, biped.toolR, null))
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
                .addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, false)
                .addEvents(AnimationProperty.StaticAnimationProperty.TIME_STAMPED_EVENTS, new AnimationEvent.TimeStampedEvent[]{
                        AnimationEvent.TimeStampedEvent.create(0.6F, (entitypatch, self, params) -> {
                            if ((LevelAccessor) entitypatch.getOriginal().level instanceof ServerLevel world)
                                world.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, entitypatch.getOriginal().getX(), entitypatch.getOriginal().getY() + 2, entitypatch.getOriginal().getZ(), 10, 0.1, 0.1, 0.1, 1);
                        }, AnimationEvent.Side.BOTH),
                        AnimationEvent.TimeStampedEvent.create(1.0F, (livingEntityPatch, staticAnimation, objects) -> BattleUnit.ab_fireball(livingEntityPatch), AnimationEvent.TimeStampedEvent.Side.SERVER)}));
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        YAMATO_STEP_FORWARD = new DodgeAnimation(0.05F, 0.5F, "biped/yamato_step_forward", 0.6F, 1.65F, biped)
                .addEvents(
                        AnimationEvent.TimeStampedEvent.create(0.1F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(Sounds.YAMATO_STEP, 1.5F, 1.5F),
                        AnimationEvent.TimeStampedEvent.create(0.15F, (entitypatch, animation, params) -> {
                            Entity entity = entitypatch.getOriginal();
                            entity.level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
                        }, AnimationEvent.Side.CLIENT)
                );
        YAMATO_STEP_BACKWARD = new DodgeAnimation(0.05F, 0.5F, "biped/yamato_step_backward", 0.6F, 1.65F, biped)
                .addEvents(
                        AnimationEvent.TimeStampedEvent.create(0.1F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(Sounds.YAMATO_STEP, 1.5F, 1.5F),
                        AnimationEvent.TimeStampedEvent.create(0.15F, (entitypatch, animation, params) -> {
                            Entity entity = entitypatch.getOriginal();
                            entity.level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
                        }, AnimationEvent.Side.CLIENT)
                );
        YAMATO_STEP_LEFT = new DodgeAnimation(0.02F, 0.45F, "biped/yamato_step_left", 0.6F, 1.65F, biped)
                .addEvents(
                        AnimationEvent.TimeStampedEvent.create(0.1F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(Sounds.YAMATO_STEP, 1.5F, 1.5F),
                        AnimationEvent.TimeStampedEvent.create(0.15F, (entitypatch, animation, params) -> {
                            Entity entity = entitypatch.getOriginal();
                            entity.level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
                        }, AnimationEvent.Side.CLIENT)
                );
        YAMATO_STEP_RIGHT = new DodgeAnimation(0.02F, 0.45F, "biped/yamato_step_right", 0.6F, 1.65F, biped)
                .addEvents(
                        AnimationEvent.TimeStampedEvent.create(0.1F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(Sounds.YAMATO_STEP, 1.5F, 1.5F),
                        AnimationEvent.TimeStampedEvent.create(0.15F, (entitypatch, animation, params) -> {
                            Entity entity = entitypatch.getOriginal();
                            entity.level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
                        }, AnimationEvent.Side.CLIENT)
                );
        BLADE_RUSH_FINISHER = new AttackAnimation(0.15F, 0.0F, 0.1F, 0.16F, 0.65F, StarNewColliderPreset.BLADE_RUSH_FINISHER, biped.rootJoint, "biped/new/blade_rush_finisher", biped)
                .addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true).addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_RUSH_FINISHER)
                .addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.BLADE_RUSH_SKILL).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE);

        MORTAL_BLADE = new BasicAttackAnimation(0.15F, 0.2F, 0.3F, 0.5F, StarNewColliderPreset.MORTAL_BLADE, biped.toolR, "biped/new/tachi_auto2", biped)
                .addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 0.9F);

        LETHAL_SLICING_START = new AttackAnimation(0.15F, 0.0F, 0.0F, 0.11F, 0.35F, ColliderPreset.FIST_FIXED, biped.rootJoint, "biped/new/lethal_slicing_start", biped)
                .addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT).addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
                .addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT).addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(1.0F)).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE);
        LETHAL_SLICING_ONCE = new AttackAnimation(0.015F, 0.0F, 0.0F, 0.1F, 0.6F, StarNewColliderPreset.LETHAL_SLICING, biped.rootJoint, "biped/new/lethal_slicing_once", biped)
                .addProperty(yesman.epicfight.api.animation.property.AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP).addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.5F))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE);
        LETHAL_SLICING_TWICE = new AttackAnimation(0.015F, 0.0F, 0.0F, 0.1F, 0.6F, StarNewColliderPreset.LETHAL_SLICING, biped.rootJoint, "biped/new/lethal_slicing_twice", biped)
                .addProperty(AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.setter(50.0F)).addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.5F))
                .addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE);
        LETHAL_SLICING_ONCE1 = new AttackAnimation(0.015F, 0.0F, 0.0F, 0.1F, 0.85F, StarNewColliderPreset.LETHAL_SLICING1, biped.rootJoint, "biped/new/lethal_slicing_once1", biped)
                .addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP).addProperty(AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.setter(50.0F))
                .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.5F)).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE);
        LETHAL_SLICING_THIRD = new AttackAnimation(0.35F, "biped/new/lethal_slicing_third", biped,
                new AttackAnimation.Phase(0.0F, 0.15F, 0.20F, 1.15F, 0.30F, biped.toolR, null), new AttackAnimation.Phase(0.30F, 0.35F, 0.4F, 1.15F, 0.41F, biped.toolR, null),
                new AttackAnimation.Phase(0.4F, 0.5F, 0.61F, 1.15F, 0.65F, biped.toolR, null), new AttackAnimation.Phase(0.65F, 0.75F, 0.85F, 1.15F, Float.MAX_VALUE, biped.toolR, null))
                .addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)
                .addProperty(yesman.epicfight.api.animation.property.AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.45F)
                .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.SHORT);


        LORD_OF_THE_STORM = new AttackAnimation(0.15F, 0.45F, 0.85F, 0.95F, 2.2F, StarNewColliderPreset.LORD_OF_THE_STORM, biped.toolR, "biped/new/mob_greatsword1", biped)
                .addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.0F).addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);

        FIST_AUTO_1 = new BasicAttackAnimation(0.08F, 0.0F, 0.11F, 0.16F, ColliderPreset.FIST, biped.toolL, "biped/new/fist_auto1", biped)
                .addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F).addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
                .addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT);

        ZWEI_DASH = new DashAttackAnimation(0.15F, 0.1F, 0.2F, 0.45F, 0.7F, null, biped.toolR, "biped/new/tachi_dash", biped, false)
                .addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.2F);
        ZWEI_AIRSLASH = new AirSlashAnimation(0.1F, 0.3F, 0.41F, 0.5F, null, biped.toolR, "biped/new/longsword_airslash", biped);
        ZWEI_AUTO1 = new BasicAttackAnimation(0.25F, 0.15F, 0.25F, 0.65F, null, biped.toolR, "biped/new/greatsword_auto1", biped)
                .addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.2F);
        ZWEI_AUTO2 = new BasicAttackAnimation(0.1F, 0.2F, 0.3F, 0.5F, null, biped.toolR, "biped/new/longsword_liechtenauer_auto2", biped)
                .addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.2F);
        ZWEI_AUTO3 = new DashAttackAnimation(0.11F, 0.4F, 0.65F, 0.8F, 1.2F, null, biped.toolR, "biped/new/vanilla_greatsword_dash", biped, false)
                .addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.2F);

        VANILLA_LETHAL_SLICING_START = new BasicAttackAnimation(0.15F, 0.0F, 0.11F, 0.38F, ColliderPreset.FIST_FIXED, biped.rootJoint, "biped/new/vanilla_lethal_slicing_start", biped)
                .addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F).addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
                .addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT);

        KATANA_SHEATH_AUTO = new BasicAttackAnimation(0.06F, 0.0F, 0.06F, 0.65F, null, biped.rootJoint, "biped/new/katana_sheath_auto", biped)
                .addProperty(AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(50.0F)).addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.5F))
                .addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(3)).addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP);
        KATANA_SHEATH_DASH = new DashAttackAnimation(0.06F, 0.08F, 0.05F, 0.11F, 0.65F, StarNewColliderPreset.LETHAL_SLICING1, biped.rootJoint, "biped/new/katana_sheath_dash", biped)
                .addProperty(AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(50.0F)).addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
                .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.5F)).addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(3))
                .addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP);
        KATANA_SHEATH_AIRSLASH = new AirSlashAnimation(0.1F, 0.1F, 0.16F, 0.3F, null, biped.toolR, "biped/new/katana_sheath_airslash", biped)
                .addProperty(AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(50.0F)).addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.5F))
                .addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(3)).addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)
                .addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.0F);

        OLD_GREATSWORD_AUTO1 = new BasicAttackAnimation(0.2F, 0.4F, 0.6F, 0.8F, null, biped.toolR, "biped/new/old_greatsword_auto1", biped)
                .addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.0F);
        OLD_GREATSWORD_AUTO2 = new BasicAttackAnimation(0.2F, 0.4F, 0.6F, 0.8F, null, biped.toolR, "biped/new/old_greatsword_auto2", biped)
                .addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.0F);
        OLD_GREATSWORD_DASH = new BasicAttackAnimation(0.11F, 0.4F, 0.65F, 0.8F, 1.2F, null, biped.toolR, "biped/new/old_greatsword_dash", biped)
                .addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.0F);
        OLD_GREATSWORD_AIRSLASH = new BasicAttackAnimation(0.1F, 0.5F, 0.55F, 0.71F, 0.75F, null, biped.toolR, "biped/new/old_greatsword_airslash", biped)
                .addProperty(AttackPhaseProperty.SOURCE_TAG, Set.of(SourceTags.FINISHER)).addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.0F);

        LION_CLAW = new BasicAttackAnimation(0.08F, 1.54F, 1.55F, 1.6F, 3.0F, null, biped.toolR, "biped/new/lion_claw", biped)
                .addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.EVISCERATE).addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
                .addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.2F);
        DUAL_SLASH = new AttackAnimation(0.1F, "biped/new/dual_slash", biped,
                new AttackAnimation.Phase(.0F, 0.2F, 0.31F, 0.4F, 0.4F, biped.toolR, null), new AttackAnimation.Phase(0.4F, 0.5F, 0.61F, 0.65F, 0.65F, InteractionHand.OFF_HAND, biped.toolL, null),
                new AttackAnimation.Phase(0.65F, 0.75F, 0.85F, 1.15F, Float.MAX_VALUE, biped.toolR, null))
                .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.5F))
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_RUSH_FINISHER)
                .addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.BLADE_RUSH_SKILL).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE);

        WIND_SLASH = new AttackAnimation(0.2F, "biped/new/wind_slash", biped,
                new AttackAnimation.Phase(.0F, 0.3F, 0.35F, 0.55F, 0.9F, 0.9F, biped.toolR, null), new AttackAnimation.Phase(0.9F, 0.95F, 1.05F, 1.2F, 1.5F, 1.5F, biped.toolR, null),
                new AttackAnimation.Phase(1.5F, 1.65F, 1.75F, 1.95F, 2.5F, Float.MAX_VALUE, biped.toolR, null))
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 0.95F);

        SPEAR_SLASH = (new AttackAnimation(0.11F, "biped/new/spear_slash", biped,
                new AttackAnimation.Phase(0.0F, 0.3F, 0.36F, 0.5F, 0.5F, biped.toolR, null),
                new AttackAnimation.Phase(0.5F, 0.5F, 0.56F, 0.75F, 0.75F, biped.toolR, null),
                new AttackAnimation.Phase(0.75F, 0.75F, 0.81F, 1.05F, Float.MAX_VALUE, biped.toolR, null)))
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.2F));

        SWORD_SLASH = new AttackAnimation(0.34F, 0.1F, 0.35F, 0.46F, 0.79F, null, biped.toolR, "biped/new/sword_slash", biped)
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_RUSH_FINISHER)
                .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.6F))
                .addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.BLADE_RUSH_SKILL)
                .addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH);

        YAMATO_IDLE = new StaticAnimation(true, "biped/new/yamato/yamato_idle", biped);
        YAMATO_WALK = new StaticAnimation(true, "biped/new/yamato/yamato_walk", biped);
        YAMATO_RUN = new StaticAnimation(true, "biped/new/yamato/yamato_run", biped);
        YAMATO_GUARD = new StaticAnimation(true, "biped/new/yamato/yamato_guard", biped);
        YAMATO_GUARD_HIT = new GuardAnimation(0.05F, "biped/new/yamato/yamato_guard_hit", biped);
        YAMATO_ACTIVE_GUARD_HIT = (new GuardAnimation(0.02F, "biped/new/yamato/yamato_guard_parry", biped))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE);
        YAMATO_ACTIVE_GUARD_HIT2 = (new GuardAnimation(0.02F, "biped/new/yamato/yamato_guard_parry2", biped))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE);
        YAMATO_AUTO1 = (new BasicAttackWinAnimation(0.10F, 0.0F, 1.41F, 0.75F, 1.3F, 0.27F, 1.41F, 0.0F, 0.0F, "biped/new/yamato/yamato_auto1", biped,
                (new AttackAnimation.Phase(0.0F, 0.27F, 0.271F, 0.33F, 0.56F, 0.56F, InteractionHand.MAIN_HAND, biped.toolL, StarNewColliderPreset.YAMATO_SHEATH))
                        .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.5F))
                        .addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)
                        .addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT),
                (new AttackAnimation.Phase(0.56F, 0.65F, 0.651F, 0.68F, 0.70F, 0.75F, InteractionHand.MAIN_HAND, biped.toolL, StarNewColliderPreset.YAMATO_SHEATH))
                        .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.5F))
                        .addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)
                        .addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)))
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(0.9F))
                .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, Float.valueOf(0.5F))
                .addState(EntityState.MOVEMENT_LOCKED, true);
        YAMATO_AUTO2 = (new BasicAttackWinAnimation(0.10F, 0.0F, 2.1F, 0.7F, 1.2F, 0.3F, 2.1F, 0.0F, 0.0F, "biped/new/yamato/yamato_auto2", biped,
                new AttackAnimation.Phase(0.0F, 0.3F, 0.37F, 0.53F, 0.53F, biped.toolR, null)
                        .addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)
                        .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new AttackAnimation.Phase(0.53F, 0.6F, 0.67F, 1.05F, 1.10F, biped.toolR, null)))
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(0.9F))
                .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, Float.valueOf(0.5F))
                .addEvents(
                        AnimationEvent.TimeStampedEvent.create(1.8F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(Sounds.YAMATO_IN))
                .addState(EntityState.MOVEMENT_LOCKED, true);
        YAMATO_AUTO3 = new BasicAttackWinAnimation(0.10F, 0.0F, 2.65F, 1.3F, 1.75F, 0.7F, 2.65F, 0.0F, 0.0F, "biped/new/yamato/yamato_auto3", biped,
                new AttackAnimation.Phase(0.0F, 0.7F, 0.78F, 0.88F, 0.88F, biped.toolR, null).addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new AttackAnimation.Phase(0.88F, 1.12F, 1.23F, 1.25F, 1.30F, biped.toolR, null)
                        .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.SHORT)
                        .addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.adder(1.5F)))
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(0.9F))
                .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.SHORT)
                .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, Float.valueOf(0.5F))
                .addEvents(
                        AnimationEvent.TimeStampedEvent.create(2.11F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(Sounds.YAMATO_IN))
                .addState(EntityState.MOVEMENT_LOCKED, true);
        YAMATO_AUTO4 = (new BasicAttackWinAnimation(0.15F, 0.0F, 2.87F, 2.87F, 2.87F, 0.81F, 2.87F, 0.81F, 2.87F, "biped/new/yamato/yamato_auto4", biped,
                (new AttackAnimation.Phase(0.0F, 0.81F, 0.9F, 2.87F, 2.87F, biped.toolR, StarNewColliderPreset.YAMATO_P)).addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG).addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.0F)).addProperty(AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(15.0F))))
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(0.95F))
                .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, Float.valueOf(0.5F))
                .addEvents(
                        AnimationEvent.TimeStampedEvent.create(2.50F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(Sounds.YAMATO_IN),
                        AnimationEvent.TimeStampedEvent.create(2.55F, STAMINA, AnimationEvent.Side.SERVER))
                .addState(EntityState.MOVEMENT_LOCKED, true);
        YAMATO_DASH = new DashAttackAnimation(0.12F, 0.1F, 0.25F, 0.4F, 0.65F, null, biped.toolR, "biped/new/yamato/yamato_dash", biped)
                .addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(2))
                .addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, Boolean.valueOf(true));
        YAMATO_AIRSLASH = new AirSlashAnimation(0.25F, 0.15F, 0.26F, 0.5F, null, biped.toolR, "biped/new/yamato/yamato_airslash", biped)
                .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.adder(1.5F));
        YAMATO_POWER1 = (new AttackAnimation(0.1F, 0.42F, 0.43F, 0.53F, 3.83F, StarNewColliderPreset.YAMATO_P, biped.toolR, "biped/new/yamato/skill/yamato_power1", biped))
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.LONG)
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.adder(3.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.75F))
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.multiplier(1.25F))
                .addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(3))
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 0.8F).addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, Float.valueOf(0.5F))
                .addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, true)
                .addEvents(AnimationEvent.TimeStampedEvent.create(2.89F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(Sounds.YAMATO_IN),
                        AnimationEvent.TimeStampedEvent.create(2.90F, STAMINASKILL, AnimationEvent.Side.SERVER));
        YAMATO_POWER2 = (new AttackAnimation(0.05F, "biped/new/yamato/skill/yamato_power2", biped,
                (new AttackAnimation.Phase(0.0F, 0.62F, 0.68F, 1.05F, 1.05F, biped.toolR, StarNewColliderPreset.YAMATO_P))
                        .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                        .addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.adder(5.0F))
                        .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.5F)),
                (new AttackAnimation.Phase(1.05F, 1.28F, 1.55F, 4.91F, 4.91F, biped.toolR, StarNewColliderPreset.YAMATO_P))
                        .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
                        .addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.EVISCERATE)
                        .addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.EVISCERATE)
                        .addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.adder(2.5F))
                        .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.5F))
                        .addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(5))
                        .addProperty(AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.multiplier(1.5F))))
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(0.8F))
                .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, Float.valueOf(0.5F))
                .addEvents(
                        AnimationEvent.TimeStampedEvent.create(4.1F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(Sounds.YAMATO_IN),
                        AnimationEvent.TimeStampedEvent.create(4.15F, STAMINASKILL, AnimationEvent.Side.SERVER));
        YAMATO_POWER3 = (new AttackAnimation(0.01F, "biped/new/yamato/skill/yamato_power3", biped,
                (new AttackAnimation.Phase(0.0F, 0.67F, 0.73F, 0.88F, 0.88F, biped.rootJoint, StarNewColliderPreset.YAMATO_DASH))
                        .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                        .addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.adder(2.0F))
                        .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.5F)),
                (new AttackAnimation.Phase(0.88F, 0.94F, 1.0F, 1.28F, 1.28F, biped.rootJoint, StarNewColliderPreset.YAMATO_DASH))
                        .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                        .addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.adder(3.0F))
                        .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.5F))))
                .addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, Boolean.valueOf(true))
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.35F))
                .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, Float.valueOf(0.5F))
                .addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, true);
        YAMATO_POWER3_REPEAT = (new AttackAnimation(0.0F, "biped/new/yamato/skill/yamato_power3_repeat", biped,
                (new AttackAnimation.Phase(0.0F, 0.04F, 0.09F, 0.25F, 0.25F, biped.rootJoint, StarNewColliderPreset.YAMATO_DASH)).addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD).addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.adder(2.0F)).addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.1F)),
                (new AttackAnimation.Phase(0.25F, 0.31F, 0.36F, 0.65F, 0.65F, biped.rootJoint, StarNewColliderPreset.YAMATO_DASH)).addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD).addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.adder(2.0F)).addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.1F))))
                .addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, Boolean.valueOf(true))
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.35F))
                .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, Float.valueOf(0.5F))
                .addState(EntityState.MOVEMENT_LOCKED, true);
        YAMATO_POWER3_FINISH = (new AttackAnimation(0.25F, "biped/new/yamato/skill/yamato_power3_finish", biped,
                (new AttackAnimation.Phase(0.0F, 0.05F, 0.13F, 0.43F, 0.43F, biped.rootJoint, StarNewColliderPreset.YAMATO_DASH)).addProperty(AttackPhaseProperty.HIT_PRIORITY, HitEntityList.Priority.TARGET).addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD).addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(4.0F)).addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(1.0F)).addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP),
                (new AttackAnimation.Phase(0.43F, 0.82F, 0.9F, 1.35F, 1.35F, biped.rootJoint, StarNewColliderPreset.YAMATO_DASH_FINISH))).addProperty(AttackPhaseProperty.STUN_TYPE, StunType.KNOCKDOWN)
                        .addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.BLADE_RUSH_SKILL))
                .addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, Boolean.valueOf(true))
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.35F))
                .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, Float.valueOf(0.5F))
                .addEvents(
                        AnimationEvent.TimeStampedEvent.create(0.89F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(Sounds.YAMATO_IN))
                .addState(EntityState.MOVEMENT_LOCKED, true);
        YAMATO_POWER_DASH = (new AttackAnimation(0.0F, "biped/new/yamato/skill/yamato_power_dash", biped,
                (new AttackAnimation.Phase(0.0F, 0.38F, 0.9F, 1.17F, 1.17F, biped.rootJoint, StarNewColliderPreset.YAMATO_DASH)
                        .addProperty(AttackPhaseProperty.HIT_PRIORITY, HitEntityList.Priority.TARGET)
                        .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.5F))
                        .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                        .addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.adder(4.0F))
                        .addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)),
                (new AttackAnimation.Phase(1.17F, 1.6F, 1.67F, 2.26F, 2.26F, biped.rootJoint, StarNewColliderPreset.YAMATO_DASH_FINISH))))
                        .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.KNOCKDOWN)
                        .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.0F))
                        .addProperty(AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.multiplier(1.5F))
                        .addProperty(AttackPhaseProperty.SWING_SOUND, Sounds.YAMATO_IN)
                        .addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.BLADE_RUSH_SKILL)
                .addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.35F)
                .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.5F)
                .addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, true)
                .addEvents(AnimationEvent.TimeStampedEvent.create(0.65F, (entitypatch, animation, params) -> {
                            Entity entity = entitypatch.getOriginal();
                            entity.level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
                        }, AnimationEvent.Side.CLIENT)
                );
        YAMATO_COUNTER1 = new DodgeAttackAnimation(0.05F, "biped/new/yamato/skill/yamato_counter_1", biped,
                new AttackAnimation.Phase(0.0F, 0.71F, 0.78F, 2.38F, 2.38F, biped.toolR, StarNewColliderPreset.YAMATO_P)
                        .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.0F))
                        .addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.adder(1.0F))
                        .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
                        .addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)
                        .addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(3.0F)))
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 0.9F)
                .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.5F)
                .addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, true);
        YAMATO_COUNTER2 = (new AttackAnimation(0.02F, 0.01F, 0.55F, 0.56F, 1.15F, StarNewColliderPreset.YAMATO_P0, biped.rootJoint, "biped/new/yamato/skill/yamato_counter_2", biped)
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.5F))
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, Sounds.YAMATO_IN)
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(3.0F)))
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.1F)
                .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.5F);
        YAMATO_STRIKE1 = (new BasicAttackWinAnimation(0.10F, 0.0F, 2.1F, 0.7F, 1.2F, 0.3F, 2.1F, 0.0F, 0.0F, "biped/new/yamato/skill/yamato_strike1", biped,
                (new AttackAnimation.Phase(0.0F, 0.51F, 30.62F, 0.72F, 0.72F, biped.toolR, StarNewColliderPreset.YAMATO_P))
                        .addProperty(AttackPhaseProperty.HIT_PRIORITY, HitEntityList.Priority.TARGET)
                        .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                        .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.5F)),
                (new AttackAnimation.Phase(0.75F, 0.78F, 0.87F, 1.2F, 1.33F, biped.toolR, StarNewColliderPreset.YAMATO_P))))
                        .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                        .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.85F))
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(0.85F))
                .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, Float.valueOf(0.5F))
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)
                .addProperty(AnimationProperty.StaticAnimationProperty.TIME_STAMPED_EVENTS, new AnimationEvent.TimeStampedEvent[]{
                        AnimationEvent.TimeStampedEvent.create(1.55F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(Sounds.YAMATO_IN),
                        AnimationEvent.TimeStampedEvent.create(0.38F, (entitypatch, animation, params) -> {
                            Entity entity = entitypatch.getOriginal();
                            entity.level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
                        }, AnimationEvent.Side.CLIENT),
                });

        YAMATO_STRIKE2 = (new BasicAttackWinAnimation(0.10F, 0.0F, 2.65F, 1.3F, 1.75F, 0.7F, 2.65F, 0.0F, 0.0F, "biped/new/yamato/skill/yamato_strike2", biped,
                (new AttackAnimation.Phase(0.0F, 0.82F, 0.95F, 1.0F, 1.0F, biped.toolR, StarNewColliderPreset.YAMATO_P))
                        .addProperty(AttackPhaseProperty.HIT_PRIORITY, HitEntityList.Priority.TARGET)
                        .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                        .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.85F)),
                (new AttackAnimation.Phase(1.0F, 1.1F, 1.32F, 1.61F, 1.61F, biped.toolR, StarNewColliderPreset.YAMATO_P)))
                        .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                        .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.85F))
                        .addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.adder(1.5F)))
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 0.85F)
                .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.5F)
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)
                .addEvents(
                        AnimationEvent.TimeStampedEvent.create(1.95F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(Sounds.YAMATO_IN))
                .addEvents(AnimationEvent.TimeStampedEvent.create(0.38F, (entitypatch, animation, params) -> {
                            Entity entity = entitypatch.getOriginal();
                            entity.level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
                        }, AnimationEvent.Side.CLIENT)
                );
        YAMATO_POWER0_1 = (new BasicAttackWinAnimation(0.1F, 0.0F, 2.87F, 2.87F, 2.87F, 0.81F, 2.87F, 0.81F, 0.83F, "biped/new/yamato/skill/yamato_power0_1", biped,
                new AttackAnimation.Phase(0.0F, 0.75F, 0.767F, 0.83F, 0.83F, biped.rootJoint, ColliderPreset.BATTOJUTSU)))
                .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.5F))
                .addProperty(AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(0.25F))
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.SWORD_IN)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE);
        YAMATO_POWER0_2 = (new DodgeAttackAnimation(0.1F, "biped/new/yamato/skill/yamato_power0_2", biped,
                new AttackAnimation.Phase(0.0F, 0.55F, 0.567F, 0.96F, 0.96F, biped.rootJoint, StarNewColliderPreset.YAMATO_P0)
                .addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.25F))
                .addProperty(AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(0.25F))))
                .addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
                .addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.20F)
                .addEvents(AnimationEvent.TimeStampedEvent.create(0.0F, (entitypatch, animation, params) -> {
                            Entity entity = entitypatch.getOriginal();
                            entity.level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
                        }, AnimationEvent.Side.CLIENT),
                        AnimationEvent.TimeStampedEvent.create(0.0F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(Sounds.FORESIGHT),
                        AnimationEvent.TimeStampedEvent.create(0.15F, STAMINASKILL, AnimationEvent.Side.SERVER));
    }
    public static final AnimationEvent.AnimationEventConsumer STAMINA = (entitypatch, animation, params) -> {
        if (entitypatch instanceof PlayerPatch) {
            PlayerPatch<?> playerPatch = (PlayerPatch<?>) entitypatch;
            float currentStamina = playerPatch.getStamina();
            float maxStamina = playerPatch.getMaxStamina();
            float recoveredStamina = currentStamina * 0.30F;
            playerPatch.setStamina(currentStamina + recoveredStamina);
        }
    };
    public static final AnimationEvent.AnimationEventConsumer STAMINASKILL = (entitypatch, animation, params) -> {
        if (entitypatch instanceof PlayerPatch) {
            PlayerPatch<?> playerPatch = (PlayerPatch<?>) entitypatch;
            float maxStamina = playerPatch.getMaxStamina();
            float currentStamina = playerPatch.getStamina();
            float recoveredStamina = maxStamina * 0.20F;
            playerPatch.setStamina(currentStamina + recoveredStamina);
        }
    };

    public static final AnimationProperty.PlaybackTimeModifier EXE = (self, entitypatch, speed, elapsedTime) -> 1.0F;

}
