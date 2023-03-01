package com.syszee.pillagerqueen.common.entity;

import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class PillagerQueenEntity extends Raider {

    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState meleeAttackAnimationState = new AnimationState();
    public final AnimationState floatingAnimationState = new AnimationState();
    public final AnimationState fallingAnimationState = new AnimationState();
    private static final EntityDataAccessor<Boolean> HAS_SPAWNED_PATROL = SynchedEntityData.defineId(PillagerQueenEntity.class, EntityDataSerializers.BOOLEAN);;
    public PillagerQueenEntity(EntityType<? extends Raider> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 15.0F, 1.0F));
        this.goalSelector.addGoal(4, new QueenMeleeAttackGoal(this, this, 1.0F, false));
        this.goalSelector.addGoal(3, new QueenFlyingGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[]{Raider.class})).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, ArmorStand.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.45)
                .add(Attributes.FOLLOW_RANGE, 64.0)
                .add(Attributes.MAX_HEALTH, 48.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8F)
                .add(Attributes.ATTACK_DAMAGE, 6.0F);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(HAS_SPAWNED_PATROL, false);
    }

    @Override
    public void applyRaidBuffs(int i, boolean bl) {

    }

    @Override
    public void tick() {
        super.tick();
        handleAnimations();

        Random r = new Random();

        /**
         for(int i = 0; i < 360; i++){
         if(i % 2 == 0){
         this.getLevel().addParticle(ParticleTypes.POOF,
         this.getX() + Math.cos(i), this.getY(), this.getZ() + Math.sin(i),
         Math.cos(i), 0.0, Math.sin(i));
         }
         }
         **/
    }

    @Override
    public int getExperienceReward() {
        this.xpReward = 40;
        return super.getExperienceReward();
    }

    public void handleAnimations(){

        if(this.isAggressive()){
            this.walkAnimationState.startIfStopped(this.tickCount);
            this.spawnSprintParticle();
        }else this.walkAnimationState.stop();

        if(!this.isOnGround() && !this.isNoGravity() && !this.isAggressive()){
            this.fallingAnimationState.startIfStopped(this.tickCount);
        }else this.fallingAnimationState.stop();

    }

    @Override
    public void handleEntityEvent(byte b) {
        if(b == 97){ // ATTACK FROM MELEE
            for(int i = 0; i < 10; i++){
                this.level.addParticle(ParticleTypes.CRIT,
                        this.getRandomX(4), this.getRandomY(), this.getRandomZ(4),
                        2, 2, 2);
            }
        }else if(b == 98){ // START RUNNING/MELEE
            this.meleeAttackAnimationState.start(this.tickCount);
        }else if(b == 99){ // FLOATING SHOULD START
            this.floatingAnimationState.startIfStopped(this.tickCount);
        }else if(b == 100) { // FLOATING SHOULD STOP
            this.floatingAnimationState.stop();
        }else if(b == 101) { // SPAWN PATROL EFFECT

            // 360 POOF
            for(int i = 0; i < 360; i++){
                if(i % 2 == 0){
                    this.getLevel().addParticle(ParticleTypes.LARGE_SMOKE,
                            this.getX() + Math.cos(i), this.getY(), this.getZ() + Math.sin(i),
                            Math.cos(i), -0.25, Math.sin(i));
                }
            }

            // AMBUSH REVEAL
            for(AbstractIllager p : level.getNearbyEntities(AbstractIllager.class, TargetingConditions.DEFAULT, this, this.getBoundingBox().inflate(25.0D))){
                for(int i = 0; i < 32; i++){

                    // POOF
                    this.getLevel().addParticle(ParticleTypes.POOF,
                            p.getRandomX(3), p.getY(), p.getRandomZ(3),
                            0.25D, 0.25D, 0.25D);

                }
            }

        }else {
            super.handleEntityEvent(b);
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {

            // SHOULD WE SPAWN A PATROL
            if ((!this.level.isClientSide)
                    && !hasSpawnedPatrol()
                    && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)
                    && this.level.getGameRules().getBoolean(GameRules.RULE_DO_PATROL_SPAWNING)){

                    ServerLevel serverLevel = (ServerLevel)this.level;
                    int targetY = this.getLevel().getHeight(Heightmap.Types.WORLD_SURFACE, this.getBlockX(), this.getBlockZ());
                    BlockPos blockPos = new BlockPos(this.getRandomX(10), targetY, this.getRandomZ(10));

                    // SPAWN LEADER
                    PatrollingMonster patrollingLeader = (PatrollingMonster)EntityType.PILLAGER.create(serverLevel);
                    if (patrollingLeader != null) {
                        patrollingLeader.setPatrolLeader(true);
                        patrollingLeader.findPatrolTarget();
                    }
                    patrollingLeader.setPos((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ());
                    patrollingLeader.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(blockPos), MobSpawnType.PATROL, (SpawnGroupData)null, (CompoundTag)null);
                    serverLevel.addFreshEntityWithPassengers(patrollingLeader);
                    setHasSpawnedPatrol(true);

                    // SPAWN SUBORDINATES
                    for(int i = 0; i < 3; i++){
                        blockPos = new BlockPos(this.getRandomX(10), targetY, this.getRandomZ(10));
                        PatrollingMonster patrollingMonster = (PatrollingMonster)EntityType.PILLAGER.create(serverLevel);
                        patrollingMonster.setPos((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ());
                        patrollingMonster.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(blockPos), MobSpawnType.PATROL, (SpawnGroupData)null, (CompoundTag)null);
                        serverLevel.addFreshEntityWithPassengers(patrollingMonster);

                        // SOUND: AMBUSH APPEAR CELEBRATE
                        this.getLevel().playSound(null,
                                patrollingMonster.getX(), patrollingMonster.getY(), patrollingMonster.getZ(),
                                SoundEvents.PILLAGER_CELEBRATE,
                                SoundSource.HOSTILE,
                                1.0F, 1.0F);

                    }

                    // SOUND: AMBUSH
                    this.getLevel().playSound(null,
                            getX(), getY(), getZ(),
                            SoundEvents.ANVIL_LAND,
                            SoundSource.HOSTILE,
                            1.0F, 1.0F);

                    // PARTICLE BROADCAST
                    this.getLevel().broadcastEntityEvent(this, (byte) 101);

            }

        return super.hurt(damageSource, f);
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return null;
    }

    public void setHasSpawnedPatrol(boolean b){
        this.getEntityData().set(HAS_SPAWNED_PATROL, b);
    }

    public boolean hasSpawnedPatrol(){
        return (Boolean)this.getEntityData().get(HAS_SPAWNED_PATROL);
    }

    class QueenMeleeAttackGoal extends Goal {
        private final PillagerQueenEntity pillagerQueen;
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

        public QueenMeleeAttackGoal(PillagerQueenEntity pillagerQueen, PathfinderMob pathfinderMob, double d, boolean bl) {
            this.pillagerQueen = pillagerQueen;
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

                    //playSound(SoundEvents.ANVIL_LAND, 1.0F, 1.0F);

                    this.mob.getLevel().broadcastEntityEvent(this.mob, (byte) 97);
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

    class QueenFlyingGoal extends Goal {

        private final PillagerQueenEntity pillagerQueen;
        Random r = new Random();

        public QueenFlyingGoal(PillagerQueenEntity pillagerQueen) {
            this.pillagerQueen = pillagerQueen;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if(pillagerQueen.getHealth() > pillagerQueen.getMaxHealth()/2){
                return true;
            }else{
                return false;
            }
        }

        @Override
        public void start() {
            super.start();
            pillagerQueen.getLevel().broadcastEntityEvent(pillagerQueen, (byte) 99);
        }

        @Override
        public void stop() {
            super.stop();
            pillagerQueen.setNoGravity(false);
            pillagerQueen.getLevel().broadcastEntityEvent(pillagerQueen, (byte) 100);
        }

        @Override
        public void tick() {
            super.tick();

            pillagerQueen.setNoGravity(true);

            int targetY = pillagerQueen.getLevel().getHeight(Heightmap.Types.WORLD_SURFACE, pillagerQueen.getBlockX(), pillagerQueen.getBlockZ());
            float maxFloatHeight = (pillagerQueen.getHealth()/4); // 12
            int goalHeight = (int) (targetY + maxFloatHeight);
            BlockPos blockPos = new BlockPos(pillagerQueen.getX(), pillagerQueen.getY(), pillagerQueen.getZ());

            if((pillagerQueen.getY() < goalHeight) && pillagerQueen.level.isEmptyBlock(blockPos.above(2))){
                pillagerQueen.moveTo(pillagerQueen.getX(), pillagerQueen.getY() + 0.5F, pillagerQueen.getZ());
            }else if((pillagerQueen.getY() > goalHeight) && pillagerQueen.level.isEmptyBlock(blockPos)){
                pillagerQueen.moveTo(pillagerQueen.getX(), pillagerQueen.getY() - 1F, pillagerQueen.getZ());
            }

            LivingEntity livingEntity = pillagerQueen.getTarget();
            if(livingEntity != null){
                pillagerQueen.getLookControl().setLookAt(livingEntity, 30.0F, 30.0F);

                Vec3 vec3 = pillagerQueen.getTarget().getEyePosition();
                AABB aabb = pillagerQueen.getBoundingBox().inflate(15.0);

                if(!aabb.contains(vec3)) pillagerQueen.moveControl.setWantedPosition(livingEntity.getX(), pillagerQueen.getY(), livingEntity.getZ(), 8.0);
            }

        }

    }


}
