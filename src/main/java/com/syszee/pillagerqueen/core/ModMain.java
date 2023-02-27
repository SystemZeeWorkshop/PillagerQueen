package com.syszee.pillagerqueen.core;

import com.syszee.pillagerqueen.core.registry.ModBlocks;
import com.syszee.pillagerqueen.core.registry.ModEntities;
import com.syszee.pillagerqueen.core.registry.ModItems;
import com.syszee.pillagerqueen.core.registry.ModSounds;
import net.fabricmc.api.ModInitializer;

public class ModMain implements ModInitializer {
    public static final String MOD_ID = "pillagerqueen";

    public void onInitialize() {
        ModItems.init();
        ModBlocks.init();
        ModBlocks.init();
        ModEntities.init();
        ModSounds.init();
    }
}
