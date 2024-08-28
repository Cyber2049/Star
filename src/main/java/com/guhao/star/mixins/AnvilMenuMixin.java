package com.guhao.star.mixins;

import com.stereowalker.tiered.Tiered;
import com.stereowalker.tiered.api.ModifierUtils;
import com.stereowalker.tiered.api.PotentialAttribute;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({AnvilMenu.class})
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
    ResourceLocation reforgedAttribute = null;

    public AnvilMenuMixin(MenuType<?> p_39773_, int p_39774_, Inventory p_39775_, ContainerLevelAccess p_39776_) {
        super(p_39773_, p_39774_, p_39775_, p_39776_);
    }

    @Inject(
            method = {"onTake"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V",
                    ordinal = 0
            )}
    )
    private void saveReforgedAttribute(Player p_150474_, ItemStack p_150475_, CallbackInfo ci) {
        if (this.inputSlots.getItem(0).getTagElement("Tiered") != null) {
            this.reforgedAttribute = new ResourceLocation(this.inputSlots.getItem(0).getTagElement("Tiered").getString("Tier"));
        }

    }

    @Redirect(
            method = {"onTake"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V",
                    ordinal = 3
            )
    )
    private void onTake_redirect(Container container, int pIndex, ItemStack pStack, Player p_150474_, ItemStack p_150475_) {
        boolean deleteItem = true;
        if (this.reforgedAttribute != null) {
            PotentialAttribute potential = Tiered.ATTRIBUTE_DATA_LOADER.getItemAttributes().get(this.reforgedAttribute);
            if (container.getItem(pIndex).getItem().getRegistryName().equals(new ResourceLocation(potential.getReforgeItem()))) {
                deleteItem = false;
                ItemStack hammer = container.getItem(pIndex);
                ResourceLocation potentialAttributeID = this.reforgedAttribute;

                for (int i = 0; potentialAttributeID.equals(this.reforgedAttribute) && i < 2; ++i) {
                    potentialAttributeID = ModifierUtils.getRandomAttributeIDFor(p_150475_.getItem());
                }

                if (potentialAttributeID != null) {
                    p_150475_.getOrCreateTagElement("Tiered").putString("Tier", potentialAttributeID.toString());
                }
            }
        }

        if (deleteItem) {
            container.setItem(pIndex, pStack);
        }
    }
}