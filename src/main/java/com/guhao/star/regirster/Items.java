package com.guhao.star.regirster;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.guhao.star.Star.MODID;

public class Items {

        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
        public static final RegistryObject<Item> CUSTOM_SHEATH = ITEMS.register("custom_sheath", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> DEFENSE = ITEMS.register("defense", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
    }

