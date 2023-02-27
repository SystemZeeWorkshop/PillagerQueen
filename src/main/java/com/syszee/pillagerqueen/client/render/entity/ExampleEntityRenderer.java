package com.syszee.pillagerqueen.client.render.entity;

import com.syszee.pillagerqueen.client.render.model.ModModelLayers;
import com.syszee.pillagerqueen.common.entity.ExampleEntity;
import com.syszee.pillagerqueen.core.ModMain;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ExampleEntityRenderer extends MobRenderer<ExampleEntity, HumanoidModel<ExampleEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModMain.MOD_ID, "textures/entity/pillagerqueen.png");

    public ExampleEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModModelLayers.EXAMPLE)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(ExampleEntity entity) {
        return TEXTURE;
    }
}
