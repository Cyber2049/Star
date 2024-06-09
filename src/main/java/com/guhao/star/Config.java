package com.guhao.star;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class Config {

    public static Config INSTANCE = null;

    public final static Map<Item,Float> modelMap = new HashMap<>();
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> KATANA_ITEM = BUILDER
            .comment("items considered as katana", "example: minecraft:apple 0")
            .defineListAllowEmpty(Collections.singletonList("katana_items"), ArrayList::new, obj -> true);

    public static final ForgeConfigSpec.BooleanValue SLOW_TIME = BUILDER
            .comment("slow time.")
            .define("Multiplayer please turn off",true);



    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static void load(){
        List<?extends String> katanaitem = Config.KATANA_ITEM.get();
        Boolean time = Config.SLOW_TIME.get();
        for (String katana : katanaitem){
            String[] entry = katana.split(" ");
            Item katanaItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(entry[0]));
            modelMap.put(katanaItem, Float.parseFloat(entry[1]));
        }
    }
}
