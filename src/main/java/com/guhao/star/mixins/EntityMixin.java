package com.guhao.star.mixins;

import com.guhao.star.regirster.Effect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class EntityMixin extends CapabilityProvider<Entity> {

    protected EntityMixin(Class<Entity> baseClass) {
        super(baseClass);
    }

    @Inject(at = @At("HEAD"), method = "push", cancellable = true)
    private void collide(Entity p_21294_, CallbackInfo ci) {
        if (p_21294_ instanceof LivingEntity livingEntity) {
            if ((livingEntity.hasEffect(Effect.EXECUTE.get()) | livingEntity.hasEffect(Effect.EXECUTED.get()))) ci.cancel();
            return;
        }
    }
}
