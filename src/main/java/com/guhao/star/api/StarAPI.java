package com.guhao.star.api;

import net.minecraft.world.entity.LivingEntity;

public class StarAPI {

    public StarAPI() {
    }

    public float getRecovery(LivingEntity entity) {
        return (float) entity.getPersistentData().getDouble("recovery");
    }

    public void setRecovery(LivingEntity entity, float RecoveryValue) {
        entity.getPersistentData().putDouble("recovery", RecoveryValue);
    }

    public float getStamina(LivingEntity entity) {
        return (float) entity.getPersistentData().getDouble("stamina");
    }

    public void setStaminaStar(LivingEntity entity, float StaminaValue) {
        entity.getPersistentData().putDouble("stamina", StaminaValue);
    }
}
