package com.bumppo109.cubeish.block;

import com.bumppo109.cubeish.block.shapes.base.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Locale;
import java.util.function.BiFunction;

public enum ExtendedShape {

    BLOCK((base, properties) -> new Block(properties)),

    SLAB((base, properties) -> new SlabBlock(properties)),

    STAIRS((base, properties) -> new StairBlock(
            base.defaultBlockState(),
            properties
    )),

    WALL((base, properties) -> new WallBlock(properties)),

    VERTICAL_SLAB((base, properties) -> new VerticalSlabBlock(properties)),

    QUARTER((base, properties) -> new QuarterBlock(properties)),

    VERTICAL_QUARTER((base, properties) -> new VerticalQuarterBlock(properties)),

    CORNER_SLAB((base, properties) -> new CornerSlabBlock(properties)),

    VERTICAL_CORNER_SLAB((base, properties) -> new VerticalCornerSlabBlock(properties)),

    VERTICAL_CORNER((base, properties) -> new VerticalCornerBlock(properties)),

    EIGHTH((base, properties) -> new EighthBlock(properties));

    private final BiFunction<Block, BlockBehaviour.Properties, Block> blockFactory;

    ExtendedShape(
            BiFunction<Block, BlockBehaviour.Properties, Block> blockFactory
    ) {
        this.blockFactory = blockFactory;
    }

    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public Block create(
            Block base,
            BlockBehaviour.Properties properties
    ) {
        return blockFactory.apply(base, properties);
    }

    public BlockItem createBlockItem(
            Block block,
            Item.Properties properties
    ) {
        return new BlockItem(block, properties);
    }
}