package com.guhao.star.mixins.ban;

import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import reascer.wom.world.loot.WOMChestLootTables;

@Mixin(value = WOMChestLootTables.class,remap = false)
@Mod.EventBusSubscriber(
        modid = "wom"
)
public class WOMChestLootTablesMixin {
    /**
     * @author
     * @reason
     */
    @Overwrite
    @SubscribeEvent
    public static void modifyVanillaLootPools(LootTableLoadEvent event) {}
}
