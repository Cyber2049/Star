package com.guhao.star.efmex;

import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.collider.MultiOBBCollider;
import yesman.epicfight.api.collider.OBBCollider;

public class StarWeaponCategories {
    public static final Collider BLADE_RUSH_FINISHER = new OBBCollider(1.2D, 0.8D, 2.0D, 0D, 1.0D, -1.2D);
    public static final Collider MORTAL_BLADE = new MultiOBBCollider(3, 0.4D, 2D, 4D, 0D, 0.5D, -5D);
    public static final Collider LETHAL_SLICING = new OBBCollider(2.0D, 0.25D, 1.5D, 0D, 1.0D, -1.0D);
    public static final Collider LETHAL_SLICING1 = new OBBCollider(2.0D, 0.25D, 1.5D, 0D, 0.5D, -1.0D);
    public static final Collider LORD_OF_THE_STORM = new MultiOBBCollider(3, 0.05D, 0.7D, 9.5D, 0.0D, 0.0D, -10.0D);

}
