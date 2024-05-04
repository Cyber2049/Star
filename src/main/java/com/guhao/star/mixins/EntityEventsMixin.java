package com.guhao.star.mixins;


import com.guhao.star.regirster.Effect;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.events.EntityEvents;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.HurtableEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.projectile.ProjectilePatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.ExtraDamageInstance;
import yesman.epicfight.world.damagesource.SourceTags;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.effect.EpicFightMobEffects;
import yesman.epicfight.world.entity.eventlistener.DealtDamageEvent;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.Iterator;

@Mixin(value = EntityEvents.class,remap = false)
@Mod.EventBusSubscriber(modid= EpicFightMod.MODID)
public class EntityEventsMixin {
    @Inject(method = "hurtEvent",at = @At("HEAD"), cancellable = true)
    private static void hurtEvent(LivingHurtEvent event, CallbackInfo ci) {
        ci.cancel();
        EpicFightDamageSource epicFightDamageSource = null;
        Entity trueSource = event.getSource().getEntity();
        if (trueSource != null) {
            LivingEntityPatch<?> attackerEntityPatch = (LivingEntityPatch) EpicFightCapabilities.getEntityPatch(trueSource, LivingEntityPatch.class);
            float baseDamage = event.getAmount();
            DamageSource var6 = event.getSource();
            if (var6 instanceof EpicFightDamageSource) {
                EpicFightDamageSource instance = (EpicFightDamageSource)var6;
                epicFightDamageSource = instance;
            } else if (event.getSource() instanceof IndirectEntityDamageSource && event.getSource().getDirectEntity() != null) {
                ProjectilePatch<?> projectileCap = (ProjectilePatch)EpicFightCapabilities.getEntityPatch(event.getSource().getDirectEntity(), ProjectilePatch.class);
                if (projectileCap != null) {
                    epicFightDamageSource = projectileCap.getEpicFightDamageSource(event.getSource());
                }
            } else if (attackerEntityPatch != null) {
                epicFightDamageSource = attackerEntityPatch.getEpicFightDamageSource();
                baseDamage = attackerEntityPatch.getModifiedBaseDamage(baseDamage);
            }

            if (epicFightDamageSource != null) {
                LivingEntity hitEntity = event.getEntityLiving();
                if (attackerEntityPatch instanceof ServerPlayerPatch) {
                    ServerPlayerPatch playerpatch = (ServerPlayerPatch)attackerEntityPatch;
                    DealtDamageEvent dealDamagePre = new DealtDamageEvent(playerpatch, hitEntity, (EpicFightDamageSource)epicFightDamageSource, baseDamage);
                    playerpatch.getEventListener().triggerEvents(PlayerEventListener.EventType.DEALT_DAMAGE_EVENT_PRE, dealDamagePre);
                }

                float totalDamage = ((EpicFightDamageSource)epicFightDamageSource).getDamageModifier().getTotalValue(baseDamage);
                if (trueSource instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity)trueSource;
                    ExtraDamageInstance extraDamage;
                    if (((EpicFightDamageSource)epicFightDamageSource).getExtraDamages() != null) {
                        for(Iterator var8 = ((EpicFightDamageSource)epicFightDamageSource).getExtraDamages().iterator(); var8.hasNext(); totalDamage += extraDamage.get(livingEntity, ((EpicFightDamageSource)epicFightDamageSource).getHurtItem(), hitEntity, baseDamage)) {
                            extraDamage = (ExtraDamageInstance)var8.next();
                        }
                    }
                }

                HurtableEntityPatch<?> hitHurtableEntityPatch = (HurtableEntityPatch)EpicFightCapabilities.getEntityPatch(hitEntity, HurtableEntityPatch.class);
                LivingEntityPatch<?> hitLivingEntityPatch = (LivingEntityPatch)EpicFightCapabilities.getEntityPatch(hitEntity, LivingEntityPatch.class);
                ServerPlayerPatch hitPlayerPatch = (ServerPlayerPatch)EpicFightCapabilities.getEntityPatch(hitEntity, ServerPlayerPatch.class);
                if (hitPlayerPatch != null) {
                    HurtEvent.Post hurtEvent = new HurtEvent.Post(hitPlayerPatch, (EpicFightDamageSource)epicFightDamageSource, totalDamage);
                    hitPlayerPatch.getEventListener().triggerEvents(PlayerEventListener.EventType.HURT_EVENT_POST, hurtEvent);
                    totalDamage = hurtEvent.getAmount();
                }

                float trueDamage = totalDamage * ((EpicFightDamageSource)epicFightDamageSource).getArmorNegation() * 0.01F;
                if (((EpicFightDamageSource)epicFightDamageSource).hasTag(SourceTags.EXECUTION)) {
                    trueDamage = Float.MAX_VALUE;
                    if (hitLivingEntityPatch != null) {
                        int executionResistance = hitLivingEntityPatch.getExecutionResistance();
                        if (executionResistance > 0) {
                            hitLivingEntityPatch.setExecutionResistance(executionResistance - 1);
                            trueDamage = 0.0F;
                        }
                    }
                }

                float calculatedDamage = trueDamage;
                int k;
                float stunTime;
                float knockBackAmount;
                if (hitEntity.hasEffect(MobEffects.DAMAGE_RESISTANCE)) {
                    k = (hitEntity.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() + 1) * 5;
                    int j = 25 - k;
                    float f = calculatedDamage * (float)j;
                    stunTime = calculatedDamage;
                    calculatedDamage = Math.max(f / 25.0F, 0.0F);
                    knockBackAmount = stunTime - calculatedDamage;
                    if (knockBackAmount > 0.0F && knockBackAmount < 3.4028235E37F) {
                        if (hitEntity instanceof ServerPlayer) {
                            ServerPlayer serverPlayer = (ServerPlayer)hitEntity;
                            serverPlayer.awardStat(Stats.DAMAGE_RESISTED, Math.round(knockBackAmount * 10.0F));
                        } else {
                            Entity var19 = event.getSource().getEntity();
                            if (var19 instanceof ServerPlayer) {
                                ServerPlayer serverPlayer = (ServerPlayer)var19;
                                serverPlayer.awardStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(knockBackAmount * 10.0F));
                            }
                        }
                    }
                }
                if (calculatedDamage > 0.0F) {
                    k = EnchantmentHelper.getDamageProtection(hitEntity.getArmorSlots(), event.getSource());
                    if (k > 0) {
                        calculatedDamage = CombatRules.getDamageAfterMagicAbsorb(calculatedDamage, (float)k);
                    }
                }

                float absorpAmount = hitEntity.getAbsorptionAmount() - calculatedDamage;
                hitEntity.setAbsorptionAmount(Math.max(absorpAmount, 0.0F));
                float realHealthDamage = Math.max(-absorpAmount, 0.0F);
                if (realHealthDamage > 0.0F && realHealthDamage < 3.4028235E37F) {
                    Entity var35 = event.getSource().getEntity();
                    if (var35 instanceof ServerPlayer) {
                        ServerPlayer serverPlayer = (ServerPlayer)var35;
                        serverPlayer.awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(realHealthDamage * 10.0F));
                    }
                }

                if (absorpAmount < 0.0F) {
                    hitEntity.setHealth(hitEntity.getHealth() + absorpAmount);
                    if (attackerEntityPatch != null) {
                        if (!hitEntity.isAlive()) {
                            attackerEntityPatch.setLastAttackEntity(hitEntity);
                        }

                        attackerEntityPatch.gatherDamageDealt((EpicFightDamageSource)epicFightDamageSource, calculatedDamage);
                    }
                }

                event.setAmount(totalDamage - trueDamage);
                if ((event.getAmount() + trueDamage > 0.0F && hitHurtableEntityPatch != null)) {
                    StunType stunType = ((EpicFightDamageSource)epicFightDamageSource).getStunType();
                    stunTime = 0.0F;
                    knockBackAmount = 0.0F;
                    float weight = 40.0F / hitHurtableEntityPatch.getWeight();
                    float stunShield = hitHurtableEntityPatch.getStunShield();
                    if (stunShield > ((EpicFightDamageSource)epicFightDamageSource).getImpact() && (stunType == StunType.SHORT || stunType == StunType.LONG)) {
                        stunType = StunType.NONE;
                    }

                    hitHurtableEntityPatch.setStunShield(stunShield - ((EpicFightDamageSource)epicFightDamageSource).getImpact());
                    boolean flag;
                    switch (stunType) {
                        case FALL:
                            stunType = hitEntity.hasEffect((MobEffect)Effect.REALLY_STUN_IMMUNITY.get()) ? StunType.NONE : StunType.FALL;
                        case SHORT:
                            stunType = StunType.NONE;
                            if (((!hitEntity.hasEffect((MobEffect) Effect.REALLY_STUN_IMMUNITY.get())) || (!hitEntity.hasEffect((MobEffect) EpicFightMobEffects.STUN_IMMUNITY.get())) && hitHurtableEntityPatch.getStunShield() == 0.0F)) {
                                float totalStunTime = (0.25F + ((EpicFightDamageSource)epicFightDamageSource).getImpact() * 0.1F) * weight;
                                totalStunTime *= 1.0F - hitHurtableEntityPatch.getStunReduction();
                                if (totalStunTime >= 0.075F) {
                                    stunTime = totalStunTime - 0.1F;
                                    flag = totalStunTime >= 0.83F;
                                    stunTime = flag ? 0.83F : stunTime;
                                    stunType = flag ? StunType.LONG : StunType.SHORT;
                                    knockBackAmount = Math.min(flag ? ((EpicFightDamageSource)epicFightDamageSource).getImpact() * 0.05F : totalStunTime, 2.0F);
                                }

                                stunTime = (float)((double)stunTime * (1.0 - hitEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)));
                            }
                            break;
                        case LONG:
                            if (hitEntity.hasEffect((MobEffect)EpicFightMobEffects.STUN_IMMUNITY.get())||hitEntity.hasEffect((MobEffect)Effect.REALLY_STUN_IMMUNITY.get())){
                                stunType = StunType.NONE;
                            }
                            knockBackAmount = Math.min(((EpicFightDamageSource)epicFightDamageSource).getImpact() * 0.05F * weight, 5.0F);
                            stunTime = 0.83F;
                            break;
                        case HOLD:
                            stunType = hitEntity.hasEffect((MobEffect) Effect.REALLY_STUN_IMMUNITY.get()) ? StunType.NONE : StunType.SHORT;
                            stunTime = ((EpicFightDamageSource)epicFightDamageSource).getImpact() * 0.25F;
                            break;
                        case KNOCKDOWN:
                            if (hitEntity.hasEffect((MobEffect)EpicFightMobEffects.STUN_IMMUNITY.get())||hitEntity.hasEffect((MobEffect)Effect.REALLY_STUN_IMMUNITY.get())){
                                stunType = StunType.NONE;
                            }
                            knockBackAmount = Math.min(((EpicFightDamageSource)epicFightDamageSource).getImpact() * 0.05F, 5.0F);
                            stunTime = 2.0F;
                            break;
                        case NEUTRALIZE:
                            stunType = hitEntity.hasEffect((MobEffect)Effect.REALLY_STUN_IMMUNITY.get()) ? StunType.NONE : StunType.NEUTRALIZE;
                            hitHurtableEntityPatch.playSound(EpicFightSounds.NEUTRALIZE_MOBS, 3.0F, 0.0F, 0.1F);
                            ((HitParticleType) EpicFightParticles.AIR_BURST.get()).spawnParticleWithArgument((ServerLevel)hitEntity.level, hitEntity, event.getSource().getDirectEntity());
                            knockBackAmount = 0.0F;
                            stunTime = 2.0F;
                    }

                    Vec3 sourcePosition = ((EpicFightDamageSource)epicFightDamageSource).getInitialPosition();
                    hitHurtableEntityPatch.setStunReductionOnHit(stunType);
                    flag = hitHurtableEntityPatch.applyStun(stunType, stunTime);
                    if (sourcePosition != null) {
                        if (!(hitEntity instanceof Player) && flag) {
                            hitEntity.lookAt(EntityAnchorArgument.Anchor.FEET, sourcePosition);
                        }

                        if (knockBackAmount > 0.0F) {
                            hitHurtableEntityPatch.knockBackEntity(sourcePosition, knockBackAmount);
                        }
                    }
                }
            }
        }

    }
}
