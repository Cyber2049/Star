
package com.guhao.star.mixins;

import com.guhao.star.units.MathUnit;
import com.teamderpy.shouldersurfing.client.ShoulderInstance;
import com.teamderpy.shouldersurfing.client.ShoulderRenderer;
import com.teamderpy.shouldersurfing.config.Config;
import com.teamderpy.shouldersurfing.mixins.CameraAccessor;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ShoulderRenderer.class ,remap = false,priority = 1)
public class ShoulderRendererMixin {
    @Shadow
    private double cameraDistance;
    @Shadow
    private double calcCameraDistance(Camera camera, Level level, double distance){
        return distance;
    };

    @Inject(at = @At("HEAD"), method = "offsetCamera", cancellable = true)
    public void offsetCamera(Camera camera, Level level, double partialTick, CallbackInfo ci) {
        ci.cancel();
        if (ShoulderInstance.getInstance().doShoulderSurfing() && level != null) {
            CameraAccessor accessor = (CameraAccessor) camera;
            double x = Mth.lerp(partialTick, camera.getEntity().xo, camera.getEntity().getX());
            double y = Mth.lerp(partialTick, camera.getEntity().yo, camera.getEntity().getY()) + Mth.lerp(partialTick, (double) accessor.getEyeHeightOld(), (double) accessor.getEyeHeight());
            MathUnit.offsetY = y;
            double z = Mth.lerp(partialTick, camera.getEntity().zo, camera.getEntity().getZ());
            if (MathUnit.LOCK) {
                accessor.invokeSetPosition(x, MathUnit.A, z);
            } else {
                accessor.invokeSetPosition(x, y, z);
            }
            Vec3 offset = new Vec3(-Config.CLIENT.getOffsetZ(), Config.CLIENT.getOffsetY(), Config.CLIENT.getOffsetX());
            this.cameraDistance = this.calcCameraDistance(camera, level, accessor.invokeGetMaxZoom(offset.length()));
            Vec3 scaled = offset.normalize().scale(this.cameraDistance);
            accessor.invokeMove(scaled.x, scaled.y, scaled.z);
        }
    }


}



