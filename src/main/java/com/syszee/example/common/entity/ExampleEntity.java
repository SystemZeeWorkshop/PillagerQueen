package com.syszee.example.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class ExampleEntity extends Mob {
    public ExampleEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }
}
