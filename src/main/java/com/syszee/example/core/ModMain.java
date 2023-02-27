package com.syszee.example.core;

import com.syszee.example.core.registry.ModBlocks;
import com.syszee.example.core.registry.ModEntities;
import com.syszee.example.core.registry.ModItems;
import com.syszee.example.core.registry.ModSounds;
import net.fabricmc.api.ModInitializer;

public class ModMain implements ModInitializer {
    public static final String MOD_ID = "example";

    public void onInitialize() {
        ModItems.init();
        ModBlocks.init();
        ModBlocks.init();
        ModEntities.init();
        ModSounds.init();
    }
}
