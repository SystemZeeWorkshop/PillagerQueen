package com.syszee.pillagerqueen.common.entity;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

public class PillagerQueenEntity extends Monster {

    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState meleeAttackAnimationState = new AnimationState();
    public PillagerQueenEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 15.0F, 1.0F));
        this.goalSelector.addGoal(4, new QueenMeleeAttackGoal(this, 1.0F, false));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[]{Raider.class})).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, ArmorStand.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.35).add(Attributes.FOLLOW_RANGE, 12.0).add(Attributes.MAX_HEALTH, 48.0);
    }

    @Override
    public void tick() {
        super.tick();
        handleAnimations();
    }

    public void handleAnimations(){

        if(this.isAggressive()){
            this.walkAnimationState.startIfStopped(this.tickCount);
        }else this.walkAnimationState.stop();

    }

    @Override
    public void handleEntityEvent(byte b) {
        if(b == 98){
            this.meleeAttackAnimationState.start(this.tickCount);
        }else{
            super.handleEntityEvent(b);
        }
    }

    class QueenMeleeAttackGoal extends Goal {
        protected final PathfinderMob mob;
        private final double speedModifier;
        private final boolean followingTargetEvenIfNotSeen;
        private Path path;
        private double pathedTargetX;
        private double pathedTargetY;
        private double pathedTargetZ;
        private int ticksUntilNextPathRecalculation;
        private int ticksUntilNextAttack;
        private final int attackInterval = 20;
        private long lastCanUseCheck;
        private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;
        private int damageTimer = -1;

        public QueenMeleeAttackGoal(PathfinderMob pathfinderMob, double d, boolean bl) {
            this.mob = pathfinderMob;
            this.speedModifier = d;
            this.followingTargetEvenIfNotSeen = bl;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            long l = this.mob.level.getGameTime();

            // IF HEALTH IS GREATER THAN 24.0F (12 HEARTS) DON'T DO THIS
            if(this.mob.getHealth() > (this.mob.getMaxHealth()/2)){
                return false;
            }else{
                if (l - this.lastCanUseCheck < 20L) {
                    return false;
                } else {
                    this.lastCanUseCheck = l;
                    LivingEntity livingEntity = this.mob.getTarget();
                    if (livingEntity == null) {
                        return false;
                    } else if (!livingEntity.isAlive()) {
                        return false;
                    } else {
                        this.path = this.mob.getNavigation().createPath(livingEntity, 0);
                        if (this.path != null) {
                            return true;
                        } else {
                            return this.getAttackReachSqr(livingEntity) >= this.mob.distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                        }
                    }
                }
            }

        }

        public boolean canContinueToUse() {
            LivingEntity livingEntity = this.mob.getTarget();
            if (livingEntity == null) {
                return false;
            } else if (!livingEntity.isAlive()) {
                return false;
            } else if (!this.followingTargetEvenIfNotSeen) {
                return !this.mob.getNavigation().isDone();
            } else if (!this.mob.isWithinRestriction(livingEntity.blockPosition())) {
                return false;
            } else {
                return !(livingEntity instanceof Player) || !livingEntity.isSpectator() && !((Player)livingEntity).isCreative();
            }
        }

        public void start() {
            this.mob.getNavigation().moveTo(this.path, this.speedModifier);
            this.mob.setAggressive(true);
            this.ticksUntilNextPathRecalculation = 0;
            this.ticksUntilNextAttack = 0;
        }

        public void stop() {
            LivingEntity livingEntity = this.mob.getTarget();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
                this.mob.setTarget((LivingEntity)null);
            }

            this.mob.setAggressive(false);
            this.mob.getNavigation().stop();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingEntity = this.mob.getTarget();
            if (livingEntity != null) {
                this.mob.getLookControl().setLookAt(livingEntity, 30.0F, 30.0F);
                double d = this.mob.distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
                if ((this.followingTargetEvenIfNotSeen || this.mob.getSensing().hasLineOfSight(livingEntity)) && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0 || livingEntity.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0 || this.mob.getRandom().nextFloat() < 0.05F)) {
                    this.pathedTargetX = livingEntity.getX();
                    this.pathedTargetY = livingEntity.getY();
                    this.pathedTargetZ = livingEntity.getZ();
                    this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                    if (d > 1024.0) {
                        this.ticksUntilNextPathRecalculation += 10;
                    } else if (d > 256.0) {
                        this.ticksUntilNextPathRecalculation += 5;
                    }

                    if (!this.mob.getNavigation().moveTo(livingEntity, this.speedModifier)) {
                        this.ticksUntilNextPathRecalculation += 15;
                    }

                    this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
                }

                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
                this.checkAndPerformAttack(livingEntity, d);
            }
        }

        protected void checkAndPerformAttack(LivingEntity livingEntity, double d) {
            this.damageTimer--;
            double e = this.getAttackReachSqr(livingEntity);
            if (d <= e) {
                if(this.damageTimer < 0 && this.getTicksUntilNextAttack() <= 0){
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    this.mob.getLevel().broadcastEntityEvent(this.mob, (byte) 98);
                    this.damageTimer = 3;
                    ticksUntilNextAttack = 40;
                }else if(this.damageTimer == 0) {
                    this.mob.doHurtTarget(livingEntity);
                    this.mob.getTarget().knockback(2.5F, 5.5F, 2.5F);
                    this.resetAttackCooldown();
                }
            }

        }

        protected void resetAttackCooldown() {
            this.ticksUntilNextAttack = this.adjustedTickDelay(20);
        }

        protected boolean isTimeToAttack() {
            return this.ticksUntilNextAttack <= 0;
        }

        protected int getTicksUntilNextAttack() {
            return this.ticksUntilNextAttack;
        }

        protected int getAttackInterval() {
            return this.adjustedTickDelay(20);
        }

        protected double getAttackReachSqr(LivingEntity livingEntity) {
            return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + livingEntity.getBbWidth());
        }
    }



}
