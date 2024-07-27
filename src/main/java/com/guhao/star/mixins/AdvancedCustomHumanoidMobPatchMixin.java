package com.guhao.star.mixins;

import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import net.minecraft.world.entity.PathfinderMob;
import org.spongepowered.asm.mixin.Mixin;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.world.capabilities.entitypatch.Faction;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;

@Mixin(value = AdvancedCustomHumanoidMobPatch.class,remap = false)
public class AdvancedCustomHumanoidMobPatchMixin<T extends PathfinderMob> extends HumanoidMobPatch<T> {
    public AdvancedCustomHumanoidMobPatchMixin(Faction faction) {
        super(faction);
    }

    @Override
    public void initAnimator(ClientAnimator clientAnimator) {
    }

    @Override
    public void updateMotion(boolean b) {
    }
/*
    @Final
    @Mutable
    @Shadow
    private static EntityDataAccessor<Boolean> IS_BLOCKING;
    @Final
    @Shadow
    private float maxStamina;
    @Final
    @Shadow
    private static EntityDataAccessor<Float> STAMINA;

    @Shadow
    public boolean isBlocking() {
        return this.original.getEntityData().get(IS_BLOCKING);
    }

    @Shadow
    public CustomGuardAnimation getGuardAnimation() {
        return null;
    }

    @Shadow
    public float getStamina() {
        return 0.5f;
    }

    @Shadow
    public float getMaxStamina() {
        return this.maxStamina;
    }


    @Shadow
    public float getCounterChance() {
        return 0.5f;
    }

    @Shadow
    public float getCounterStamina() {
        return 0.5f;
    }
    @Shadow
    public boolean canBlockProjectile() {
        return true;
    }

    @Shadow private int tickSinceBreakShield;

    @Inject(method = "tryProcess", at = @At("TAIL"))
    private void onTryProcess(DamageSource damageSource, float amount, CallbackInfoReturnable<AttackResult> cir) {
        if (this.isBlocking()) {
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
                    EpicFightDamageSource efDamageSource = (EpicFightDamageSource) damageSource;
                    impact = amount / 4.0F * (1.0F + efDamageSource.getImpact() / 2.0F);
                    if (efDamageSource.hasTag(SourceTags.GUARD_PUNCTURE)) {
                        impact = Float.MAX_VALUE;
                    }
                } else {
                    impact = amount / 3.0F;
                }
                float counter_cost = this.getCounterStamina();
                CustomGuardAnimation animation = this.getGuardAnimation();
                float stamina = this.getStamina() - impact * this.getStaminaCostMultiply();
                Random random = this.getOriginal().getRandom();
                if (random.nextFloat() < this.getCounterChance() && stamina >= counter_cost) {
                    this.playSound(Sounds.BIGBONG, -0.05F, 0.1F);
                } else {
                    this.playSound(animation.isShield ? SoundEvents.SHIELD_BLOCK : Sounds.BIGBONG, -0.05F, 0.1F);
                }
            }
        }
    }
    */
}


