package com.syszee.pillagerqueen.core;

import com.syszee.pillagerqueen.client.render.entity.ExampleEntityRenderer;
import com.syszee.pillagerqueen.client.render.entity.PillagerQueenRenderer;
import com.syszee.pillagerqueen.client.render.model.ModModelLayers;
import com.syszee.pillagerqueen.core.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModModelLayers.init();
        EntityRendererRegistry.register(ModEntities.EXAMPLE, ExampleEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.PILLAGER_QUEEN, PillagerQueenRenderer::new);
    }
}
