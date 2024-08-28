package com.guhao.star.mixins;

import com.guhao.star.regirster.Effect;
import com.guhao.star.regirster.Sounds;
import com.mojang.datafixers.util.Pair;
import com.nameless.indestructible.api.animation.types.CustomGuardAnimation;
import com.nameless.indestructible.data.AdvancedMobpatchReloader;
import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.world.capabilities.entitypatch.Faction;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.SourceTags;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.effect.EpicFightMobEffects;

import java.util.Iterator;
import java.util.Random;

@Mixin(value = AdvancedCustomHumanoidMobPatch.class,remap = false)
public class AdvancedCustomHumanoidMobPatchMixin<T extends PathfinderMob> extends HumanoidMobPatch<T> {
    public AdvancedCustomHumanoidMobPatchMixin(Faction faction, AdvancedMobpatchReloader.AdvancedCustomHumanoidMobPatchProvider provider) {
        super(faction);
        this.provider = provider;

    }
    @Mutable
    @Final
    @Shadow
    private final AdvancedMobpatchReloader.AdvancedCustomHumanoidMobPatchProvider provider;
    @Shadow
    private boolean cancel_block;
    @Shadow
    private int maxParryTimes;
    @Shadow
    private boolean isParry;
    @Shadow
    private int parryCounter = 0;
    @Shadow
    private int parryTimes = 0;
    @Shadow
    private int stun_immunity_time;
    @Shadow
    private boolean isRunning = false;
    @Shadow
    private float lastGetImpact;

    @Override
    public void initAnimator(ClientAnimator clientAnimator) {
        Iterator var2 = this.provider.getDefaultAnimations().iterator();

        while(var2.hasNext()) {
            Pair<LivingMotion, StaticAnimation> pair = (Pair)var2.next();
            clientAnimator.addLivingAnimation(pair.getFirst(), pair.getSecond());
        }

        clientAnimator.setCurrentMotionsAsDefault();
    }

    @Override
    public void updateMotion(boolean considerInaction) {
        if (((PathfinderMob)this.original).getHealth() <= 0.0F) {
            this.currentLivingMotion = LivingMotions.DEATH;
        } else if (this.state.inaction() && considerInaction) {
            this.currentLivingMotion = LivingMotions.IDLE;
        } else if (((PathfinderMob)this.original).getVehicle() != null) {
            this.currentLivingMotion = LivingMotions.MOUNT;
        } else if (((PathfinderMob)this.original).getDeltaMovement().y < -0.550000011920929) {
            this.currentLivingMotion = LivingMotions.FALL;
        } else if (((PathfinderMob)this.original).animationSpeed > 0.01F) {
            if (this.isRunning) {
                this.currentLivingMotion = LivingMotions.CHASE;
            } else {
                this.currentLivingMotion = LivingMotions.WALK;
            }
        } else {
            this.currentLivingMotion = LivingMotions.IDLE;
        }

        this.currentCompositeMotion = this.currentLivingMotion;
        if (((PathfinderMob)this.original).isUsingItem()) {
            CapabilityItem activeItem = this.getHoldingItemCapability(((PathfinderMob)this.original).getUsedItemHand());
            UseAnim useAnim = ((PathfinderMob)this.original).getItemInHand(((PathfinderMob)this.original).getUsedItemHand()).getUseAnimation();
            UseAnim secondUseAnim = activeItem.getUseAnimation(this);
            if (useAnim != UseAnim.BLOCK && secondUseAnim != UseAnim.BLOCK) {
                if (useAnim != UseAnim.BOW && useAnim != UseAnim.SPEAR) {
                    if (useAnim == UseAnim.CROSSBOW) {
                        this.currentCompositeMotion = LivingMotions.RELOAD;
                    } else {
                        this.currentCompositeMotion = this.currentLivingMotion;
                    }
                } else {
                    this.currentCompositeMotion = LivingMotions.AIM;
                }
            } else if (activeItem.getWeaponCategory() == CapabilityItem.WeaponCategories.SHIELD) {
                this.currentCompositeMotion = LivingMotions.BLOCK_SHIELD;
            } else {
                this.currentCompositeMotion = LivingMotions.BLOCK;
            }
        } else {
            if (this.isBlocking()) {
                this.currentCompositeMotion = LivingMotions.BLOCK;
            } else if (CrossbowItem.isCharged(((PathfinderMob)this.original).getMainHandItem())) {
                this.currentCompositeMotion = LivingMotions.AIM;
            } else if (this.getClientAnimator().getCompositeLayer(Layer.Priority.MIDDLE).animationPlayer.getAnimation().isReboundAnimation()) {
                this.currentCompositeMotion = LivingMotions.NONE;
            } else if (((PathfinderMob)this.original).swinging && ((PathfinderMob)this.original).getSleepingPos().isEmpty()) {
                this.currentCompositeMotion = LivingMotions.DIGGING;
            } else {
                this.currentCompositeMotion = this.currentLivingMotion;
            }

            if (this.getClientAnimator().isAiming() && this.currentCompositeMotion != LivingMotions.AIM) {
                this.playReboundAnimation();
            }
        }

    }
    @Shadow
    public boolean canBlockProjectile() {
        return true;
    }

    @Shadow
    public boolean isBlocking() {
        return false;
    }
    @Shadow
    public CustomGuardAnimation getGuardAnimation() {
        return null;
    }
    @Shadow
    public float getParryCostMultiply() {
        return 0;
    }
    @Shadow
    public StaticAnimation getParryAnimation() {

        return null;
    }
    @Shadow
    public float getMaxStamina() {
        return 0f;
    }
    @Shadow
    public float getStamina() {

        return 0;
    }
    @Shadow
    public void setStamina(float value) {
    }
    @Shadow
    public void setAttackSpeed(float value) {
    }
    @Shadow
    public float getGuardCostMultiply() {
        return 0f;
    }
    @Shadow
    public void setBlocking(boolean blocking) {
    }
    @Shadow
    public StaticAnimation getCounter() {
        return null;
    }
    @Shadow
    public float getCounterChance() {
        return 0.0f;
    }
    @Shadow
    public float getCounterStamina() {
        return 0.0f;
    }
    @Shadow
    public float getCounterSpeed() {
        return 0.0f;
    }
    @Inject(method = "setAIAsInfantry",at = @At("HEAD"), cancellable = true)
    public void setAIAsInfantry(boolean holdingRanedWeapon, CallbackInfo ci) {
        if (this.getOriginal().hasEffect(Effect.EXECUTED.get())) ci.cancel();
    }
    /**
     * @author
     * @reason
     */
    @Overwrite

    private AttackResult tryProcess(DamageSource damageSource, float amount) {
        if (this.isBlocking()) {
            CustomGuardAnimation animation = this.getGuardAnimation();
            StaticAnimation success = animation.successAnimation != null ? EpicFightMod.getInstance().animationManager.findAnimationByPath(animation.successAnimation) : Animations.SWORD_GUARD_HIT;
            boolean isFront = false;
            boolean canBlockSource = !damageSource.isExplosion() && !damageSource.isMagic() && !damageSource.isBypassInvul() && (!damageSource.isProjectile() || this.canBlockProjectile());
            Vec3 sourceLocation = damageSource.getSourcePosition();
            if (sourceLocation != null) {
                Vec3 viewVector = this.getOriginal().getViewVector(1.0F);
                Vec3 toSourceLocation = sourceLocation.subtract(this.getOriginal().position()).normalize();
                if (toSourceLocation.dot(viewVector) > 0.0) {
                    isFront = true;
                }
            }

            if (canBlockSource && isFront) {
                float impact;
                if (damageSource instanceof EpicFightDamageSource) {
                    EpicFightDamageSource efDamageSource = (EpicFightDamageSource)damageSource;
                    impact = amount / 4.0F * (1.0F + efDamageSource.getImpact() / 2.0F);
                    if (efDamageSource.hasTag(SourceTags.GUARD_PUNCTURE)) {
                        impact = Float.MAX_VALUE;
                    }
                } else {
                    impact = amount / 3.0F;
                }

                float knockback = 0.25F + Math.min(impact * 0.1F, 1.0F);
                Entity var11 = damageSource.getDirectEntity();
                if (var11 instanceof LivingEntity) {
                    LivingEntity targetEntity = (LivingEntity)var11;
                    knockback += (float)EnchantmentHelper.getKnockbackBonus(targetEntity) * 0.1F;
                }

                float cost = this.isParry ? this.getParryCostMultiply() : this.getGuardCostMultiply();
                float stamina = this.getStamina() - impact * cost;
                this.setStamina(stamina);
                EpicFightParticles.HIT_BLUNT.get().spawnParticleWithArgument((ServerLevel) this.getOriginal().level, HitParticleType.FRONT_OF_EYES, HitParticleType.ZERO, this.getOriginal(), damageSource.getDirectEntity());
                if (!(stamina >= 0.0F)) {
                    this.setBlocking(false);
                    this.applyStun(StunType.NEUTRALIZE, 2.0F);
                    this.playSound(EpicFightSounds.NEUTRALIZE_MOBS, -0.05F, 0.1F);
                    this.setStamina(this.getMaxStamina());
                    return new AttackResult(AttackResult.ResultType.SUCCESS, amount / 2.0F);
                }

                float counter_cost = this.getCounterStamina();
                Random random = this.getOriginal().getRandom();
                this.rotateTo(damageSource.getDirectEntity(), 30.0F, true);
                if (random.nextFloat() < this.getCounterChance() && stamina >= counter_cost) {
                    if (this.stun_immunity_time > 0) {
                        this.getOriginal().addEffect(new MobEffectInstance(EpicFightMobEffects.STUN_IMMUNITY.get(), this.stun_immunity_time));
                    }

                    this.setAttackSpeed(this.getCounterSpeed());
                    this.playAnimationSynchronized(this.getCounter(), 0.0F);
                    if (isParry) this.playSound(Sounds.BIGBONG, -0.31F, -0.27F);
                    else this.playSound(Sounds.BONG, -0.31F, -0.27F);
                    if (this.cancel_block) {
                        this.setBlocking(false);
                    }

                    this.setStamina(this.getStamina() - counter_cost);
                } else if (this.isParry) {
                    if (this.parryCounter + 1 >= this.maxParryTimes) {
                        this.setBlocking(false);
                        if (this.stun_immunity_time > 0) {
                            this.getOriginal().addEffect(new MobEffectInstance(EpicFightMobEffects.STUN_IMMUNITY.get(), this.stun_immunity_time));
                        }
                    }

                    this.playAnimationSynchronized(this.getParryAnimation(), 0.0F);
                    ++this.parryCounter;
                    ++this.parryTimes;
                    if (isParry) this.playSound(Sounds.BIGBONG, -0.31F, -0.27F);
                    else this.playSound(Sounds.BONG, -0.31F, -0.27F);
                    this.knockBackEntity(damageSource.getDirectEntity().position(), 0.4F * knockback);
                } else {
                    this.playAnimationSynchronized(success, 0.1F);
                    if (animation.isShield) {
                        this.playSound(SoundEvents.SHIELD_BLOCK, -0.05F, 0.1F);
                    } else {
                        if (isParry) this.playSound(Sounds.BIGBONG, -0.31F, -0.27F);
                        else this.playSound(Sounds.BONG, -0.31F, -0.27F);
                    }
                    this.knockBackEntity(damageSource.getDirectEntity().position(), knockback);
                }

                return new AttackResult(AttackResult.ResultType.BLOCKED, amount);
            }
        }

        if (damageSource instanceof EpicFightDamageSource efDamageSource) {
            this.lastGetImpact = efDamageSource.getImpact();
        } else {
            this.lastGetImpact = amount / 3.0F;
        }

        return new AttackResult(AttackResult.ResultType.SUCCESS, amount);
    }
}


