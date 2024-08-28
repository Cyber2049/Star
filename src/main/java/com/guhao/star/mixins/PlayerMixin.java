package com.guhao.star.mixins;

import com.guhao.star.regirster.Effect;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.OptionalInt;

@Mixin({Player.class})
public abstract class PlayerMixin extends LivingEntity {


    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    public void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {

            if (this.hasEffect(Effect.EXECUTE.get()) | this.hasEffect(Effect.EXECUTED.get())) {
                cir.setReturnValue(false);
            }

    }
    @Inject(method = "openMenu",at = @At("HEAD"), cancellable = true)
    public void openMenu(MenuProvider p_36150_, CallbackInfoReturnable<OptionalInt> cir) {
        PlayerPatch<?> pp = EpicFightCapabilities.getEntityPatch(Minecraft.getInstance().player, PlayerPatch.class);
        if (pp != null && pp.isBattleMode() && (!(Minecraft.getInstance().player.getMainHandItem().isEmpty()))) {
            cir.setReturnValue(OptionalInt.empty());
            cir.cancel();
        }
    }
    @Inject(method = "interactOn", at = @At("HEAD"), cancellable = true)
    public void interactOn(Entity p_36158_, InteractionHand p_36159_, CallbackInfoReturnable<InteractionResult> cir) {
        if (p_36158_ instanceof Player) {
            PlayerPatch<?> pp = EpicFightCapabilities.getEntityPatch(Minecraft.getInstance().player, PlayerPatch.class);
            if (pp != null && pp.isBattleMode() && (!(Minecraft.getInstance().player.getMainHandItem().isEmpty()))) {
                cir.setReturnValue(InteractionResult.FAIL);
                cir.cancel();
            }
        }
    }
    @Inject(method = "mayUseItemAt", at = @At("HEAD"), cancellable = true)
    public void mayUseItemAt(BlockPos p_36205_, Direction p_36206_, ItemStack p_36207_, CallbackInfoReturnable<Boolean> cir) {
        PlayerPatch<?> pp = EpicFightCapabilities.getEntityPatch(Minecraft.getInstance().player, PlayerPatch.class);
        if (pp != null && pp.isBattleMode() && (!(Minecraft.getInstance().player.getMainHandItem().isEmpty()))) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
