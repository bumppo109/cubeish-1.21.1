package com.bumppo109.cubeish.block.shapes.flammable;

import com.bumppo109.cubeish.block.shapes.base.CornerSlabBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class FlammableCornerSlabBlock extends CornerSlabBlock {
    private final int flammability;
    private final int fireSpeed;

    public FlammableCornerSlabBlock(Properties properties, int flammability, int fireSpeed) {
        super(properties);
        this.flammability = flammability;
        this.fireSpeed = fireSpeed;
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return flammability;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return fireSpeed;
    }
}
