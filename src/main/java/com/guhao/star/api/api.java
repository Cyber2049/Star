package com.guhao.star.api;

import net.minecraftforge.event.entity.living.LivingDeathEvent;

public interface api {
    void onDeath(LivingDeathEvent event);

    void correctRotation();
}
