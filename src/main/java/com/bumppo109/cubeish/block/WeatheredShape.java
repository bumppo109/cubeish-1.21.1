
package com.bumppo109.cubeish.block;

import com.bumppo109.cubeish.block.shapes.weathering.*;
import com.bumppo109.cubeish.util.function.TriFunction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Locale;

public enum WeatheredShape {

    SLAB((base, properties, weatherState) ->
            new WeatheringCopperSlabBlock(weatherState, properties)),

    STAIRS((base, properties, weatherState) ->
            new WeatheringCopperStairBlock(
                    weatherState,
                    base.defaultBlockState(),
                    properties
            )),

    WALL((base, properties, weatherState) ->
            new WeatheringWallBlock(properties, weatherState)),

    VERTICAL_SLAB((base, properties, weatherState) ->
            new WeatheringVerticalSlabBlock(properties, weatherState)),

    QUARTER((base, properties, weatherState) ->
            new WeatheringQuarterBlock(properties, weatherState)),

    VERTICAL_QUARTER((base, properties, weatherState) ->
            new WeatheringVerticalQuarterBlock(properties, weatherState)),

    CORNER_SLAB((base, properties, weatherState) ->
            new WeatheringCornerSlabBlock(properties, weatherState)),

    VERTICAL_CORNER_SLAB((base, properties, weatherState) ->
            new WeatheringVerticalCornerSlabBlock(properties, weatherState)),

    VERTICAL_CORNER((base, properties, weatherState) ->
            new WeatheringVerticalCornerBlock(properties, weatherState)),

    EIGHTH((base, properties, weatherState) ->
            new WeatheringEighthBlock(properties, weatherState));

    private final TriFunction<
            Block,
            BlockBehaviour.Properties,
            WeatheringCopper.WeatherState,
            Block
            > blockFactory;

    WeatheredShape(
            TriFunction<
                    Block,
                    BlockBehaviour.Properties,
                    WeatheringCopper.WeatherState,
                    Block
                    > blockFactory
    ) {
        this.blockFactory = blockFactory;
    }

    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public Block create(
            Block base,
            BlockBehaviour.Properties properties,
            WeatheringCopper.WeatherState weatherState
    ) {
        return blockFactory.apply(base, properties, weatherState);
    }

    public BlockItem createBlockItem(
            Block block,
            Item.Properties properties
    ) {
        return new BlockItem(block, properties);
    }
}
