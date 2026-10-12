package com.brilliafy.magicstorage.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockHellBricks extends Block {

    public BlockHellBricks(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.fireImmune()) {
            entity.setRemainingFireTicks(60); // 3 seconds of fire
        }
        super.stepOn(level, pos, state, entity);
    }
}
