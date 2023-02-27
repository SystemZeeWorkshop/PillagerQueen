package com.syszee.example.common.block.entity;

import com.syszee.example.core.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ExampleBlockEntity extends BlockEntity {
    public ExampleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.EXAMPLE_BLOCK_ENTITY, blockPos, blockState);
    }
}
