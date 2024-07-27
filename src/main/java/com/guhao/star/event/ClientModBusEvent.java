package com.guhao.star.event;

import com.guhao.star.Config;
import com.guhao.star.client.RenderCustomKatana;
import com.guhao.star.regirster.Items;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;

import javax.annotation.Nullable;
import java.util.List;

import static com.guhao.star.Star.MODID;


@Mod.EventBusSubscriber
public class ClientModBusEvent {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void RenderRegistry(final PatchedRenderersEvent.Add event) {
        //sheath
        List<? extends String> katanaitem = Config.KATANA_ITEM.get();
        for (String katana : katanaitem) {
            String[] entry = katana.split(" ");
            Item katanaItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(entry[0]));
            event.addItemRenderer(katanaItem, new RenderCustomKatana());
        }
    }

    @SubscribeEvent
    public static void propertyOverrideRegistry(FMLClientSetupEvent event) {
        Config.load();
        event.enqueueWork(() -> ItemProperties.register(Items.CUSTOM_SHEATH.get(),
                new ResourceLocation(MODID, "custom"),
                (Stack, World, Entity, i) -> Stack.getOrCreateTag().getFloat("custom_sheath")));
        event.enqueueWork(() -> ItemProperties.register(Items.DEFENSE.get(),
                new ResourceLocation(MODID, "defense"),
                (Stack, World, Entity, i) -> Stack.getOrCreateTag().getFloat("enable_defense")));
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            execute(event, event.player.level, event.player.getX(), event.player.getY(), event.player.getZ());
        }
    }

    public static void execute(LevelAccessor world, double x, double y, double z) {
        execute(null, world, x, y, z);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z) {
        if ((world.getBlockState(new BlockPos(x, y, z))).getBlock() == Blocks.COBWEB) {
            {
                BlockPos _pos = new BlockPos(x, y, z);
                Block.dropResources(world.getBlockState(_pos), world, new BlockPos(x, y, z), null);
                world.destroyBlock(_pos, false);
            }
        }
        if ((world.getBlockState(new BlockPos(x, y+1, z))).getBlock() == Blocks.COBWEB) {
            {
                BlockPos _pos = new BlockPos(x, y+1, z);
                Block.dropResources(world.getBlockState(_pos), world, new BlockPos(x, y+1, z), null);
                world.destroyBlock(_pos, false);
            }
        }
    }
}
