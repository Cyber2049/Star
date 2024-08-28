package com.guhao.star.client.particle.par;


import com.dfdyz.epicacg.client.particle.DMC.JCBladeTrail;
import com.dfdyz.epicacg.utils.RenderUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.api.utils.math.Vec3f;

import java.util.Random;


@OnlyIn(Dist.CLIENT)
public class Fire_Ball extends NoRenderParticle {
    public Fire_Ball(ClientLevel level, double x, double y, double z, double rx, double ry, double rz) {
        super(level, x, y, z, rx, ry, rz);
        this.lifetime = 1;
    }

    public boolean shouldCull() {
        return false;
    }

    public void tick() {
        if (this.age++ >= this.lifetime) {
            this.remove();
        }
        for(int i = 0; i < 2; ++i) {
            int n = 500;
            double rn = 0.2;
            double tn = 0.01;
            for (int l = 0; l < n; ++l) {
                double theta = 6.283185307179586 * (new Random()).nextDouble();
                double phi = ((new Random()).nextDouble() - 0.5) * Math.PI * tn / rn;
                double xn = rn * Math.cos(phi) * Math.cos(theta);
                double yn = rn * Math.cos(phi) * Math.sin(theta);
                double zn = rn * Math.sin(phi);
                Vec3f direction = new Vec3f((float) xn, (float) yn, (float) zn);
                RenderUtils.AddParticle(this.level, new JCBladeTrail(this.level, x, y, z, direction.x,direction.y,direction.z));
            }
        }
    }
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new Fire_Ball(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
