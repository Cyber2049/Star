package com.guhao.star.mixins;

import com.guhao.star.regirster.effect_reg;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.skill.ActiveGuardSkill;
import yesman.epicfight.skill.GuardSkill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;

@Mixin(value = ActiveGuardSkill.class, remap = false, priority = 5000)
public class ActiveGuardMixin extends GuardSkill {
    public ActiveGuardMixin(Builder builder) {
        super(builder);
    }

    @Mutable
    @Final
    @Shadow
    private static final SkillDataManager.SkillDataKey<Integer> LAST_ACTIVE;

    static {
        LAST_ACTIVE = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
    }

    @Inject(at = @At("HEAD"), method = "guard", cancellable = true)
    public void guard(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced, CallbackInfo ci) {
        ci.cancel();
        if (this.isHoldingWeaponAvailable(event.getPlayerPatch(), itemCapability, BlockType.ADVANCED_GUARD)) {
            DamageSource damageSource = (DamageSource) event.getDamageSource();
            if (this.isBlockableSource(damageSource, true)) {
                ServerPlayer playerentity = (ServerPlayer) ((ServerPlayerPatch) event.getPlayerPatch()).getOriginal();
                boolean successParrying = playerentity.tickCount - (Integer) container.getDataManager().getDataValue(LAST_ACTIVE) < 8;
                float penalty = (Float) container.getDataManager().getDataValue(PENALTY);
                ((ServerPlayerPatch) event.getPlayerPatch()).playSound(EpicFightSounds.CLASH, -0.05F, 0.1F);
                ((HitParticleType) EpicFightParticles.HIT_BLUNT.get()).spawnParticleWithArgument((ServerLevel) playerentity.level, HitParticleType.FRONT_OF_EYES, HitParticleType.ZERO, playerentity, damageSource.getDirectEntity());
                if (successParrying) {
                    penalty = 0.0F;
                    knockback *= 0.4F;
                    ///////////////////////////////
                    if (event.getDamageSource().getDirectEntity() instanceof Monster) {
                        LivingEntity L = (LivingEntity) event.getDamageSource().getDirectEntity();
                        if (event.getPlayerPatch().getOriginal() instanceof Player && !(L.hasEffect(effect_reg.DEFENSE.get()))) {
                            //L.hurt(DamageSource.playerAttack(event.getPlayerPatch().getOriginal()), (float) (event.getAmount()*Math.random())+2);     //这个是受伤
                            L.addEffect(new MobEffectInstance(effect_reg.DEFENSE.get(), 140, 0));
                        } else if ((L.getEffect(effect_reg.DEFENSE.get()).getAmplifier() >= 15)) {
                            L.removeEffect(effect_reg.DEFENSE.get());
                            LazyOptional<EntityPatch> optional = L.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY);
                            optional.ifPresent(patch -> {
                                if (patch instanceof LivingEntityPatch<?> livingEntityPatch) {
                                    ClientAnimator Animator = new ClientAnimator(livingEntityPatch);
                                    livingEntityPatch.playAnimationSynchronized(Animations.COMMON_GUARD_BREAK, 0.0F);
                                    /*
                                    livingEntityPatch.initAnimator(Animator);
                                    livingEntityPatch.playAnimationSynchronized(Animations.BIPED_DEATH, 0.0F);
                                    livingEntityPatch.playAnimationSynchronized(Animations.DRAGON_DEATH, 0.0F);
                                    livingEntityPatch.playAnimationSynchronized(Animations.CREEPER_DEATH, 0.0F);
                                    livingEntityPatch.playAnimationSynchronized(Animations.ENDERMAN_DEATH, 0.0F);
                                    livingEntityPatch.playAnimationSynchronized(Animations.GOLEM_DEATH, 0.0F);
                                    livingEntityPatch.playAnimationSynchronized(Animations.HOGLIN_DEATH, 0.0F);
                                    livingEntityPatch.playAnimationSynchronized(Animations.PIGLIN_DEATH, 0.0F);
                                    livingEntityPatch.playAnimationSynchronized(Animations.RAVAGER_DEATH, 0.0F);
                                    livingEntityPatch.playAnimationSynchronized(Animations.SPIDER_DEATH, 0.0F);
                                    livingEntityPatch.playAnimationSynchronized(Animations.VEX_DEATH, 0.0F);
                                    livingEntityPatch.playAnimationSynchronized(Animations.WITHER_DEATH, 0.0F);
                                    */

                                }
                            });

                        } else {
                            MobEffectInstance a = L.getEffect(effect_reg.DEFENSE.get());
                            assert a != null;
                            int e = a.getAmplifier() + 1;
                            L.addEffect(new MobEffectInstance(effect_reg.DEFENSE.get(), 140, e));
                            L.playSound(EpicFightSounds.NEUTRALIZE_BOSSES,-0.2F,0.2F);
                        }
                    }
                    /////////////////////////////////
                } else {
                    penalty += this.getPenaltyMultiplier(itemCapability);
                    container.getDataManager().setDataSync(PENALTY, penalty, playerentity);
                }

                if (damageSource.getDirectEntity() instanceof LivingEntity) {
                    knockback += (float) EnchantmentHelper.getKnockbackBonus((LivingEntity) damageSource.getDirectEntity()) * 0.1F;
                }

                ((ServerPlayerPatch) event.getPlayerPatch()).knockBackEntity(damageSource.getDirectEntity().position(), knockback);
                float stamina = ((ServerPlayerPatch) event.getPlayerPatch()).getStamina() - penalty * impact;
                ((ServerPlayerPatch) event.getPlayerPatch()).setStamina(stamina);
                GuardSkill.BlockType blockType = successParrying ? BlockType.ADVANCED_GUARD : (stamina >= 0.0F ? BlockType.GUARD : BlockType.GUARD_BREAK);
                StaticAnimation animation = this.getGuardMotion(event.getPlayerPatch(), itemCapability, blockType);
                if (animation != null) {
                    ((ServerPlayerPatch) event.getPlayerPatch()).playAnimationSynchronized(animation, 0.0F);
                }

                if (blockType == BlockType.GUARD_BREAK) {
                    ((ServerPlayerPatch) event.getPlayerPatch()).playSound(EpicFightSounds.NEUTRALIZE_MOBS, 3.0F, 0.0F, 0.1F);
                }

                this.dealEvent(event.getPlayerPatch(), event);
                return;
            }
        }

        super.guard(container, itemCapability, event, knockback, impact, false);
    }
}
