package com.guhao.star.mixins;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(BaseContainerBlockEntity.class)
public class OpenGuiMixin {
    @Inject(method = "canOpen",at = @At("HEAD"), cancellable = true)
    public void canOpen(Player p_58645_, CallbackInfoReturnable<Boolean> cir) {
        PlayerPatch<?> pp = EpicFightCapabilities.getEntityPatch(p_58645_, PlayerPatch.class);
        if (pp != null && pp.isBattleMode() && (!(p_58645_.getMainHandItem().isEmpty()))) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
