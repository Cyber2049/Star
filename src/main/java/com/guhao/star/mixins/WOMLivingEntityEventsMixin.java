package com.guhao.star.mixins;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import reascer.wom.events.WOMLivingEntityEvents;
import reascer.wom.gameasset.WOMEnchantment;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

import java.util.Map;
import java.util.UUID;

@Mixin(value = WOMLivingEntityEvents.class,remap = false)
@Mod.EventBusSubscriber(
        modid = "wom",
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class WOMLivingEntityEventsMixin {
    @Shadow
    private static final Map<EquipmentSlot, UUID> STAMINAR_ADD = makeUUIDMap("wom_staminar_add");
    @Shadow
    private static Map<EquipmentSlot, UUID> makeUUIDMap(String key) {
        return Map.of();
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    @SubscribeEvent
    public static void itemAttributeModifiers(ItemAttributeModifierEvent event) {
        float invigoration = 0.0F;
        ItemStack stack = event.getItemStack();
        EquipmentSlot slot = event.getSlotType();
        if (slot == LivingEntity.getEquipmentSlotForItem(stack)) {
            invigoration += (float) EnchantmentHelper.getItemEnchantmentLevel(WOMEnchantment.INVIGORATION.get(), stack);
        }

        if (invigoration != 0.0F) {
            event.addModifier(EpicFightAttributes.MAX_STAMINA.get(), new AttributeModifier(STAMINAR_ADD.get(slot), "invigoration_stamina_add", invigoration * 0.1F, AttributeModifier.Operation.ADDITION));
        }

    }
}
