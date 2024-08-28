package com.guhao.star.mixins;

import com.guhao.star.regirster.Effect;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import yesman.epicfight.events.EntityEvents;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
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

@Mixin(value = EntityEvents.class,remap = false,priority = 999999)
@Mod.EventBusSubscriber(modid= "epicfight")
public class EntityEventsMixin {
    public EntityEventsMixin() {
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    @SubscribeEvent
    public static void hurtEvent(LivingHurtEvent event) {
        EpicFightDamageSource epicFightDamageSource = null;
        Entity trueSource = event.getSource().getEntity();

        if (trueSource != null) {
            LivingEntityPatch<?> attackerEntityPatch = EpicFightCapabilities.getEntityPatch(trueSource, LivingEntityPatch.class);
            float baseDamage = event.getAmount();

            if (event.getSource() instanceof EpicFightDamageSource instance) {
                epicFightDamageSource = instance;
            } else if (event.getSource() instanceof IndirectEntityDamageSource && event.getSource().getDirectEntity() != null) {
                ProjectilePatch<?> projectileCap = EpicFightCapabilities.getEntityPatch(event.getSource().getDirectEntity(), ProjectilePatch.class);

                if (projectileCap != null) {
                    epicFightDamageSource = projectileCap.getEpicFightDamageSource(event.getSource());
                }
            } else if (attackerEntityPatch != null) {
                epicFightDamageSource = attackerEntityPatch.getEpicFightDamageSource();
                baseDamage = attackerEntityPatch.getModifiedBaseDamage(baseDamage);
            }

            if (epicFightDamageSource != null) {
                LivingEntity hitEntity = event.getEntityLiving();

                if (attackerEntityPatch instanceof ServerPlayerPatch playerpatch) {
                    DealtDamageEvent dealDamagePre = new DealtDamageEvent(playerpatch, hitEntity, epicFightDamageSource, baseDamage);
                    playerpatch.getEventListener().triggerEvents(PlayerEventListener.EventType.DEALT_DAMAGE_EVENT_PRE, dealDamagePre);
                }

                float totalDamage = epicFightDamageSource.getDamageModifier().getTotalValue(baseDamage);

                if (trueSource instanceof LivingEntity livingEntity && epicFightDamageSource.getExtraDamages() != null) {
                    for (ExtraDamageInstance extraDamage : epicFightDamageSource.getExtraDamages()) {
                        totalDamage += extraDamage.get(livingEntity, epicFightDamageSource.getHurtItem(), hitEntity, baseDamage);
                    }
                }

                HurtableEntityPatch<?> hitHurtableEntityPatch = EpicFightCapabilities.getEntityPatch(hitEntity, HurtableEntityPatch.class);
                LivingEntityPatch<?> hitLivingEntityPatch = EpicFightCapabilities.getEntityPatch(hitEntity, LivingEntityPatch.class);
                ServerPlayerPatch hitPlayerPatch = EpicFightCapabilities.getEntityPatch(hitEntity, ServerPlayerPatch.class);

                if (hitPlayerPatch != null) {
                    HurtEvent.Post hurtEvent = new HurtEvent.Post(hitPlayerPatch, epicFightDamageSource, totalDamage);
                    hitPlayerPatch.getEventListener().triggerEvents(PlayerEventListener.EventType.HURT_EVENT_POST, hurtEvent);
                    totalDamage = hurtEvent.getAmount();
                }

                float trueDamage = totalDamage * epicFightDamageSource.getArmorNegation() * 0.01F;

                if (epicFightDamageSource.hasTag(SourceTags.EXECUTION)) {

                    if (hitLivingEntityPatch != null) {
                        int executionResistance = hitLivingEntityPatch.getExecutionResistance();

                        if (executionResistance > 0) {
                            hitLivingEntityPatch.setExecutionResistance(executionResistance - 1);
                            trueDamage = 0;
                        }
                    }
                }

                float calculatedDamage = trueDamage;

                if (hitEntity.hasEffect(MobEffects.DAMAGE_RESISTANCE)) {
                    int i = (hitEntity.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() + 1) * 5;
                    int j = 25 - i;
                    float f = calculatedDamage * (float)j;
                    float f1 = calculatedDamage;
                    calculatedDamage = Math.max(f / 25.0F, 0.0F);
                    float f2 = f1 - calculatedDamage;

                    if (f2 > 0.0F && f2 < 3.4028235E37F) {
                        if (hitEntity instanceof ServerPlayer serverPlayer) {
                            serverPlayer.awardStat(Stats.DAMAGE_RESISTED, Math.round(f2 * 10.0F));
                        } else if (event.getSource().getEntity() instanceof ServerPlayer serverPlayer) {
                            serverPlayer.awardStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(f2 * 10.0F));
                        }
                    }
                }

                if (calculatedDamage > 0.0F) {
                    int k = EnchantmentHelper.getDamageProtection(hitEntity.getArmorSlots(), event.getSource());

                    if (k > 0) {
                        calculatedDamage = CombatRules.getDamageAfterMagicAbsorb(calculatedDamage, (float)k);
                    }
                }

                float absorpAmount = hitEntity.getAbsorptionAmount() - calculatedDamage;
                hitEntity.setAbsorptionAmount(Math.max(absorpAmount, 0.0F));
                float realHealthDamage = Math.max(-absorpAmount, 0.0F);

                if (realHealthDamage > 0.0F && realHealthDamage < 3.4028235E37F && event.getSource().getEntity() instanceof ServerPlayer serverPlayer) {
                    serverPlayer.awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(realHealthDamage * 10.0F));
                }

                if (absorpAmount < 0.0F) {
                    hitEntity.setHealth(hitEntity.getHealth() + absorpAmount);

                    if (attackerEntityPatch != null) {
                        if (!hitEntity.isAlive()) {
                            attackerEntityPatch.setLastAttackEntity(hitEntity);
                        }

                        attackerEntityPatch.gatherDamageDealt(epicFightDamageSource, calculatedDamage);
                    }
                }

                event.setAmount(totalDamage - trueDamage);

                if (event.getAmount() + trueDamage > 0.0F) {
                    if (hitHurtableEntityPatch != null) {
                        StunType stunType = epicFightDamageSource.getStunType();
                        float stunTime = 0.0F;
                        float knockBackAmount = 0.0F;
                        float weight = 40.0F / hitHurtableEntityPatch.getWeight();
                        float stunShield = hitHurtableEntityPatch.getStunShield();

                        if (stunShield > epicFightDamageSource.getImpact()) {
                            if (stunType == StunType.SHORT || stunType == StunType.LONG) {
                                stunType = StunType.NONE;
                            }
                        }

                        hitHurtableEntityPatch.setStunShield(stunShield - epicFightDamageSource.getImpact());

                        switch (stunType) {
                            case SHORT:
                                stunType = StunType.NONE;
                                if (!hitEntity.hasEffect(EpicFightMobEffects.STUN_IMMUNITY.get()) && (hitHurtableEntityPatch.getStunShield() == 0.0F)) {
                                    float totalStunTime = (0.25F + (epicFightDamageSource.getImpact()) * 0.1F) * weight;
                                    totalStunTime *= (1.0F - hitHurtableEntityPatch.getStunReduction());

                                    if (totalStunTime >= 0.075F) {
                                        stunTime = totalStunTime - 0.1F;
                                        boolean flag = totalStunTime >= 0.83F;
                                        stunTime = flag ? 0.83F : stunTime;
                                        stunType = flag ? StunType.LONG : StunType.SHORT;
                                        knockBackAmount = Math.min(flag ? epicFightDamageSource.getImpact() * 0.05F : totalStunTime, 2.0F);
                                    }

                                    stunTime *= 1.0F - hitEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
                                }
                                break;
                            case LONG:
                                stunType = hitEntity.hasEffect(EpicFightMobEffects.STUN_IMMUNITY.get()) ? StunType.NONE : StunType.LONG;
                                knockBackAmount = Math.min(epicFightDamageSource.getImpact() * 0.05F * weight, 5.0F);
                                stunTime = 0.83F;
                                break;
                            case HOLD:
                                stunType = StunType.SHORT;
                                stunTime = epicFightDamageSource.getImpact() * 0.25F;
                                if (hitEntity.hasEffect(Effect.REALLY_STUN_IMMUNITY.get())) {
                                    if (hitLivingEntityPatch != null) {
                                        stunType = StunType.NONE;
                                        stunTime = 0.0f;
                                        hitLivingEntityPatch.applyStun(StunType.NONE, 0.0f);
                                    }
                                }
                                break;
                            case KNOCKDOWN:
                                stunType = hitEntity.hasEffect(EpicFightMobEffects.STUN_IMMUNITY.get()) ? StunType.NONE : StunType.KNOCKDOWN;
                                knockBackAmount = Math.min(epicFightDamageSource.getImpact() * 0.05F, 5.0F);
                                stunTime = 2.0F;
                                break;
                            case FALL:
                                if (hitEntity.hasEffect(Effect.REALLY_STUN_IMMUNITY.get())) {
                                    if (hitLivingEntityPatch != null) {
                                        hitLivingEntityPatch.applyStun(StunType.NONE, 0.0f);
                                    }
                                }
                            case NEUTRALIZE:
                                stunType = StunType.NEUTRALIZE;
                                hitHurtableEntityPatch.playSound(EpicFightSounds.NEUTRALIZE_MOBS, 3.0F, 0.0F, 0.1F);
                                EpicFightParticles.AIR_BURST.get().spawnParticleWithArgument(((ServerLevel)hitEntity.level), hitEntity, event.getSource().getDirectEntity());
                                knockBackAmount = 0.0F;
                                stunTime = 2.0F;
                            default:
                                break;
                        }

                        Vec3 sourcePosition = epicFightDamageSource.getInitialPosition();
                        hitHurtableEntityPatch.setStunReductionOnHit(stunType);
                        boolean stunApplied = hitHurtableEntityPatch.applyStun(stunType, stunTime);

                        if (sourcePosition != null) {
                            if (!(hitEntity instanceof Player) && stunApplied) {
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
}