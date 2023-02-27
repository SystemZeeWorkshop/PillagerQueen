package com.syszee.pillagerqueen.core.registry;

import com.syszee.pillagerqueen.common.entity.ExampleEntity;
import com.syszee.pillagerqueen.common.entity.PillagerQueenEntity;
import com.syszee.pillagerqueen.core.ModMain;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

public class ModEntities {
    public static final EntityType<ExampleEntity> EXAMPLE = register("example_entity", FabricEntityTypeBuilder.createMob()
            .entityFactory(ExampleEntity::new)
            .spawnGroup(MobCategory.MISC)
            .dimensions(EntityDimensions.scalable(1.0F, 1.0F))
            .trackRangeBlocks(10).build(), 0xff00ff, 0xff00ff);

    public static final EntityType<PillagerQueenEntity> PILLAGER_QUEEN = register("pillager_queen", FabricEntityTypeBuilder.createMob()
            .entityFactory(PillagerQueenEntity::new)
            .spawnGroup(MobCategory.MISC)
            .dimensions(EntityDimensions.scalable(1.0F, 2.0F))
            .trackRangeBlocks(10)
            .build(), 0xA77FC6, 0x7356D5);

    public static void init() {
        FabricDefaultAttributeRegistry.register(EXAMPLE, ExampleEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(PILLAGER_QUEEN, PillagerQueenEntity.createAttributes());
    }

    private static <T extends Mob> EntityType<T> register(String id, EntityType<T> type, int primaryColor, int secondaryColor) {
        EntityType<T> object = register(id, type);
        Registry.register(Registry.ITEM, new ResourceLocation(ModMain.MOD_ID, id + "_spawn_egg"), new SpawnEggItem(object, primaryColor, secondaryColor, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
        return object;
    }

    private static <T extends Entity> EntityType<T> register(String id, EntityType<T> type) {
        return Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(ModMain.MOD_ID, id), type);
    }
}