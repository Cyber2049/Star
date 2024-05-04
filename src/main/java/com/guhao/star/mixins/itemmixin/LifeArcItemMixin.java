package com.guhao.star.mixins.itemmixin;

import com.legacy.blue_skies.items.arcs.ArcItem;
import com.legacy.blue_skies.items.arcs.LifeArcItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LifeArcItem.class,remap = false)
public class LifeArcItemMixin extends ArcItem {
    public LifeArcItemMixin(int slotId) {
        super(slotId,"life", (new Item.Properties()).tab((CreativeModeTab)null).stacksTo(1));
    }

    @Inject(method = "onHit",at = @At("HEAD"), cancellable = true)
    public void onHit(ItemStack stack, Player player, LivingDamageEvent event, CallbackInfo ci) {
        ci.cancel();
        int level = this.getFunctionalLevel(stack, player);
        float reductionHealth = level == 0 ? 8.0F : (level == 1 ? 16.0F : 24.0F);
        if (player.getRemainingFireTicks() > 0 && player.getHealth() <= reductionHealth) {
            event.setAmount(event.getAmount() / 2.0F);
        }

    }
}
