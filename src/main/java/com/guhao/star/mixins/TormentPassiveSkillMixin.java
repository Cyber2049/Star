package com.guhao.star.mixins;

import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import reascer.wom.gameasset.WOMAnimations;
import reascer.wom.skill.weaponinnate.TrueBerserkSkill;
import reascer.wom.skill.weaponpassive.TormentPassiveSkill;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.level.block.FractureBlockState;

import java.util.Random;
import java.util.UUID;

@Mixin(value = TormentPassiveSkill.class,remap = false)
public abstract class TormentPassiveSkillMixin extends PassiveSkill {
    @Shadow
    private static final SkillDataManager.SkillDataKey<Integer> TIMER = null;
    @Shadow
    private static final SkillDataManager.SkillDataKey<Boolean> ACTIVE = null;
    @Shadow
    public static final SkillDataManager.SkillDataKey<Integer> CHARGING_TIME = null;
    @Shadow
    public static final SkillDataManager.SkillDataKey<Integer> SAVED_CHARGE = null;
    @Shadow
    private static final SkillDataManager.SkillDataKey<Boolean> CHARGING = null;
    @Shadow
    private static final SkillDataManager.SkillDataKey<Boolean> SUPER_ARMOR = null;
    @Shadow
    private static final SkillDataManager.SkillDataKey<Boolean> CHARGED = null;
    @Shadow
    private static final SkillDataManager.SkillDataKey<Boolean> CHARGED_ATTACK = null;
    @Shadow
    private static final SkillDataManager.SkillDataKey<Boolean> MOVESPEED = null;
    @Shadow
    private static final UUID EVENT_UUID = null;
    @Shadow
    public void consume_stamina(SkillContainer container) {
    }
    public TormentPassiveSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    @Override
    public void updateContainer(SkillContainer container) {
        if(container.getExecuter().isLogicalClient()) {
            if ((container.getExecuter().getCurrentLivingMotion() == LivingMotions.WALK || container.getExecuter().getCurrentLivingMotion() == LivingMotions.RUN)) {
                PlayerPatch<?> entitypatch = container.getExecuter();
                float interpolation = 0.0F;
                OpenMatrix4f transformMatrix;
                transformMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(interpolation), Armatures.BIPED.toolR);
                transformMatrix.translate(new Vec3f(0,-0.0F,-1.2F));
                OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix,transformMatrix);
                transformMatrix.translate(new Vec3f(0,0.0F,-(new Random().nextFloat() * 1.0f)));

                float dpx = transformMatrix.m30 + (float) entitypatch.getOriginal().getX();
                float dpy = transformMatrix.m31 + (float) entitypatch.getOriginal().getY();
                float dpz = transformMatrix.m32 + (float) entitypatch.getOriginal().getZ();
                BlockState blockstate = entitypatch.getOriginal().level.getBlockState(new BlockPos(new Vec3(dpx,dpy,dpz)));
                BlockPos blockpos = new BlockPos(new Vec3(dpx,dpy,dpz));
                while ((blockstate.getBlock() instanceof BushBlock || blockstate.isAir()) && !blockstate.is(Blocks.VOID_AIR)) {
                    dpy--;
                    blockstate = entitypatch.getOriginal().level.getBlockState(new BlockPos(new Vec3(dpx,dpy,dpz)));
                }

                while (blockstate instanceof FractureBlockState) {
                    blockpos = new BlockPos(dpx, dpy--, dpz);
                    blockstate = entitypatch.getOriginal().level.getBlockState(blockpos.below());
                }
                if ((transformMatrix.m31 + entitypatch.getOriginal().getY()) < dpy+1.50f) {
                    for (int i = 0; i < 2; i++) {
                        entitypatch.getOriginal().level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate),
                                (transformMatrix.m30 + entitypatch.getOriginal().getX()),
                                (transformMatrix.m31 + entitypatch.getOriginal().getY())-0.2f,
                                (transformMatrix.m32 + entitypatch.getOriginal().getZ()),
                                (new Random().nextFloat() - 0.5F)*0.005f,
                                (new Random().nextFloat()) * 0.02f,
                                (new Random().nextFloat() - 0.5F)*0.005f);
                    }
                }
            }
        }

        if (container.getDataManager().getDataValue(CHARGED)) {
            PlayerPatch<?> entitypatch = container.getExecuter();
            int numberOf = 2;
            float partialScale = 1.0F / (numberOf - 1);
            float interpolation = 0.0F;
            OpenMatrix4f transformMatrix;
            for (int i = 0; i < numberOf; i++) {
                transformMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(interpolation), Armatures.BIPED.toolR);
                transformMatrix.translate(new Vec3f(0,0.0F,-1.0F));
                OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix,transformMatrix);
                transformMatrix.translate(new Vec3f(0,0.0F,-(new Random().nextFloat() * 1.0f)));
                entitypatch.getOriginal().level.addParticle(new DustParticleOptions(new Vector3f(0.8f,0.6f,0f),1.0f),
                        (transformMatrix.m30 + entitypatch.getOriginal().getX() + (new Random().nextFloat() - 0.5F)*0.55f),
                        (transformMatrix.m31 + entitypatch.getOriginal().getY() + (new Random().nextFloat() - 0.5F)*0.55f),
                        (transformMatrix.m32 + entitypatch.getOriginal().getZ() + (new Random().nextFloat() - 0.5F)*0.55f),
                        0,
                        0,
                        0);
                entitypatch.getOriginal().level.addParticle(ParticleTypes.FLAME,
                        (transformMatrix.m30 + entitypatch.getOriginal().getX() + (new Random().nextFloat() - 0.5F)*0.75f),
                        (transformMatrix.m31 + entitypatch.getOriginal().getY() + (new Random().nextFloat() - 0.5F)*0.75f),
                        (transformMatrix.m32 + entitypatch.getOriginal().getZ() + (new Random().nextFloat() - 0.5F)*0.75f),
                        0,
                        0,
                        0);
                interpolation += partialScale;
            }
        }
        if(!container.getExecuter().isLogicalClient()) {
            AttributeModifier charging_Movementspeed = new AttributeModifier(EVENT_UUID, "torment.charging_movespeed", 0.1f, AttributeModifier.Operation.MULTIPLY_TOTAL);
            ServerPlayerPatch executer = (ServerPlayerPatch) container.getExecuter();
            int sweeping_edge = EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, executer.getOriginal());

            if (container.getDataManager().getDataValue(CHARGING) && !container.getExecuter().getOriginal().isUsingItem() && container.getExecuter().getEntityState().canBasicAttack()){
                container.getDataManager().setDataSync(MOVESPEED, true, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
                int animation_timer = container.getDataManager().getDataValue(CHARGING_TIME);
                if (container.getDataManager().getDataValue(CHARGING_TIME) < 20 && container.getDataManager().getDataValue(SAVED_CHARGE) >= 20) {
                    animation_timer = container.getDataManager().getDataValue(SAVED_CHARGE);
                }
                if (container.getDataManager().getDataValue(CHARGING_TIME) >= 110) {
                    container.getExecuter().getOriginal().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,4, 2,true,false,false));
                    container.getExecuter().getOriginal().level.playSound(null,
                            container.getExecuter().getOriginal().getX(),
                            container.getExecuter().getOriginal().getY(),
                            container.getExecuter().getOriginal().getZ(),
                            EpicFightSounds.WHOOSH_BIG, SoundSource.PLAYERS,
                            1.0F, 1.2F);
                    if (!container.getExecuter().getOriginal().isCreative()) {
                        if (!container.getExecuter().consumeStamina(3)) {
                            container.getExecuter().setStamina(0);
                        }
                    }
                } else if (animation_timer >= 80) {
                    container.getExecuter().playAnimationSynchronized(WOMAnimations.TORMENT_CHARGED_ATTACK_3, 0);
                } else if (animation_timer >= 50) {
                    container.getExecuter().playAnimationSynchronized(WOMAnimations.TORMENT_CHARGED_ATTACK_2, 0);
                } else if (animation_timer >= 20) {
                    container.getExecuter().playAnimationSynchronized(WOMAnimations.TORMENT_CHARGED_ATTACK_1, 0);
                } else {

                    if (!container.getExecuter().getSkill(SkillSlots.WEAPON_INNATE).getDataManager().getDataValue(TrueBerserkSkill.ACTIVE)) {
                        container.getExecuter().getOriginal().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,4, 1,true,false,false));
                        container.getExecuter().getOriginal().level.playSound(null,
                                container.getExecuter().getOriginal().getX(),
                                container.getExecuter().getOriginal().getY(),
                                container.getExecuter().getOriginal().getZ(),
                                EpicFightSounds.WHOOSH_BIG, SoundSource.PLAYERS,
                                1.0F, 1.2F);
                        if (!container.getExecuter().getOriginal().isCreative()) {
                            if (!container.getExecuter().consumeStamina(3)) {
                                container.getExecuter().setStamina(0);
                            }
                        }
                    } else {
                        container.getExecuter().getOriginal().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,7, 3,true,false,false));
                        container.getExecuter().getOriginal().level.playSound(null,
                                container.getExecuter().getOriginal().getX(),
                                container.getExecuter().getOriginal().getY(),
                                container.getExecuter().getOriginal().getZ(),
                                EpicFightSounds.WHOOSH_BIG, SoundSource.PLAYERS,
                                1.0F, 1.2F);
                        float stamina = container.getExecuter().getStamina();
                        float maxStamina = container.getExecuter().getMaxStamina();
                        float staminaRegen = (float)container.getExecuter().getOriginal().getAttributeValue(EpicFightAttributes.STAMINA_REGEN.get());
                        int regenStandbyTime = 900 / (int)(30 * staminaRegen);

                        if (container.getExecuter().getTickSinceLastAction() > regenStandbyTime) {
                            if (!container.getExecuter().getOriginal().isCreative()) {
                                float staminaFactor = 1.0F + (float)Math.pow((stamina / (maxStamina - stamina * 0.5F)), 2);
                                if (!container.getExecuter().consumeStamina(2 + maxStamina * 0.05F * staminaFactor * staminaRegen)) {
                                    container.getExecuter().setStamina(0);
                                }
                            }
                        } else {
                            if (!container.getExecuter().getOriginal().isCreative()) {
                                if (!container.getExecuter().consumeStamina(2)) {
                                    container.getExecuter().setStamina(0);
                                }
                            }
                        }
                    }
                }
                container.getDataManager().setDataSync(CHARGING, false, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
                container.getDataManager().setDataSync(CHARGING_TIME, 0, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
                container.getDataManager().setDataSync(SAVED_CHARGE, 0, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
                container.getExecuter().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(charging_Movementspeed);
            }
            if (container.getDataManager().getDataValue(CHARGING)) {
                if (container.getExecuter().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).getModifier(EVENT_UUID) == null && container.getDataManager().getDataValue(MOVESPEED)) {
                    container.getExecuter().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(charging_Movementspeed);
                }

                container.getDataManager().setDataSync(CHARGING_TIME, container.getDataManager().getDataValue(CHARGING_TIME) +1, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
                if (container.getDataManager().getDataValue(CHARGING_TIME) <= 130) {
                    if (container.getDataManager().getDataValue(CHARGING_TIME) == 20) {
                        container.getDataManager().setDataSync(SAVED_CHARGE, 0, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
                        container.getExecuter().getOriginal().level.playSound(null,
                                container.getExecuter().getOriginal().getX(),
                                container.getExecuter().getOriginal().getY(),
                                container.getExecuter().getOriginal().getZ(),
                                SoundEvents.ANVIL_LAND, SoundSource.PLAYERS,
                                1.0F, 0.6F);
                        this.consume_stamina(container);
                    }
                    if (container.getDataManager().getDataValue(CHARGING_TIME) == 50) {
                        container.getExecuter().getOriginal().level.playSound(null,
                                container.getExecuter().getOriginal().getX(),
                                container.getExecuter().getOriginal().getY(),
                                container.getExecuter().getOriginal().getZ(),
                                SoundEvents.ANVIL_LAND, SoundSource.PLAYERS,
                                1.0F, 0.65F);
                        this.consume_stamina(container);
                    }
                    if (container.getDataManager().getDataValue(CHARGING_TIME) == 80) {
                        container.getExecuter().getOriginal().level.playSound(null,
                                container.getExecuter().getOriginal().getX(),
                                container.getExecuter().getOriginal().getY(),
                                container.getExecuter().getOriginal().getZ(),
                                SoundEvents.ANVIL_LAND, SoundSource.PLAYERS,
                                1.0F, 0.7F);
                        this.consume_stamina(container);
                    }
                    if (container.getDataManager().getDataValue(CHARGING_TIME) == 110) {
                        container.getDataManager().setDataSync(CHARGED, true, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
                        container.getExecuter().getOriginal().level.playSound(null,
                                container.getExecuter().getOriginal().getX(),
                                container.getExecuter().getOriginal().getY(),
                                container.getExecuter().getOriginal().getZ(),
                                SoundEvents.ANVIL_LAND, SoundSource.PLAYERS,
                                1.0F, 0.5F);
                        container.getExecuter().getOriginal().level.playSound(null,
                                container.getExecuter().getOriginal().getX(),
                                container.getExecuter().getOriginal().getY(),
                                container.getExecuter().getOriginal().getZ(),
                                SoundEvents.BELL_BLOCK, SoundSource.MASTER,
                                2.5F, 0.5F);
                        this.consume_stamina(container);
                    }
                    if (container.getDataManager().getDataValue(CHARGING_TIME) == 130) {
                        container.getDataManager().setDataSync(CHARGING_TIME, 0, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
                        container.getExecuter().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(charging_Movementspeed);
                    }
                }
            } else {
                container.getExecuter().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(charging_Movementspeed);
            }
        }
    }

}
