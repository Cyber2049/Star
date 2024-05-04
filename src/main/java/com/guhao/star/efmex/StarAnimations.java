package com.guhao.star.efmex;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;

import static com.guhao.star.Star.MODID;

public class StarAnimations {
    public static StaticAnimation EAT_LEFT;
    public static StaticAnimation EAT_RIGHT;
    public static StaticAnimation DRINK_LEFT;
    public static StaticAnimation DRINK_RIGHT;
    public static StaticAnimation SEKIRO;

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(StarAnimations::registerAnimations);
        MinecraftForge.EVENT_BUS.register(new StarAnimations());
    }
    public static void registerAnimations(AnimationRegistryEvent event) {
        event.getRegistryMap().put(MODID, StarAnimations::build);
    }

    private static void build() {

    }
}
