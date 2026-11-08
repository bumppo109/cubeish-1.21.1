package com.bumppo109.cubeish.block;

import com.bumppo109.cubeish.block.shapes.magma.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Locale;
import java.util.function.Function;

public enum MagmaShape {
    SLAB(MagmaSlabBlock::new),
    STAIRS(properties -> new MagmaStairsBlock(
            Blocks.MAGMA_BLOCK.defaultBlockState(), properties)),
    WALL(MagmaWallBlock::new),

    VERTICAL_SLAB(MagmaVerticalSlabBlock::new),
    QUARTER(MagmaQuarterBlock::new),
    VERTICAL_QUARTER(MagmaVerticalQuarterBlock::new),
    CORNER_SLAB(MagmaCornerSlabBlock::new),
    VERTICAL_CORNER_SLAB(MagmaVerticalCornerSlabBlock::new),
    VERTICAL_CORNER(MagmaVerticalCornerBlock::new),
    EIGHTH(MagmaEighthBlock::new);

    private final Function<BlockBehaviour.Properties, Block> factory;

    MagmaShape(Function<BlockBehaviour.Properties, Block> factory) {
        this.factory = factory;
    }

    public Block create() {
        return factory.apply(
                BlockBehaviour.Properties.ofFullCopy(Blocks.MAGMA_BLOCK)
        );
    }

    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}