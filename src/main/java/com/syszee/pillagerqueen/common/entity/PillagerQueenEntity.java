package com.syszee.pillagerqueen.common.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PillagerQueenEntity extends Monster {

    public PillagerQueenEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 15.0F, 1.0F));
    }

    /** ANIMATION **/
    private float prevMoveAnimationWeight, moveAnimationWeight;

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.FOLLOW_RANGE, 12.0).add(Attributes.MAX_HEALTH, 24.0);
    }

    @Override
    public void tick() {
        super.tick();

        if(!this.level.isClientSide) {

        } else {
            this.prevMoveAnimationWeight = this.moveAnimationWeight;
            if (this.animationSpeed >= 0.05F) {
                this.moveAnimationWeight = Math.min(this.moveAnimationWeight + 0.1F, Math.min(this.animationSpeed * 2.0F, 1.0F));
            } else {
                this.moveAnimationWeight = Math.max(this.moveAnimationWeight - 0.1F, 0.0F);
            }
        }

    }

    public float getMoveAnimationWeight(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevMoveAnimationWeight, this.moveAnimationWeight);
    }


}
