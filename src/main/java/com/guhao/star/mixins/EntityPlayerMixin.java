package com.guhao.star.mixins;

import com.guhao.star.regirster.Effect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Player.class})
public class EntityPlayerMixin {
    public EntityPlayerMixin() {
    }
    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    public void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getEntity() instanceof LivingEntity livingEntity) {
             if (livingEntity.hasEffect(Effect.EXECUTE.get()) || livingEntity.hasEffect(Effect.EXECUTED.get())){
                cir.setReturnValue(false);
            }
        }
    }
}
