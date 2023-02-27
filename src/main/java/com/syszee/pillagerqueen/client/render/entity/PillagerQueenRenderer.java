package com.syszee.pillagerqueen.client.render.entity;

import com.syszee.pillagerqueen.client.render.model.ModModelLayers;
import com.syszee.pillagerqueen.client.render.model.PillagerQueenModel;
import com.syszee.pillagerqueen.common.entity.PillagerQueenEntity;
import com.syszee.pillagerqueen.core.ModMain;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PillagerQueenRenderer extends MobRenderer<PillagerQueenEntity, PillagerQueenModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModMain.MOD_ID, "textures/entity/pillagerqueen.png");

    public PillagerQueenRenderer(EntityRendererProvider.Context context) {
        super(context, new PillagerQueenModel(context.bakeLayer(ModModelLayers.PILLAGER_QUEEN)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(PillagerQueenEntity entity) {
        return TEXTURE;
    }
}
