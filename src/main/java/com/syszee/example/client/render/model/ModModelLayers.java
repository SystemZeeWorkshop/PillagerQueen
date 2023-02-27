package com.syszee.example.client.render.model;

import com.syszee.example.core.ModMain;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation EXAMPLE = register("example", LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));

    public static void init() {
    }

    private static ModelLayerLocation register(String name, LayerDefinition definition) {
        return register(name, "main", definition);
    }

    private static ModelLayerLocation register(String name, String layer, LayerDefinition definition) {
        ModelLayerLocation location = new ModelLayerLocation(new ResourceLocation(ModMain.MOD_ID, name), layer);
        EntityModelLayerRegistry.registerModelLayer(location, () -> definition);
        return location;
    }
}
