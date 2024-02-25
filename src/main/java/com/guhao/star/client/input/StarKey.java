package com.guhao.star.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;

import static com.guhao.star.Star.MODID;

@OnlyIn(Dist.CLIENT)
public class StarKey {
    public static final KeyMapping LOCK_ON = new KeyMapping("key." + MODID + ".lock_on", InputConstants.KEY_G, "key." + MODID + ".combat");
    public static void registerKeys() {
        ClientRegistry.registerKeyBinding(LOCK_ON);
    }
}

