package com.syszee.pillagerqueen.core.mixin;

import com.syszee.pillagerqueen.common.entity.PillagerQueenEntity;
import com.syszee.pillagerqueen.core.registry.ModEntities;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Raid.class)
public class RaidMixin {

    @Inject(method = "spawnGroup", at = @At("TAIL"))
    void spawnGroup(BlockPos blockPos, CallbackInfo ci){
        Raid raid = Raid.class.cast(this);
        if(raid.getGroupsSpawned() == raid.getNumGroups(raid.getLevel().getDifficulty())){
            PillagerQueenEntity queen = new PillagerQueenEntity(ModEntities.PILLAGER_QUEEN, raid.getLevel());
            joinRaid(raid.getGroupsSpawned(), queen, blockPos, false);
        }
    }

    public void joinRaid(int i, Raider raider, @Nullable BlockPos blockPos, boolean bl) {
        Raid raid = Raid.class.cast(this);
        boolean bl2 = raid.addWaveMob(i, raider, true);
        if (bl2) {
            raider.setCurrentRaid(raid);
            raider.setWave(i);
            raider.setCanJoinRaid(true);
            raider.setTicksOutsideRaid(0);
            if (!bl && blockPos != null) {
                raider.setPos((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 1.0, (double)blockPos.getZ() + 0.5);
                raider.finalizeSpawn((ServerLevelAccessor) raid.getLevel(), raid.getLevel().getCurrentDifficultyAt(blockPos), MobSpawnType.EVENT, null, null);
                raider.applyRaidBuffs(i, false);
                raider.setOnGround(true);
                ((ServerLevelAccessor) raid.getLevel()).addFreshEntityWithPassengers(raider);
            }
        }
    }
}
