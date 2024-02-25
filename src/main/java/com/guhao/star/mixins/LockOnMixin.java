/*!别删这个
package com.guhao.star.mixins;

import com.guhao.star.units.MathUnit;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.lockon.LockOnHandler;

import static tfar.lockon.LockOnHandler.lockedOn;

@Mixin(value = LockOnHandler.class,remap = false,priority = 100000)
public class LockOnMixin {

    @Shadow
    private static Entity targeted;
    @Final
    @Shadow
    private static final Minecraft mc = Minecraft.getInstance();
    @Inject(at = @At("HEAD"), method = "handleKeyPress")
    private static void handleKeyPress(Player player, double d2, double d3, CallbackInfoReturnable<Boolean> cir) {
        if (player != null && !mc.isPaused() && targeted != null) {
            double y = MathUnit.offsetY + 0.965;
            MathUnit.A = y;
            MathUnit.LOCK = true;
        }
    }
    @Inject(at = @At("HEAD"),method = "leaveLockOn")
    private static void leaveLockOn(CallbackInfo ci) {
        MathUnit.LOCK = false;
    }
    @Inject(at = @At("HEAD"),method = "tick")
    private static void tick(TickEvent.ClientTickEvent e, CallbackInfo ci) {
        if (!lockedOn) {
            MathUnit.LOCK = false;
        }
    }
}

 */

