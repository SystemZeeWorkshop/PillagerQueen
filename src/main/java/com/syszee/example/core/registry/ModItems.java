package com.syszee.example.core.registry;

import com.syszee.example.common.item.ExampleItem;
import com.syszee.example.core.ModMain;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class ModItems {
    public static final ExampleItem EXAMPLE_ITEM = register("example_item", new ExampleItem(new FabricItemSettings().tab(CreativeModeTab.TAB_MISC)));

    public static void init() {
    }

    public static <T extends Item> T register(String name, T item) {
        return Registry.register(Registry.ITEM, new ResourceLocation(ModMain.MOD_ID, name), item);
    }
}