package com.guhao.star.regirster;


import com.guhao.star.client.particle.par.Dangers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.guhao.star.Star.MODID;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ParticleType {
    public static final DeferredRegister<net.minecraft.core.particles.ParticleType<?>> PARTICLES;
    public static final RegistryObject<SimpleParticleType> DANGER;

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void RP(ParticleFactoryRegisterEvent event) {
        ParticleEngine PE = Minecraft.getInstance().particleEngine;
        PE.register(DANGER.get(), Dangers.DangerParticleProvider::new);
    }

    public ParticleType() {
    }

    static {
        PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MODID);
        DANGER = PARTICLES.register("dangers", () -> {
            return new SimpleParticleType(false);
        });
    }
}
