package com.guhao.star.mixins;

import com.guhao.star.regirster.Effect;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.UseAnim;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.data.reloader.MobPatchReloadListener;
import yesman.epicfight.world.capabilities.entitypatch.CustomHumanoidMobPatch;
import yesman.epicfight.world.capabilities.entitypatch.Faction;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.Iterator;

@Mixin(value = CustomHumanoidMobPatch.class,remap = false)
public class CustomHumanoidMobPatchMixin<T extends PathfinderMob> extends HumanoidMobPatch<T> {
    public CustomHumanoidMobPatchMixin(Faction faction, MobPatchReloadListener.CustomHumanoidMobPatchProvider provider) {
        super(faction);
        this.provider = provider;
    }
    @Mutable
    @Final
    @Shadow
    private final MobPatchReloadListener.CustomHumanoidMobPatchProvider provider;

    @Inject(method = "setAIAsInfantry",at = @At("HEAD"), cancellable = true)
    public void setAIAsInfantry(boolean holdingRanedWeapon, CallbackInfo ci) {
        if (this.getOriginal().hasEffect(Effect.EXECUTED.get())) ci.cancel();
    }

    @Override
    public void initAnimator(ClientAnimator clientAnimator) {
        Iterator<Pair<LivingMotion, StaticAnimation>> var2 = this.provider.getDefaultAnimations().iterator();

        while(var2.hasNext()) {
            Pair<LivingMotion, StaticAnimation> pair = (Pair)var2.next();
            clientAnimator.addLivingAnimation((LivingMotion)pair.getFirst(), (StaticAnimation)pair.getSecond());
        }

        clientAnimator.setCurrentMotionsAsDefault();
    }

    @Override
    public void updateMotion(boolean considerInaction) {
        super.commonAggressiveMobUpdateMotion(considerInaction);
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
            if (CrossbowItem.isCharged(((PathfinderMob)this.original).getMainHandItem())) {
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
}
