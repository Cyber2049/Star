package com.guhao.star.mixins;

import com.guhao.init.Items;
import com.guhao.star.api.StarAPI;
import com.guhao.star.efmex.StarWeaponCategory;
import com.guhao.star.regirster.Effect;
import com.guhao.star.regirster.Sounds;
import com.guhao.star.units.Guard_Array;
import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import com.nameless.toybox.common.attribute.ai.ToyBoxAttributes;
import com.nameless.toybox.config.CommonConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.skill.guard.ParryingSkill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.Arrays;

import static yesman.epicfight.skill.guard.ParryingSkill.PARRY_MOTION_COUNTER;

@Mixin(value = ParryingSkill.class, remap = false, priority = 999999)
public class ActiveGuardMixin extends GuardSkill {
    public ActiveGuardMixin(Builder builder) {
        super(builder);
    }
    private HurtEvent.Pre event;
    @Unique
    Guard_Array star_new$GuardArray = new Guard_Array();
    @Unique
    StaticAnimation[] star_new$GUARD = star_new$GuardArray.getGuard();
    @Unique
    StaticAnimation[] star_new$PARRY = star_new$GuardArray.getParry();

    @Mutable
    @Final
    @Shadow
    private static final SkillDataManager.SkillDataKey<Integer> LAST_ACTIVE;

    static {
        LAST_ACTIVE = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
    }

    @Unique
    public void star_new$Sta(DamageSource entity) {
        StarAPI starAPI = new StarAPI();
        if (entity.getDirectEntity() instanceof LivingEntity entity1) {
            LazyOptional<EntityPatch> optional = entity1.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY);
            optional.ifPresent(patch -> {
                if (patch instanceof AdvancedCustomHumanoidMobPatch<?> Patch1) {
                    Patch1.setStamina(Patch1.getStamina() - starAPI.getStamina(entity1));
                }
            });
        }
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public static GuardSkill.Builder createActiveGuardBuilder() {
        return GuardSkill.createGuardBuilder().addAdvancedGuardMotion(CapabilityItem.WeaponCategories.SWORD, (itemCap, playerpatch) -> itemCap.getStyle(playerpatch) == CapabilityItem.Styles.ONE_HAND ? new StaticAnimation[]{Animations.SWORD_GUARD_ACTIVE_HIT1, Animations.SWORD_GUARD_ACTIVE_HIT2} : new StaticAnimation[]{Animations.SWORD_GUARD_ACTIVE_HIT2, Animations.SWORD_GUARD_ACTIVE_HIT3})
                .addAdvancedGuardMotion(CapabilityItem.WeaponCategories.LONGSWORD, (itemCap, playerpatch) -> new StaticAnimation[]{Animations.LONGSWORD_GUARD_ACTIVE_HIT1, Animations.LONGSWORD_GUARD_ACTIVE_HIT2})
                .addAdvancedGuardMotion(CapabilityItem.WeaponCategories.UCHIGATANA, (itemCap, playerpatch) -> new StaticAnimation[]{Animations.SWORD_GUARD_ACTIVE_HIT1, Animations.SWORD_GUARD_ACTIVE_HIT2})
                .addAdvancedGuardMotion(CapabilityItem.WeaponCategories.TACHI, (itemCap, playerpatch) -> new StaticAnimation[]{Animations.LONGSWORD_GUARD_ACTIVE_HIT1, Animations.LONGSWORD_GUARD_ACTIVE_HIT2})
                .addAdvancedGuardMotion(CapabilityItem.WeaponCategories.SPEAR, (itemCap, playerpatch) -> new StaticAnimation[]{Animations.LONGSWORD_GUARD_ACTIVE_HIT1, Animations.LONGSWORD_GUARD_ACTIVE_HIT2})
                .addAdvancedGuardMotion(StarWeaponCategory.DRAGONSLAYER, (itemCap, playerpatch) -> new StaticAnimation[]{Animations.LONGSWORD_GUARD_ACTIVE_HIT1, Animations.LONGSWORD_GUARD_ACTIVE_HIT2});
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getDataManager().registerData(LAST_ACTIVE);
        container.getDataManager().registerData(PARRY_MOTION_COUNTER);
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID, (event) -> {
            CapabilityItem itemCapability = ((ServerPlayerPatch)event.getPlayerPatch()).getHoldingItemCapability(InteractionHand.MAIN_HAND);
            if (this.isHoldingWeaponAvailable(event.getPlayerPatch(), itemCapability, BlockType.GUARD) && this.isExecutableState(event.getPlayerPatch())) {
                ((ServerPlayer)((ServerPlayerPatch)event.getPlayerPatch()).getOriginal()).startUsingItem(InteractionHand.MAIN_HAND);
            }

            int lastActive = (Integer)container.getDataManager().getDataValue(LAST_ACTIVE);
            if ((float)(((ServerPlayer)((ServerPlayerPatch)event.getPlayerPatch()).getOriginal()).tickCount - lastActive) > 0.099999994F) {
                container.getDataManager().setData(LAST_ACTIVE, ((ServerPlayer)((ServerPlayerPatch)event.getPlayerPatch()).getOriginal()).tickCount);
            }

        });
    }
    @Inject(at = @At("HEAD"), method = "guard", cancellable = true)
    public void guard(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced, CallbackInfo ci) {
        ci.cancel();
        ///////////////////////////////////////////////////
        EpicFightDamageSource EFDamageSource = star_new$GuardArray.getEpicFightDamageSources(event.getDamageSource());
        if ((EFDamageSource != null)) {
            StaticAnimation an = EFDamageSource.getAnimation();
            if (!(Arrays.asList(star_new$GUARD).contains(an))) {
                if (this.isHoldingWeaponAvailable(event.getPlayerPatch(), itemCapability, BlockType.ADVANCED_GUARD)) {
                    DamageSource damageSource = event.getDamageSource();
                    if (this.isBlockableSource(damageSource, true)) {
                        ServerPlayer playerentity = event.getPlayerPatch().getOriginal();
                        boolean successParrying = (playerentity.tickCount - container.getDataManager().getDataValue(LAST_ACTIVE) < 10);
                        if (event.getPlayerPatch().getOriginal().getMainHandItem().getItem() == Items.GUHAO.get()) {
                            successParrying = true;
                        }
                        float penalty = container.getDataManager().getDataValue(PENALTY);
                        if (successParrying) {
                            event.getPlayerPatch().playSound(Sounds.BIGBONG, -0.21F, -0.12F);
                        } else {
                            event.getPlayerPatch().playSound(Sounds.BONG, -0.21F, -0.12F);
                        }
                        EpicFightParticles.HIT_BLUNT.get().spawnParticleWithArgument((ServerLevel) playerentity.level, HitParticleType.FRONT_OF_EYES, HitParticleType.ZERO, playerentity, damageSource.getDirectEntity());
                        if (successParrying) {
                            if (!(event.getPlayerPatch().getOriginal().getMainHandItem().getItem() == Items.GUHAO.get())) {
                                event.setParried(true);
                                penalty = 0.1F;
                                knockback *= 0.4F;
                                container.getDataManager().setData(LAST_ACTIVE, 0);
                                star_new$Sta(event.getDamageSource());
                                if (event.getDamageSource().getDirectEntity() instanceof Monster) {
                                    Entity L = event.getDamageSource().getDirectEntity();
                                    if (event.getPlayerPatch().getOriginal() != null && (L instanceof LivingEntity E) && (E.hasEffect(Effect.DEFENSE.get()))) {
                                        E.removeEffect(Effect.DEFENSE.get());
                                        LazyOptional<EntityPatch> optional = L.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY);
                                        optional.ifPresent(patch -> {
                                            if (patch instanceof LivingEntityPatch<?> livingEntityPatch) {
                                                livingEntityPatch.playAnimationSynchronized(Animations.BIPED_COMMON_NEUTRALIZED, 0.0F);
                                                L.playSound(EpicFightSounds.NEUTRALIZE_BOSSES, 3.0F, 1.2F);
                                            }
                                        });
                                    }
                                }
                            } else {
                                event.setParried(true);
                                if (event.getPlayerPatch().getOriginal().hasEffect(com.guhao.init.Effect.GUHAO.get())) {
                                    Entity E = event.getDamageSource().getDirectEntity();
                                    E.hurt(DamageSource.playerAttack(event.getPlayerPatch().getOriginal().getLevel().getNearestPlayer(event.getPlayerPatch().getOriginal(), -1)).setMagic().bypassInvul().bypassMagic().bypassArmor().damageHelmet(), (event.getAmount()*0.25f));
                                    EpicFightParticles.EVISCERATE.get().spawnParticleWithArgument((ServerLevel) playerentity.level, HitParticleType.FRONT_OF_EYES, HitParticleType.ZERO, playerentity, damageSource.getDirectEntity());
                                }
                                penalty = 0.0F;
                                knockback *= 0.0F;
                                container.getDataManager().setData(LAST_ACTIVE, 0);
                                star_new$Sta(event.getDamageSource());
                                if (event.getDamageSource().getDirectEntity() instanceof Monster) {
                                    Entity L = event.getDamageSource().getDirectEntity();
                                    if (event.getPlayerPatch().getOriginal() != null && (L instanceof LivingEntity E) && (E.hasEffect(Effect.DEFENSE.get()))) {
                                            E.removeEffect(Effect.DEFENSE.get());
                                            LazyOptional<EntityPatch> optional = L.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY);
                                            optional.ifPresent(patch -> {
                                                if (patch instanceof LivingEntityPatch<?> livingEntityPatch) {
                                                    livingEntityPatch.playAnimationSynchronized(Animations.BIPED_COMMON_NEUTRALIZED, 0.0F);
                                                    L.playSound(EpicFightSounds.NEUTRALIZE_BOSSES, 3.0F, 1.2F);
                                                }
                                            });
                                    }
                                }
                            }
                        } else {
                            if (!(Arrays.asList(star_new$PARRY).contains(an))) {
                                penalty += this.getPenalizer(itemCapability);
                                container.getDataManager().setDataSync(PENALTY, penalty, playerentity);
                            } else {
                                event.setParried(false);
                                event.setResult(AttackResult.ResultType.SUCCESS);
                                return;
                            }
                        }

                        Entity var12 = damageSource.getDirectEntity();
                        if (var12 instanceof LivingEntity) {
                            LivingEntity livingentity = (LivingEntity) var12;
                            knockback += (float) EnchantmentHelper.getKnockbackBonus(livingentity) * 0.1F;
                        }

                        event.getPlayerPatch().knockBackEntity(damageSource.getDirectEntity().position(), knockback);
                        float consumeAmount = penalty * impact;
                        event.getPlayerPatch().consumeStaminaAlways(consumeAmount);
                        GuardSkill.BlockType blockType = successParrying ? BlockType.ADVANCED_GUARD : (event.getPlayerPatch().hasStamina(0.0F) ? BlockType.GUARD : BlockType.GUARD_BREAK);
                        StaticAnimation animation = this.getGuardMotion(event.getPlayerPatch(), itemCapability, blockType);
                        if (animation != null) {
                            event.getPlayerPatch().playAnimationSynchronized(animation, 0.0F);
                        }

                        if (blockType == BlockType.GUARD_BREAK) {
                            event.getPlayerPatch().playSound(EpicFightSounds.NEUTRALIZE_MOBS, 3.0F, 0.0F, 0.1F);
                        }
                        this.dealEvent(event.getPlayerPatch(), event, advanced);

                    }
                }
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        else {
            if (this.isHoldingWeaponAvailable(event.getPlayerPatch(), itemCapability, BlockType.ADVANCED_GUARD)) {
                DamageSource damageSource = event.getDamageSource();
                if (this.isBlockableSource(damageSource, true)) {

                    ServerPlayer playerentity = event.getPlayerPatch().getOriginal();
                    boolean successParrying = playerentity.tickCount - container.getDataManager().getDataValue(LAST_ACTIVE) < 10;
                    if (event.getPlayerPatch().getOriginal().getMainHandItem().getItem() == Items.GUHAO.get()) {
                        successParrying = true;
                    }
                    float penalty = container.getDataManager().getDataValue(PENALTY);
                    if (successParrying) {
                        event.getPlayerPatch().playSound(Sounds.BIGBONG, -0.06F, 0.12F);
                    } else {
                        event.getPlayerPatch().playSound(Sounds.BONG, -0.06F, 0.12F);
                    }
                    EpicFightParticles.HIT_BLUNT.get().spawnParticleWithArgument((ServerLevel) playerentity.level, HitParticleType.FRONT_OF_EYES, HitParticleType.ZERO, playerentity, damageSource.getDirectEntity());
                    if (successParrying) {
                        if (!(event.getPlayerPatch().getOriginal().getMainHandItem().getItem() == Items.GUHAO.get())) {
                            event.setParried(true);

                            penalty = 0.1F;
                            knockback *= 0.4F;
                            container.getDataManager().setData(LAST_ACTIVE, 0);
                            star_new$Sta(event.getDamageSource());
                            if (event.getDamageSource().getDirectEntity() instanceof Monster) {
                                Entity L = event.getDamageSource().getDirectEntity();
                                if (event.getPlayerPatch().getOriginal() != null && (L instanceof LivingEntity E) && (E.hasEffect(Effect.DEFENSE.get()))) {
                                    E.removeEffect(Effect.DEFENSE.get());
                                    LazyOptional<EntityPatch> optional = L.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY);
                                    optional.ifPresent(patch -> {
                                        if (patch instanceof LivingEntityPatch<?> livingEntityPatch) {
                                            ClientAnimator Animator = new ClientAnimator(livingEntityPatch);
                                            livingEntityPatch.playAnimationSynchronized(Animations.BIPED_COMMON_NEUTRALIZED, 0.0F);
                                            Animator.getOwner().getImpact(E.getUsedItemHand());
                                            L.playSound(EpicFightSounds.NEUTRALIZE_BOSSES, 3.0F, 1.2F);
                                        }
                                    });
                                }
                            }
                        }
                        else {
                            event.setParried(true);
                            if (event.getPlayerPatch().getOriginal().hasEffect(com.guhao.init.Effect.GUHAO.get())) {
                                Entity E = event.getDamageSource().getDirectEntity();
                                E.hurt(DamageSource.playerAttack(event.getPlayerPatch().getOriginal().getLevel().getNearestPlayer(event.getPlayerPatch().getOriginal(), -1)), (event.getAmount()*0.25f));
                                EpicFightParticles.EVISCERATE.get().spawnParticleWithArgument((ServerLevel) playerentity.level, HitParticleType.FRONT_OF_EYES, HitParticleType.ZERO, playerentity, damageSource.getDirectEntity());
                            }
                            penalty = 0.0F;
                            knockback *= 0.0F;
                            container.getDataManager().setData(LAST_ACTIVE, 0);
                            star_new$Sta(event.getDamageSource());
                            if (event.getDamageSource().getDirectEntity() instanceof Monster) {
                                Entity L = event.getDamageSource().getDirectEntity();
                                if (event.getPlayerPatch().getOriginal() != null && (L instanceof LivingEntity E) && (E.hasEffect(Effect.DEFENSE.get()))) {
                                    E.removeEffect(Effect.DEFENSE.get());
                                    LazyOptional<EntityPatch> optional = L.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY);
                                    optional.ifPresent(patch -> {
                                        if (patch instanceof LivingEntityPatch<?> livingEntityPatch) {
                                            ClientAnimator Animator = new ClientAnimator(livingEntityPatch);
                                            livingEntityPatch.playAnimationSynchronized(Animations.BIPED_COMMON_NEUTRALIZED, 0.0F);
                                            Animator.getOwner().getImpact(E.getUsedItemHand());
                                            L.playSound(EpicFightSounds.NEUTRALIZE_BOSSES, 3.0F, 1.2F);
                                        }
                                    });
                                }
                            }
                        }
                    } else {
                        penalty += this.getPenalizer(itemCapability);
                        container.getDataManager().setDataSync(PENALTY, penalty, playerentity);
                    }

                    Entity var12 = damageSource.getDirectEntity();
                    if (var12 instanceof LivingEntity) {
                        LivingEntity livingentity = (LivingEntity) var12;
                        knockback += (float) EnchantmentHelper.getKnockbackBonus(livingentity) * 0.1F;
                    }

                    event.getPlayerPatch().knockBackEntity(damageSource.getDirectEntity().position(), knockback);
                    float consumeAmount = penalty * impact;
                    event.getPlayerPatch().consumeStaminaAlways(consumeAmount);
                    GuardSkill.BlockType blockType = successParrying ? BlockType.ADVANCED_GUARD : (event.getPlayerPatch().hasStamina(0.0F) ? BlockType.GUARD : BlockType.GUARD_BREAK);
                    StaticAnimation animation = this.getGuardMotion(event.getPlayerPatch(), itemCapability, blockType);
                    if (animation != null) {
                        event.getPlayerPatch().playAnimationSynchronized(animation, 0.0F);
                    }

                    if (blockType == BlockType.GUARD_BREAK) {
                        event.getPlayerPatch().playSound(EpicFightSounds.NEUTRALIZE_MOBS, 3.0F, 0.0F, 0.1F);
                    }

                    this.dealEvent(event.getPlayerPatch(), event, advanced);
                }
            }
        }
    }

    @Inject(
            method = {"guard(Lyesman/epicfight/skill/SkillContainer;Lyesman/epicfight/world/capabilities/item/CapabilityItem;Lyesman/epicfight/world/entity/eventlistener/HurtEvent$Pre;FFZ)V"},
            at = {@At("HEAD")},
            remap = false
    )
    private void getSuccessParry(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced, CallbackInfo ci) {
        this.event = event;
    }

    @ModifyVariable(
            method = {"guard(Lyesman/epicfight/skill/SkillContainer;Lyesman/epicfight/world/capabilities/item/CapabilityItem;Lyesman/epicfight/world/entity/eventlistener/HurtEvent$Pre;FFZ)V"},
            at = @At("HEAD"),
            ordinal = 1,
            remap = false
    )
    private float setImpact(float impact) {
        if (ModList.get().isLoaded("epicparagliders") && CommonConfig.EPIC_PARAGLIDER_COMPAT.get()) {
            return impact;
        } else {
            float blockrate = 1.0F - Math.min((float) this.event.getPlayerPatch().getOriginal().getAttributeValue(ToyBoxAttributes.BLOCK_RATE.get()) / 100.0F, 0.9F);
            Object var4 = this.event.getDamageSource();
            if (var4 instanceof EpicFightDamageSource) {
                EpicFightDamageSource epicdamagesource = (EpicFightDamageSource)var4;
                float k = epicdamagesource.getImpact();
                return this.event.getAmount() / 4.0F * (1.0F + k / 2.0F) * blockrate;
            } else {
                return this.event.getAmount() / 3.0F * blockrate;
            }
        }
    }
}

