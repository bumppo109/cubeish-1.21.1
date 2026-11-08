package com.bumppo109.cubeish.block.shapes.friction;

import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

public class FrictionStairBlock extends StairBlock implements IFrictionBlock {

    public FrictionStairBlock(BlockState baseState, Properties properties) {
        super(baseState, properties);
    }
}
