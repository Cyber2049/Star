package com.guhao.star.event.lockon;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.guhao.star.client.LocalPlayerPatchEX;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.settings.KeyBindingMap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.world.entity.eventlistener.MovementInputEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import static com.guhao.star.Star.MODID;
import static com.guhao.star.client.input.StarKey.LOCK_ON;

@OnlyIn(Dist.CLIENT)
public class Lockon extends ControllEngine {
    private  Map<KeyMapping, BiConsumer<KeyMapping, Integer>> keyFunctions = Maps.newHashMap();
    private  LocalPlayerPatchEX playerpatch;
    private  KeyBindingMap keyHash;
    private  Set<Object> packets = Sets.newHashSet();
    private  Minecraft minecraft;
    private LocalPlayer player;
    private LocalPlayerPatchEX pl;
    private int weaponInnatePressCounter = 0;
    private int sneakPressCounter = 0;
    private int moverPressCounter = 0;
    private int lastHotbarLockedTime;
    private boolean weaponInnatePressToggle = false;
    private boolean sneakPressToggle = false;
    private boolean moverPressToggle = false;
    private boolean attackLightPressToggle = false;
    private boolean hotbarLocked;
    private boolean chargeKeyUnpressed;
    private int reserveCounter;
    private KeyMapping reservedKey;
    private KeyMapping currentChargingKey;

    public Options options;



    public Lockon() {
        Events.controllEngine = this;
        this.minecraft = Minecraft.getInstance();
        this.options = this.minecraft.options;
        keyFunctions.put(LOCK_ON, this::lockonPressed);
        try {
            keyHash = (KeyBindingMap) ObfuscationReflectionHelper.findField(KeyMapping.class, "f_90810_").get(null);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void lockonPressed(KeyMapping key, int action) {
        if (action == 1) {
            playerpatch.toggleLockOn();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class Events {
        static Lockon controllEngine;

/*
        @SubscribeEvent
        public static void keyboardEvent(InputEvent.KeyInputEvent event) {
            if (controllEngine.minecraft.player != null && controllEngine.playerpatch != null && Minecraft.getInstance().screen == null) {
                InputConstants.Key input = InputConstants.Type.KEYSYM.getOrCreate(event.getKey());
                //Controllable Compat
                InputConstants.Key inputMouse = InputConstants.Type.MOUSE.getOrCreate(event.getKey());

                for (KeyMapping keybinding : controllEngine.keyHash.lookupAll(input)) {
                    if (controllEngine.keyFunctions.containsKey(keybinding)) {
                        controllEngine.keyFunctions.get(keybinding).accept(keybinding, event.getAction());
                    }
                }

                for (KeyMapping keybinding : controllEngine.keyHash.lookupAll(inputMouse)) {
                    if (controllEngine.keyFunctions.containsKey(keybinding)) {
                        controllEngine.keyFunctions.get(keybinding).accept(keybinding, event.getAction());
                    }
                }
            }
        }

 */
    }


}

