package com.syszee.pillagerqueen.core.registry;

import com.syszee.pillagerqueen.core.ModMain;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Function;

public class ModBlocks {
    //public static final Block EXAMPLE_BLOCK = register("pillagerqueen", new ExampleBlock(FabricBlockSettings.copy(Blocks.STONE)));
    //public static final BlockEntityType<ExampleBlockEntity> EXAMPLE_BLOCK_ENTITY = registerBlockEntity("example_block", FabricBlockEntityTypeBuilder.create(ExampleBlockEntity::new, EXAMPLE_BLOCK).build());

    public static void init() {
    }

    private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String name, BlockEntityType<T> blockEntityType) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(ModMain.MOD_ID, name), blockEntityType);
    }

    private static <T extends Block> T register(String name, T block) {
        return register(name, block, new Item.Properties().tab(CreativeModeTab.TAB_MISC));
    }

    private static <T extends Block> T register(String name, T block, Item.Properties itemProperties) {
        return register(name, block, object -> new BlockItem(object, itemProperties));
    }

    private static <T extends Block> T register(String name, T block, Function<T, Item> item) {
        ResourceLocation location = new ResourceLocation(ModMain.MOD_ID, name);
        T object = Registry.register(Registry.BLOCK, location, block);
        Registry.register(Registry.ITEM, location, item.apply(object));
        return object;
    }


}