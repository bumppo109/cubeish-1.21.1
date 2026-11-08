package com.bumppo109.cubeish.block.families;

import com.bumppo109.cubeish.block.ExtendedShape;
import com.bumppo109.cubeish.block.WaxingState;
import com.bumppo109.cubeish.block.WeatheringState;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public enum Weathering {

    COPPER(
            Map.of(
                    WeatheringState.UNAFFECTED, Blocks.COPPER_BLOCK,
                    WeatheringState.EXPOSED, Blocks.EXPOSED_COPPER,
                    WeatheringState.WEATHERED, Blocks.WEATHERED_COPPER,
                    WeatheringState.OXIDIZED, Blocks.OXIDIZED_COPPER
            ),
            Map.of(
                    WeatheringState.UNAFFECTED, Blocks.WAXED_COPPER_BLOCK,
                    WeatheringState.EXPOSED, Blocks.WAXED_EXPOSED_COPPER,
                    WeatheringState.WEATHERED, Blocks.WAXED_WEATHERED_COPPER,
                    WeatheringState.OXIDIZED, Blocks.WAXED_OXIDIZED_COPPER
            )
    ),

    CUT_COPPER(
            Map.of(
                    WeatheringState.UNAFFECTED, Blocks.CUT_COPPER,
                    WeatheringState.EXPOSED, Blocks.EXPOSED_CUT_COPPER,
                    WeatheringState.WEATHERED, Blocks.WEATHERED_CUT_COPPER,
                    WeatheringState.OXIDIZED, Blocks.OXIDIZED_CUT_COPPER
            ),
            Map.of(
                    WeatheringState.UNAFFECTED, Blocks.WAXED_CUT_COPPER,
                    WeatheringState.EXPOSED, Blocks.WAXED_EXPOSED_CUT_COPPER,
                    WeatheringState.WEATHERED, Blocks.WAXED_WEATHERED_CUT_COPPER,
                    WeatheringState.OXIDIZED, Blocks.WAXED_OXIDIZED_CUT_COPPER
            )
    ),

    CHISELED_COPPER(
            Map.of(
                    WeatheringState.UNAFFECTED, Blocks.CHISELED_COPPER,
                    WeatheringState.EXPOSED, Blocks.EXPOSED_CHISELED_COPPER,
                    WeatheringState.WEATHERED, Blocks.WEATHERED_CHISELED_COPPER,
                    WeatheringState.OXIDIZED, Blocks.OXIDIZED_CHISELED_COPPER
            ),
            Map.of(
                    WeatheringState.UNAFFECTED, Blocks.WAXED_CHISELED_COPPER,
                    WeatheringState.EXPOSED, Blocks.WAXED_EXPOSED_CHISELED_COPPER,
                    WeatheringState.WEATHERED, Blocks.WAXED_WEATHERED_CHISELED_COPPER,
                    WeatheringState.OXIDIZED, Blocks.WAXED_OXIDIZED_CHISELED_COPPER
            )
    ),

    COPPER_GRATE(
            Map.of(
                    WeatheringState.UNAFFECTED, Blocks.COPPER_GRATE,
                    WeatheringState.EXPOSED, Blocks.EXPOSED_COPPER_GRATE,
                    WeatheringState.WEATHERED, Blocks.WEATHERED_COPPER_GRATE,
                    WeatheringState.OXIDIZED, Blocks.OXIDIZED_COPPER_GRATE
            ),
            Map.of(
                    WeatheringState.UNAFFECTED, Blocks.WAXED_COPPER_GRATE,
                    WeatheringState.EXPOSED, Blocks.WAXED_EXPOSED_COPPER_GRATE,
                    WeatheringState.WEATHERED, Blocks.WAXED_WEATHERED_COPPER_GRATE,
                    WeatheringState.OXIDIZED, Blocks.WAXED_OXIDIZED_COPPER_GRATE
            )
    );

    private final Map<WeatheringState, Block> unwaxedBlocks;
    private final Map<WeatheringState, Block> waxedBlocks;

    Weathering(
            Map<WeatheringState, Block> unwaxedBlocks,
            Map<WeatheringState, Block> waxedBlocks
    ) {
        this.unwaxedBlocks = new EnumMap<>(unwaxedBlocks);
        this.waxedBlocks = new EnumMap<>(waxedBlocks);
    }

    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public Block getBase(WeatheringState state, WaxingState waxing) {
        return (waxing.isWaxed() ? waxedBlocks : unwaxedBlocks).get(state);
    }

    public Block getBase(WeatheringState state) {
        return unwaxedBlocks.get(state);
    }

    public Block getWaxedBase(WeatheringState state) {
        return waxedBlocks.get(state);
    }

    public BlockBehaviour.Properties properties(
            WeatheringState state,
            WaxingState waxing
    ) {
        return BlockBehaviour.Properties.ofFullCopy(getBase(state, waxing));
    }

    public static String metalName(
            Weathering metal,
            WeatheringState state,
            WaxingState waxing,
            ExtendedShape shape
    ) {
        StringBuilder name = new StringBuilder();

        if (waxing.isWaxed()) {
            name.append("waxed_");
        }

        if (state != WeatheringState.UNAFFECTED) {
            name.append(state.getSerializedName()).append("_");
        }

        name.append(metal.getSerializedName());

        if (shape != ExtendedShape.BLOCK) {
            name.append("_").append(shape.getSerializedName());
        }

        return name.toString();
    }

    public boolean canCreate(ExtendedShape shape) {
        if (this.equals(CUT_COPPER)) {
            return shape.equals(ExtendedShape.SLAB) || shape.equals(ExtendedShape.STAIRS);
        }

        return shape != ExtendedShape.BLOCK;
    }

    public record WeatheringAsset(
            Weathering metal,
            WeatheringState state,
            WaxingState waxing,
            ExtendedShape shape
    ) {

        public Block getBase() {
            return metal.getBase(state, waxing);
        }

        public BlockBehaviour.Properties properties() {
            return metal.properties(state, waxing);
        }
    }
}