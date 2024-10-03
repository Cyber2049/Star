package com.guhao.star.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;


@Mixin({ServerPlayer.class})
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level p_36114_, BlockPos p_36115_, float p_36116_, GameProfile p_36117_) {
        super(p_36114_, p_36115_, p_36116_, p_36117_);
    }
//
//    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
//    public void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
//            if (this.hasEffect(Effect.EXECUTE.get()) | this.hasEffect(Effect.EXECUTED.get())) {
//                cir.setReturnValue(false);
//        }
//    }
//
//    @Inject(method = "openMenu",at = @At("HEAD"), cancellable = true)
//    public void openMenu(MenuProvider p_36150_, CallbackInfoReturnable<OptionalInt> cir) {
//        PlayerPatch<?> pp = EpicFightCapabilities.getEntityPatch(Minecraft.getInstance().player, PlayerPatch.class);
//        if (pp != null && pp.isBattleMode() && (!(Minecraft.getInstance().player.getMainHandItem().isEmpty()))) {
//            cir.setReturnValue(OptionalInt.empty());
//            cir.cancel();
//        }
//    }
//
//    @Inject(method = "mayInteract",at = @At("HEAD"), cancellable = true)
//    public void mayInteract(Level p_143406_, BlockPos p_143407_, CallbackInfoReturnable<Boolean> cir) {
//        PlayerPatch<?> pp = EpicFightCapabilities.getEntityPatch(Minecraft.getInstance().player, PlayerPatch.class);
//        if (pp != null && pp.isBattleMode() && (!(Minecraft.getInstance().player.getMainHandItem().isEmpty()))) {
//            cir.setReturnValue(false);
//            cir.cancel();
//        }
//    }
}
