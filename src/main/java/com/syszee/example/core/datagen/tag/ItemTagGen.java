package com.syszee.example.core.datagen.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import org.jetbrains.annotations.Nullable;

public class ItemTagGen extends FabricTagProvider.ItemTagProvider {
    public ItemTagGen(FabricDataGenerator dataGenerator, @Nullable BlockTagProvider blockTagProvider) {
        super(dataGenerator, blockTagProvider);
    }

    @Override
    protected void generateTags() {

    }
}
