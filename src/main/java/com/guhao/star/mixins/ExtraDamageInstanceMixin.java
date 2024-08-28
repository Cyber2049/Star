package com.guhao.star.mixins;

import org.spongepowered.asm.mixin.Mixin;
import yesman.epicfight.world.damagesource.ExtraDamageInstance;

@Mixin(value = ExtraDamageInstance.class, remap = false)
public class ExtraDamageInstanceMixin {
/*
    @ModifyVariable(at = @At("HEAD"), name = "SWEEPING_EDGE_ENCHANTMENT")
    private static float[] modifyTargetLostHealth(float[] params) {
        // 在这里修改params数组的值
        params[0] = 2.0f; // 修改后的值
        return params;
    }

 */
}