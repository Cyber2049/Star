package com.guhao.star.efmex;

import net.minecraftforge.fml.ModList;

public class IntegrationHandler {
    public static void construct() {
        ModList modlist = ModList.get();
        if (modlist.isLoaded("epicfight")) StarAnimations.init();
    }
}
