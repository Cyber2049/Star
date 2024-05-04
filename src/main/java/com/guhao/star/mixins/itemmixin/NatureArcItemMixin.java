package com.guhao.star.mixins.itemmixin;

import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.items.arcs.ArcItem;
import com.legacy.blue_skies.items.arcs.NatureArcItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NatureArcItem.class,remap = false)
public class NatureArcItemMixin extends ArcItem {
    public NatureArcItemMixin(int slotId) {
        super(slotId,"nature");
    }
    @Inject(method = "serverTick",at = @At("HEAD"), cancellable = true)
    public void serverTick(ItemStack stack, ServerPlayer player, CallbackInfo ci) {
        ci.cancel();
        SkiesPlayer.ifPresent(player, (skiesPlayer) -> {
            int regenRate = 40;
            if (player.tickCount % regenRate == 0 && player.getLastDamageSource() == null) {
                float maxHealth = (float)((this.getFunctionalLevel(stack, player) + 1) * 2);
                float currentHealth = skiesPlayer.getNatureHealth();
                if (currentHealth < maxHealth) {
                    skiesPlayer.setNatureHealth(Math.min(currentHealth + 1.0F, maxHealth));
                }
            }
        });
    }
}
