package com.guhao.star.mixins.itemmixin;

import com.legacy.blue_skies.items.arcs.ArcItem;
import com.legacy.blue_skies.items.arcs.EtherealArcItem;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(value = EtherealArcItem.class,remap = false)
public class EtherealArcItemMixin extends ArcItem {
    public EtherealArcItemMixin(int slotId) {
        super(slotId,"ethereal");
    }
    @Unique
    private static final AttributeModifier[] NEW_SPEED_BOOSTS;
    static {
        NEW_SPEED_BOOSTS = new AttributeModifier[]{new AttributeModifier(UUID.fromString("9b997a54-73f4-4cea-9527-d24220cc98f6"), "ethereal_speed_1", 0.05, AttributeModifier.Operation.MULTIPLY_TOTAL), new AttributeModifier(UUID.fromString("33b4f594-371b-11e9-b210-d663bd873d94"), "ethereal_speed_2", 0.06, AttributeModifier.Operation.MULTIPLY_TOTAL), new AttributeModifier(UUID.fromString("945684da-5657-4294-9342-803c3528dbc6"), "ethereal_speed_3", 0.08, AttributeModifier.Operation.MULTIPLY_TOTAL), new AttributeModifier(UUID.fromString("9f6ef2b9-a342-4a6f-adf9-13b30fb71ea7"), "ethereal_speed_4", 0.1, AttributeModifier.Operation.MULTIPLY_TOTAL)};
    }
    @Inject(method = "onEquip",remap = false,at = @At("HEAD"), cancellable = true)
    public void onEquip(ItemStack stack, Player player, CallbackInfo ci) {
        ci.cancel();
        AttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        int level = this.getFunctionalLevel(stack, player);
        if (!attribute.hasModifier(NEW_SPEED_BOOSTS[level])) {
            attribute.addTransientModifier(NEW_SPEED_BOOSTS[level]);
        }

    }
    @Inject(method = "onUnequip",remap = false,at = @At("HEAD"), cancellable = true)
    public void onUnequip(ItemStack stack, Player player, CallbackInfo ci) {
        ci.cancel();
        AttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        for(int i = 0; i < NEW_SPEED_BOOSTS.length; ++i) {
            if (attribute.hasModifier(NEW_SPEED_BOOSTS[i])) {
                attribute.removeModifier(NEW_SPEED_BOOSTS[i]);
            }
        }

    }
}
