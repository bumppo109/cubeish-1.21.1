package com.bumppo109.cubeish.block;

import com.bumppo109.cubeish.block.shapes.ice.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Locale;
import java.util.function.Function;

public enum IceShape {

    SLAB(IceSlabBlock::new),
    STAIRS(properties -> new IceStairBlock(
            Blocks.ICE.defaultBlockState(), properties)),
    WALL(IceWallBlock::new),
    VERTICAL_SLAB(IceVerticalSlabBlock::new),
    QUARTER(IceQuarterBlock::new),
    VERTICAL_QUARTER(IceVerticalQuarterBlock::new),
    CORNER_SLAB(IceCornerSlabBlock::new),
    VERTICAL_CORNER_SLAB(IceVerticalCornerSlabBlock::new),
    VERTICAL_CORNER(IceVerticalCornerBlock::new),
    EIGHTH(IceEighthBlock::new);

    private final Function<BlockBehaviour.Properties, Block> factory;

    IceShape(Function<BlockBehaviour.Properties, Block> factory) {
        this.factory = factory;
    }

    public Block create() {
        return factory.apply(
                BlockBehaviour.Properties.ofFullCopy(Blocks.ICE)
        );
    }

    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}