package com.syszee.example.core.registry;

import com.syszee.example.core.ModMain;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static final TagKey<Item> EXAMPLE_ITEM_TAG = createItem("example_items");
    public static final TagKey<Block> EXAMPLE_BLOCK_TAG = createBlock("example_blocks");
    public static final TagKey<EntityType<?>> EXAMPLE_ENTITY_TAG = createEntity("example_entity_types");

    public static void init() {
    }

    public static TagKey<Item> createItem(String name) {
        return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(ModMain.MOD_ID, name));
    }

    private static TagKey<Block> createBlock(String name) {
        return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(ModMain.MOD_ID, name));
    }

    private static TagKey<EntityType<?>> createEntity(String name) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(ModMain.MOD_ID, name));
    }
}
