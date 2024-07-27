package com.guhao.star.regirster;

import com.guhao.star.effects.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.guhao.star.Star.MODID;

public class Effect {
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MODID);
    public static final RegistryObject<MobEffect> DEFENSE = REGISTRY.register("defense", Defense::new);
    public static final RegistryObject<MobEffect> UNSTABLE = REGISTRY.register("unstable", Unstable::new);
    public static final RegistryObject<MobEffect> EXECUTE = REGISTRY.register("execute", Execute::new);
    public static final RegistryObject<MobEffect> EXECUTED = REGISTRY.register("executed", Executed::new);
    public static final RegistryObject<MobEffect> REALLY_STUN_IMMUNITY = REGISTRY.register("really_stun_immunity", Really_stun_immunity::new);
}
