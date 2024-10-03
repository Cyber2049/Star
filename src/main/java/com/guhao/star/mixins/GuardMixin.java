package com.guhao.star.mixins;

import com.guhao.star.regirster.Sounds;
import com.guhao.star.units.Guard_Array;
import com.nameless.toybox.common.attribute.ai.ToyBoxAttributes;
import com.nameless.toybox.config.CommonConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.function.BiFunction;

@Mixin(value = GuardSkill.class, remap = false)
public abstract class GuardMixin extends Skill {
    public GuardMixin(Builder<? extends Skill> builder) {
        super(builder);
    }
    private HurtEvent.Pre event;
    @Unique
    Guard_Array star_new$GuardArray = new Guard_Array();
    @Unique
    StaticAnimation[] star_new$GUARD = star_new$GuardArray.getGuard();
    @Unique
    StaticAnimation[] star_new$PARRY = star_new$GuardArray.getParry();

    @Shadow
    protected boolean isBlockableSource(DamageSource damageSource, boolean advanced) {
        return !damageSource.isBypassInvul() && !damageSource.isBypassArmor() && !damageSource.isProjectile() && !damageSource.isExplosion() && !damageSource.isMagic() && !damageSource.isFire();
    }

    @Shadow
    protected float getPenalizer(CapabilityItem itemCapability) {
        return this.penalizer;
    }

    @Shadow
    public abstract void dealEvent(PlayerPatch<?> playerpatch, HurtEvent.Pre event, boolean advanced);

    @Final
    @Shadow
    protected static SkillDataManager.SkillDataKey<Float> PENALTY;
    @Shadow
    @Final
    protected Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?>> guardMotions;
    @Final
    @Shadow
    protected Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?>> advancedGuardMotions;
    @Final
    @Shadow
    protected Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?>> guardBreakMotions;
    @Shadow
    protected float penalizer;

    @Shadow
    @Nullable
    protected abstract StaticAnimation getGuardMotion(PlayerPatch<?> playerpatch, CapabilityItem itemCapability, GuardSkill.BlockType blockType);

    @Inject(method = "guard", remap = false, at = @At("HEAD"), cancellable = true)
    public void guard(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced, CallbackInfo ci) {
        ci.cancel();
        DamageSource damageSource = event.getDamageSource();
        EpicFightDamageSource epicFightDamageSource = Guard_Array.getEpicFightDamageSources(damageSource);
        if (epicFightDamageSource != null) {
            StaticAnimation an = epicFightDamageSource.getAnimation();
            if (!(Arrays.asList(star_new$GUARD).contains(an)) && !(Arrays.asList(star_new$PARRY).contains(an))) {
                if (this.isBlockableSource(damageSource, advanced) && !(Arrays.asList(star_new$GUARD).contains(an)) && !(Arrays.asList(star_new$PARRY).contains(an))) {
                    event.getPlayerPatch().playSound(Sounds.BONG, -0.06F, 0.12F);
                    ServerPlayer serveerPlayer = event.getPlayerPatch().getOriginal();
                    EpicFightParticles.HIT_BLUNT.get().spawnParticleWithArgument(serveerPlayer.getLevel(), HitParticleType.FRONT_OF_EYES, HitParticleType.ZERO, serveerPlayer, damageSource.getDirectEntity());
                    Entity var10 = damageSource.getDirectEntity();
                    if (var10 instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) var10;
                        knockback += (float) EnchantmentHelper.getKnockbackBonus(livingEntity) * 0.1F;
                    }

                    float penalty = container.getDataManager().getDataValue(PENALTY) + getPenalizer(itemCapability);
                    float consumeAmount = penalty * impact;
                    event.getPlayerPatch().knockBackEntity(damageSource.getDirectEntity().position(), knockback);
                    event.getPlayerPatch().consumeStaminaAlways(consumeAmount);
                    container.getDataManager().setDataSync(PENALTY, penalty, event.getPlayerPatch().getOriginal());
                    GuardSkill.BlockType blockType = event.getPlayerPatch().hasStamina(0.0F) ? GuardSkill.BlockType.GUARD : GuardSkill.BlockType.GUARD_BREAK;
                    StaticAnimation animation = getGuardMotion(event.getPlayerPatch(), itemCapability, blockType);
                    if (animation != null) {
                        event.getPlayerPatch().playAnimationSynchronized(animation, 0.0F);
                    }

                    if (blockType == GuardSkill.BlockType.GUARD_BREAK) {
                        ((ServerPlayerPatch) event.getPlayerPatch()).playSound(EpicFightSounds.NEUTRALIZE_MOBS, 3.0F, 0.0F, 0.1F);
                    }

                    dealEvent(event.getPlayerPatch(), event, advanced);
                } else {
                    if (this.isBlockableSource(damageSource, advanced)) {
                        event.getPlayerPatch().playSound(Sounds.BONG, -0.06F, 0.12F);
                        ServerPlayer serveerPlayer = (ServerPlayer) ((ServerPlayerPatch) event.getPlayerPatch()).getOriginal();
                        ((HitParticleType) EpicFightParticles.HIT_BLUNT.get()).spawnParticleWithArgument(serveerPlayer.getLevel(), HitParticleType.FRONT_OF_EYES, HitParticleType.ZERO, serveerPlayer, damageSource.getDirectEntity());
                        Entity var10 = damageSource.getDirectEntity();
                        if (var10 instanceof LivingEntity) {
                            LivingEntity livingEntity = (LivingEntity) var10;
                            knockback += (float) EnchantmentHelper.getKnockbackBonus(livingEntity) * 0.1F;
                        }

                        float penalty = container.getDataManager().getDataValue(PENALTY) + getPenalizer(itemCapability);
                        float consumeAmount = penalty * impact;
                        event.getPlayerPatch().knockBackEntity(damageSource.getDirectEntity().position(), knockback);
                        event.getPlayerPatch().consumeStaminaAlways(consumeAmount);
                        container.getDataManager().setDataSync(PENALTY, penalty, (ServerPlayer) ((ServerPlayerPatch) event.getPlayerPatch()).getOriginal());
                        GuardSkill.BlockType blockType = ((ServerPlayerPatch) event.getPlayerPatch()).hasStamina(0.0F) ? GuardSkill.BlockType.GUARD : GuardSkill.BlockType.GUARD_BREAK;
                        StaticAnimation animation = getGuardMotion(event.getPlayerPatch(), itemCapability, blockType);
                        if (animation != null) {
                            ((ServerPlayerPatch) event.getPlayerPatch()).playAnimationSynchronized(animation, 0.0F);
                        }

                        if (blockType == GuardSkill.BlockType.GUARD_BREAK) {
                            ((ServerPlayerPatch) event.getPlayerPatch()).playSound(EpicFightSounds.NEUTRALIZE_MOBS, 3.0F, 0.0F, 0.1F);
                        }

                        dealEvent(event.getPlayerPatch(), event, advanced);
                    }
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
        if (ModList.get().isLoaded("epicparagliders") && (Boolean) CommonConfig.EPIC_PARAGLIDER_COMPAT.get()) {
            return impact;
        } else {
            float blockrate = 1.0F - Math.min((float)((ServerPlayer)((ServerPlayerPatch)this.event.getPlayerPatch()).getOriginal()).getAttributeValue((Attribute) ToyBoxAttributes.BLOCK_RATE.get()) / 100.0F, 0.9F);
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
