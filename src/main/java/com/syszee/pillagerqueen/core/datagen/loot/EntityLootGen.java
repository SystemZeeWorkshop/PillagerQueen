package com.syszee.pillagerqueen.core.datagen.loot;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.function.BiConsumer;

public class EntityLootGen extends SimpleFabricLootTableProvider {
    public EntityLootGen(FabricDataGenerator dataGenerator) {
        super(dataGenerator, LootContextParamSets.ENTITY);
    }

    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> resourceLocationBuilderBiConsumer) {

    }
}
