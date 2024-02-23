package com.guhao.star.event;

import com.guhao.star.Config;
import com.guhao.star.client.RenderCustomKatana;
import com.guhao.star.regirster.Items;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;

import java.util.List;

import static com.guhao.star.Star.MODID;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid= MODID, value=Dist.CLIENT, bus=EventBusSubscriber.Bus.MOD)
public class ClientModBusEvent {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void RenderRegistry(final PatchedRenderersEvent.Add event) {
        //sheath
        List<? extends String> katanaitem = Config.KATANA_ITEM.get();
        for(String katana : katanaitem){
            String[] entry = katana.split(" ");
            Item katanaItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(entry[0]));
            event.addItemRenderer(katanaItem, new RenderCustomKatana());
        }
    }

    @SubscribeEvent
    public static void propertyOverrideRegistry(FMLClientSetupEvent event){
        event.enqueueWork(() -> ItemProperties.register(Items.CUSTOM_SHEATH.get(),
                new ResourceLocation(MODID,"custom"),
                (Stack, World, Entity, i) -> {
                    if (Entity != null) {
                        return Config.modelMap.get(Entity.getItemInHand(InteractionHand.MAIN_HAND).getItem());
                    }
                    return 0;
                }));
    }
}
