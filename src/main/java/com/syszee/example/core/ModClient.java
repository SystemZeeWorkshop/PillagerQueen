package com.syszee.example.core;

import com.syszee.example.client.render.entity.ExampleEntityRenderer;
import com.syszee.example.client.render.model.ModModelLayers;
import com.syszee.example.core.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModModelLayers.init();
        EntityRendererRegistry.register(ModEntities.EXAMPLE, ExampleEntityRenderer::new);
    }
}
