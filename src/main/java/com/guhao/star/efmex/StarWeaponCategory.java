package com.guhao.star.efmex;

import yesman.epicfight.world.capabilities.item.WeaponCategory;

public enum StarWeaponCategory implements WeaponCategory {
    DRAGONSLAYER,
    YAMATO;
    final int id;

    StarWeaponCategory() {
        this.id = WeaponCategory.ENUM_MANAGER.assign(this);
    }

    @Override
    public int universalOrdinal() {
        return this.id;
    }
}
