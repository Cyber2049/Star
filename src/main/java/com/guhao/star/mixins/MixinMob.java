
package com.guhao.star.mixins;

import com.guhao.star.regirster.Effect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MixinMob extends LivingEntity {

    @Shadow @Final public BodyRotationControl bodyRotationControl;

    public MixinMob(Level level) {
        super(null, level);
    }

    @Inject(at = @At("HEAD"), method = "isNoAi", cancellable = true)
    public void isNoAi(CallbackInfoReturnable<Boolean> callback) {
        if (hasEffect(Effect.EXECUTED.get())) {
            callback.setReturnValue(true);
            callback.cancel();
        }
    }
    @Inject(at = @At("HEAD"), method = "tickHeadTurn", cancellable = true)
    private void tickHeadTurn(float p_21538_, float p_21539_, CallbackInfoReturnable<Float> callback) {

    }
    @Inject(at = @At("HEAD"), method = "createBodyControl", cancellable = true)
    protected void createBodyControl(CallbackInfoReturnable<BodyRotationControl> cir) {
        if (hasEffect(Effect.EXECUTED.get())) {
            cir.setReturnValue(null);
            cir.cancel();
        }
    }
}

