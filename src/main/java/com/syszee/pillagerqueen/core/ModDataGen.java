package com.syszee.pillagerqueen.core;

import com.syszee.pillagerqueen.core.datagen.LanguageGen;
import com.syszee.pillagerqueen.core.datagen.ModelGen;
import com.syszee.pillagerqueen.core.datagen.RecipeGen;
import com.syszee.pillagerqueen.core.datagen.loot.BlockLootGen;
import com.syszee.pillagerqueen.core.datagen.loot.EntityLootGen;
import com.syszee.pillagerqueen.core.datagen.tag.BlockTagGen;
import com.syszee.pillagerqueen.core.datagen.tag.EntityTypeTagGen;
import com.syszee.pillagerqueen.core.datagen.tag.ItemTagGen;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ModDataGen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
        BlockTagGen blockTagGen = new BlockTagGen(dataGenerator);
        dataGenerator.addProvider(blockTagGen);
        dataGenerator.addProvider(new ItemTagGen(dataGenerator, blockTagGen));
        dataGenerator.addProvider(new EntityTypeTagGen(dataGenerator));

        dataGenerator.addProvider(new LanguageGen(dataGenerator));
        dataGenerator.addProvider(new RecipeGen(dataGenerator));

        dataGenerator.addProvider(new BlockLootGen(dataGenerator));
        dataGenerator.addProvider(new EntityLootGen(dataGenerator));

        dataGenerator.addProvider(new ModelGen(dataGenerator));
    }
}
