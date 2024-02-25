package com.guhao.star.units;

import net.minecraft.world.phys.Vec3;

public class MathUnit {
    public static double offsetY;
    public static double A;
    public static boolean LOCK = false;
    public static double getXRotOfVector(Vec3 vec) {
        Vec3 normalized = vec.normalize();
        return -(Math.atan2(normalized.y, (float)Math.sqrt(normalized.x * normalized.x + normalized.z * normalized.z)) * (180D / Math.PI));
    }

    public static double getYRotOfVector(Vec3 vec) {
        Vec3 normalized = vec.normalize();
        return Math.atan2(normalized.z, normalized.x) * (180D / Math.PI) - 90.0F;
    }
}
