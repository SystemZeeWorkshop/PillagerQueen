package com.syszee.pillagerqueen.client.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.syszee.pillagerqueen.client.render.entity.PillagerQueenAnimations;
import com.syszee.pillagerqueen.common.entity.PillagerQueenEntity;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;

public class PillagerQueenModel extends HierarchicalModel<PillagerQueenEntity> {
    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();

    /** ANIMATIONS **/
    private final ModelPart root, head, leftLeg, rightLeg, leftArm, rightArm;

    public PillagerQueenModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("root").getChild("torso").getChild("head");
        this.leftLeg = root.getChild("root").getChild("leg_left");
        this.rightLeg = root.getChild("root").getChild("leg_right");
        this.leftArm = root.getChild("root").getChild("torso").getChild("arm_left");
        this.rightArm = root.getChild("root").getChild("torso").getChild("arm_right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 23.0F, 0.0F));
        PartDefinition skirt = root.addOrReplaceChild("skirt", CubeListBuilder.create().texOffs(55, 23).addBox(-4.0F, 0.5F, 0.0F, 8.0F, 7.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -12.5F, -2.0F));
        PartDefinition leg_right = root.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(112, 13).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 13.0F, 4.0F, new CubeDeformation(0.5F))
                .texOffs(39, 23).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));
        PartDefinition leg_left = root.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(112, 13).mirror().addBox(0.0F, 0.0F, -2.0F, 4.0F, 13.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false)
                .texOffs(39, 23).mirror().addBox(0.0F, 0.0F, -2.0F, 4.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -12.0F, 0.0F));
        PartDefinition torso = root.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(1, 23).addBox(-4.0F, -13.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, 0.0F));
        PartDefinition arm_left = torso.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(25, 23).addBox(0.0F, -2.5F, -2.5F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 44).mirror().addBox(0.0F, -2.5F, -2.5F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(4.0F, -10.5F, 0.5F));
        PartDefinition arm_left_shoulder_r1 = arm_left.addOrReplaceChild("arm_left_shoulder_r1", CubeListBuilder.create().texOffs(92, 23).mirror().addBox(0.0F, -3.0F, -3.0F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0873F));
        PartDefinition arm_left_sword = arm_left.addOrReplaceChild("arm_left_sword", CubeListBuilder.create().texOffs(56, 55).mirror().addBox(-1.0F, -1.0151F, -3.8732F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 39).mirror().addBox(-0.5F, -2.0151F, -25.8732F, 1.0F, 4.0F, 21.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, 8.9151F, -0.6268F));
        PartDefinition cube_r1 = arm_left_sword.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(65, 14).mirror().addBox(-1.0F, 0.0F, -0.8F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.001F)).mirror(false), PartPose.offsetAndRotation(0.0F, -0.0151F, -3.8732F, -0.1309F, 0.0F, 0.0F));
        PartDefinition cube_r2 = arm_left_sword.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(65, 14).mirror().addBox(-1.0F, -4.0F, -0.8F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.001F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0849F, -3.8732F, 0.1309F, 0.0F, 0.0F));
        PartDefinition cube_r3 = arm_left_sword.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(107, 31).mirror().addBox(5.0F, -1.6F, -1.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.5F, 0.3849F, -26.1732F, 0.7854F, 0.0F, 0.0F));
        PartDefinition arm_right = torso.addOrReplaceChild("arm_right", CubeListBuilder.create().texOffs(25, 23).mirror().addBox(-3.0F, -2.5F, -2.5F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 44).addBox(-3.0F, -2.5F, -2.5F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-4.0F, -10.5F, 0.5F));
        PartDefinition arm_right_shoulder_r1 = arm_right.addOrReplaceChild("arm_right_shoulder_r1", CubeListBuilder.create().texOffs(92, 23).addBox(-5.0F, -3.0F, -3.0F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0873F));
        PartDefinition arm_right_sword = arm_right.addOrReplaceChild("arm_right_sword", CubeListBuilder.create().texOffs(71, 26).addBox(-1.0F, -1.0151F, -3.8732F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 39).addBox(-0.5F, -2.0151F, -25.8732F, 1.0F, 4.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 8.9151F, -0.6268F));
        PartDefinition cube_r4 = arm_right_sword.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(65, 14).addBox(-1.0F, 0.0F, -0.8F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(0.0F, -0.0151F, -3.8732F, -0.1309F, 0.0F, 0.0F));
        PartDefinition cube_r5 = arm_right_sword.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(65, 14).addBox(-1.0F, -4.0F, -0.8F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(0.0F, 0.0849F, -3.8732F, 0.1309F, 0.0F, 0.0F));
        PartDefinition cube_r6 = arm_right_sword.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(107, 31).addBox(-6.0F, -1.6F, -1.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, 0.3849F, -26.1732F, 0.7854F, 0.0F, 0.0F));
        PartDefinition chest = torso.addOrReplaceChild("chest", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, -2.0F));
        PartDefinition chest_r1 = chest.addOrReplaceChild("chest_r1", CubeListBuilder.create().texOffs(88, 13).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.6545F, 0.0F, 0.0F));
        PartDefinition head = torso.addOrReplaceChild("head", CubeListBuilder.create().texOffs(23, 42).addBox(-2.6291F, -10.7333F, -3.8348F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(102, -13).addBox(1.3709F, -13.7333F, -3.8348F, 0.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.3709F, -12.2667F, -0.1652F));
        PartDefinition hair_extension_r1 = head.addOrReplaceChild("hair_extension_r1", CubeListBuilder.create().texOffs(70, 0).addBox(-9.0F, -39.0F, 0.0F, 7.0F, 15.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.8709F, 23.4667F, 0.1652F, 0.0F, -0.576F, 0.0F));
        PartDefinition hair = head.addOrReplaceChild("hair", CubeListBuilder.create().texOffs(1, 0).addBox(-16.0F, 0.0F, 0.0F, 32.0F, 13.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.3709F, -0.7333F, 4.1652F, 0.0873F, 0.0F, 0.0F));
        PartDefinition hair_tip = hair.addOrReplaceChild("hair_tip", CubeListBuilder.create().texOffs(1, 12).addBox(-16.0F, 0.0F, 0.0F, 32.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 13.0F, 0.0F, 0.1745F, 0.0F, 0.0F));
        PartDefinition helmet = head.addOrReplaceChild("helmet", CubeListBuilder.create(), PartPose.offsetAndRotation(1.3709F, 15.7167F, 10.8652F, -0.0436F, 0.0F, 0.0F));
        PartDefinition helmet_flair_right_r1 = helmet.addOrReplaceChild("helmet_flair_right_r1", CubeListBuilder.create().texOffs(47, 35).addBox(-5.2326F, -2.5F, 2.7326F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0461F, -24.1302F, -13.8635F, 0.7333F, -0.639F, -0.493F));
        PartDefinition helmet_flair_left_r1 = helmet.addOrReplaceChild("helmet_flair_left_r1", CubeListBuilder.create().texOffs(47, 35).mirror().addBox(4.3F, -30.3F, 3.6F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7333F, 0.639F, 0.493F));
        PartDefinition helmet_top_r1 = helmet.addOrReplaceChild("helmet_top_r1", CubeListBuilder.create().texOffs(83, 31).addBox(-7.4506F, -28.54F, -7.4506F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.3F, 9.8F, 1.1475F, -0.3897F, -0.7006F));
        PartDefinition helmet_r1 = helmet.addOrReplaceChild("helmet_r1", CubeListBuilder.create().texOffs(57, 36).addBox(-4.0F, -30.0F, -4.0F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.6F, 0.7333F, -0.639F, -0.493F));
        PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(65, 19).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.3709F, -3.7333F, -4.8348F));
        PartDefinition wing_right = torso.addOrReplaceChild("wing_right", CubeListBuilder.create().texOffs(10, 64).addBox(-11.0F, -7.0F, 0.0F, 11.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -11.0F, 2.0F, 0.0055F, 0.1747F, -0.0861F));
        PartDefinition wing_right_tip = wing_right.addOrReplaceChild("wing_right_tip", CubeListBuilder.create().texOffs(33, 93).addBox(-22.0F, -22.0F, 0.0F, 22.0F, 35.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, 1.0F, 0.0F, 0.0F, -0.829F, 0.0F));
        PartDefinition wing_left = torso.addOrReplaceChild("wing_left", CubeListBuilder.create().texOffs(10, 64).mirror().addBox(0.0F, -7.0F, 0.0F, 11.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, -11.0F, 2.0F, 0.0055F, -0.1747F, 0.0861F));
        PartDefinition wing_left_tip = wing_left.addOrReplaceChild("wing_left_tip", CubeListBuilder.create().texOffs(33, 93).mirror().addBox(0.0F, -22.0F, 0.0F, 22.0F, 35.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(11.0F, 1.0F, 0.0F, 0.0F, 0.829F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(PillagerQueenEntity pillagerQueen, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);

        /** HEAD ROTATION | LOOK AT PLAYER **/
        this.head.yRot = netHeadYaw * 0.017453292F;
        this.head.xRot = headPitch * 0.017453292F;

        KeyframeAnimations.animate(this, PillagerQueenAnimations.PILLAGERQUEEN_IDLE, (long) (50.0F * ageInTicks), 1.0F, ANIMATION_VECTOR_CACHE);

        /** LEG SWINGS **/
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
        this.rightLeg.yRot = 0.0F;
        this.rightLeg.zRot = 0.0F;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount * 0.5F;
        this.leftLeg.yRot = 0.0F;
        this.leftLeg.zRot = 0.0F;

        /** DEFAULT ARM SWING
        this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 2.0F * limbSwingAmount * 0.5F;
        this.rightArm.yRot = 0.0F;
        this.rightArm.zRot = 0.0F;
        this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        this.leftArm.yRot = 0.0F;
        this.leftArm.zRot = 0.0F;
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
        this.rightLeg.yRot = 0.0F;
        this.rightLeg.zRot = 0.0F;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount * 0.5F;
        this.leftLeg.yRot = 0.0F;
        this.leftLeg.zRot = 0.0F;
         **/

        this.animate(pillagerQueen.meleeAttackAnimationState, PillagerQueenAnimations.PILLAGERQUEEN_MELEE, ageInTicks);
        if(!pillagerQueen.meleeAttackAnimationState.isStarted()) this.animate(pillagerQueen.walkAnimationState, PillagerQueenAnimations.PILLAGERQUEEN_WALK, ageInTicks);
        if (pillagerQueen.meleeAttackAnimationState.getAccumulatedTime() / 1000.0F >= PillagerQueenAnimations.PILLAGERQUEEN_MELEE.lengthInSeconds()) pillagerQueen.meleeAttackAnimationState.stop();


    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return root;
    }

}