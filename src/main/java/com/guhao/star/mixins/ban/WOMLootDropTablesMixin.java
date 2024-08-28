package com.guhao.star.mixins.ban;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import reascer.wom.world.loot.WOMLootDropTables;
import yesman.epicfight.api.forgeevent.SkillLootTableRegistryEvent;

@Mixin(value = WOMLootDropTables.class,remap = false)
@Mod.EventBusSubscriber(
        modid = "wom",
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class WOMLootDropTablesMixin {
    /**
     * @author
     * @reason
     */
    @Overwrite
    @SubscribeEvent
    public static void SkillLootDrop(SkillLootTableRegistryEvent event) {}
}
