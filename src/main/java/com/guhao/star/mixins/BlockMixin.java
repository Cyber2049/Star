package com.guhao.star.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(BlockBehaviour.class)
public abstract class BlockMixin {
    @Inject(method = "canBeReplaced(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/Fluid;)Z", at = @At("HEAD"), cancellable = true)
    private void onBlockPlacedL(BlockState p_60535_, Fluid p_60536_, CallbackInfoReturnable<Boolean> cir) {
        PlayerPatch<?> pp = EpicFightCapabilities.getEntityPatch(Minecraft.getInstance().player, PlayerPatch.class);
        if (pp != null && pp.isBattleMode() && (!(Minecraft.getInstance().player.getMainHandItem().isEmpty()))) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
    @Inject(method = "canBeReplaced(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/item/context/BlockPlaceContext;)Z", at = @At("HEAD"), cancellable = true)
    private void onBlockPlacedB(BlockState p_60470_, BlockPlaceContext p_60471_, CallbackInfoReturnable<Boolean> cir) {
        PlayerPatch<?> pp = EpicFightCapabilities.getEntityPatch(Minecraft.getInstance().player, PlayerPatch.class);
        if (pp != null && pp.isBattleMode() && (!(Minecraft.getInstance().player.getMainHandItem().isEmpty()))) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
