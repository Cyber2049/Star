package com.guhao.star.regirster;

import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;

@Mod.EventBusSubscriber(
        modid = "star",
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class Sounds {

    public static final HashSet<SoundEvent> SOUND_EVENTS = Sets.newHashSet();
    public static final SoundEvent BONG = RegSound("bong");
    public static final SoundEvent BIGBONG = RegSound("bigbong");
    public static final SoundEvent DANGER = RegSound("danger");
    public static final SoundEvent DUANG1 = RegSound("duang1");
    public static final SoundEvent DUANG2 = RegSound("duang2");
    public static final SoundEvent FORESIGHT = RegSound("foresight");
    public static final SoundEvent YAMATO_STEP = RegSound("yamato_step");
    public static final SoundEvent SEKIRO = RegSound("sekiro");

    public Sounds() {
    }

    private static SoundEvent RegSound(String name) {
        ResourceLocation r = new ResourceLocation("star", name);
        SoundEvent s = (new SoundEvent(r)).setRegistryName(name);
        SOUND_EVENTS.add(s);
        return s;
    }

    @SubscribeEvent
    public static void onSoundRegistry(RegistryEvent.Register<SoundEvent> event) {
        SOUND_EVENTS.forEach((s) -> {
            event.getRegistry().register(s);
        });
    }
}

