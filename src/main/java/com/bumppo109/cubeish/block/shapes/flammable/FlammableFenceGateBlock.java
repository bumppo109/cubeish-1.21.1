package com.bumppo109.cubeish.block.shapes.flammable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class FlammableFenceGateBlock extends FenceGateBlock {
    private final int flammability;
    private final int fireSpeed;

    public FlammableFenceGateBlock(Properties properties, SoundEvent openSound, SoundEvent closeSound, int flammability, int fireSpeed) {
        super(properties, openSound, closeSound);
        this.flammability = flammability;
        this.fireSpeed = fireSpeed;
    }

    public FlammableFenceGateBlock(WoodType woodType, Properties properties, int flammability, int fireSpeed) {
        super(woodType, properties);
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
