package com.guhao.star.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.guhao.star.Star.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Recovery {
    private final static double A = 2.0;
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof LivingEntity entity && !(entity instanceof Player)) {
            entity.getPersistentData().putDouble("recovery", A);
        }
    }
}
