package com.guhao.star.mixins;

import com.guhao.star.regirster.Effect;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

@Mixin(value = PlayerPatch.class , remap = false)
public abstract class PlayerPatchMixin<T extends Player> extends LivingEntityPatch<T> {

    @Inject(method = "getDamageSource",at = @At("TAIL"))
    public void getDamageSource(StaticAnimation animation, InteractionHand hand, CallbackInfoReturnable<EpicFightDamageSource> cir) {
        Player p = this.getOriginal();
        EpicFightDamageSource damagesource = EpicFightDamageSource.commonEntityDamageSource("player", this.original, animation);
        if (p.hasEffect(Effect.REALLY_STUN_IMMUNITY.get())) {
            damagesource.setImpact(0);
        } else {
            damagesource.setImpact(this.getImpact(hand));
        }
    }
}
