
package com.bumppo109.cubeish.block.families;

import com.bumppo109.cubeish.block.ExtendedShape;
import com.bumppo109.cubeish.block.WaxingState;
import com.bumppo109.cubeish.block.WeatheredShape;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public enum Weathering {

    COPPER(
            Map.of(
                    WeatheringCopper.WeatherState.UNAFFECTED, Blocks.COPPER_BLOCK,
                    WeatheringCopper.WeatherState.EXPOSED, Blocks.EXPOSED_COPPER,
                    WeatheringCopper.WeatherState.WEATHERED, Blocks.WEATHERED_COPPER,
                    WeatheringCopper.WeatherState.OXIDIZED, Blocks.OXIDIZED_COPPER
            ),
            Map.of(
                    WeatheringCopper.WeatherState.UNAFFECTED, Blocks.WAXED_COPPER_BLOCK,
                    WeatheringCopper.WeatherState.EXPOSED, Blocks.WAXED_EXPOSED_COPPER,
                    WeatheringCopper.WeatherState.WEATHERED, Blocks.WAXED_WEATHERED_COPPER,
                    WeatheringCopper.WeatherState.OXIDIZED, Blocks.WAXED_OXIDIZED_COPPER
            )
    ),

    CUT_COPPER(
            Map.of(
                    WeatheringCopper.WeatherState.UNAFFECTED, Blocks.CUT_COPPER,
                    WeatheringCopper.WeatherState.EXPOSED, Blocks.EXPOSED_CUT_COPPER,
                    WeatheringCopper.WeatherState.WEATHERED, Blocks.WEATHERED_CUT_COPPER,
                    WeatheringCopper.WeatherState.OXIDIZED, Blocks.OXIDIZED_CUT_COPPER
            ),
            Map.of(
                    WeatheringCopper.WeatherState.UNAFFECTED, Blocks.WAXED_CUT_COPPER,
                    WeatheringCopper.WeatherState.EXPOSED, Blocks.WAXED_EXPOSED_CUT_COPPER,
                    WeatheringCopper.WeatherState.WEATHERED, Blocks.WAXED_WEATHERED_CUT_COPPER,
                    WeatheringCopper.WeatherState.OXIDIZED, Blocks.WAXED_OXIDIZED_CUT_COPPER
            )
    ),

    CHISELED_COPPER(
            Map.of(
                    WeatheringCopper.WeatherState.UNAFFECTED, Blocks.CHISELED_COPPER,
                    WeatheringCopper.WeatherState.EXPOSED, Blocks.EXPOSED_CHISELED_COPPER,
                    WeatheringCopper.WeatherState.WEATHERED, Blocks.WEATHERED_CHISELED_COPPER,
                    WeatheringCopper.WeatherState.OXIDIZED, Blocks.OXIDIZED_CHISELED_COPPER
            ),
            Map.of(
                    WeatheringCopper.WeatherState.UNAFFECTED, Blocks.WAXED_CHISELED_COPPER,
                    WeatheringCopper.WeatherState.EXPOSED, Blocks.WAXED_EXPOSED_CHISELED_COPPER,
                    WeatheringCopper.WeatherState.WEATHERED, Blocks.WAXED_WEATHERED_CHISELED_COPPER,
                    WeatheringCopper.WeatherState.OXIDIZED, Blocks.WAXED_OXIDIZED_CHISELED_COPPER
            )
    ),

    COPPER_GRATE(
            Map.of(
                    WeatheringCopper.WeatherState.UNAFFECTED, Blocks.COPPER_GRATE,
                    WeatheringCopper.WeatherState.EXPOSED, Blocks.EXPOSED_COPPER_GRATE,
                    WeatheringCopper.WeatherState.WEATHERED, Blocks.WEATHERED_COPPER_GRATE,
                    WeatheringCopper.WeatherState.OXIDIZED, Blocks.OXIDIZED_COPPER_GRATE
            ),
            Map.of(
                    WeatheringCopper.WeatherState.UNAFFECTED, Blocks.WAXED_COPPER_GRATE,
                    WeatheringCopper.WeatherState.EXPOSED, Blocks.WAXED_EXPOSED_COPPER_GRATE,
                    WeatheringCopper.WeatherState.WEATHERED, Blocks.WAXED_WEATHERED_COPPER_GRATE,
                    WeatheringCopper.WeatherState.OXIDIZED, Blocks.WAXED_OXIDIZED_COPPER_GRATE
            )
    );

    private final Map<WeatheringCopper.WeatherState, Block> unwaxedBlocks;
    private final Map<WeatheringCopper.WeatherState, Block> waxedBlocks;

    Weathering(
            Map<WeatheringCopper.WeatherState, Block> unwaxedBlocks,
            Map<WeatheringCopper.WeatherState, Block> waxedBlocks
    ) {
        this.unwaxedBlocks = new EnumMap<>(unwaxedBlocks);
        this.waxedBlocks = new EnumMap<>(waxedBlocks);
    }

    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public Block getBase(
            WeatheringCopper.WeatherState state,
            WaxingState waxing
    ) {
        return (waxing.isWaxed() ? waxedBlocks : unwaxedBlocks).get(state);
    }

    public Block getBase(WeatheringCopper.WeatherState state) {
        return unwaxedBlocks.get(state);
    }

    public Block getWaxedBase(WeatheringCopper.WeatherState state) {
        return waxedBlocks.get(state);
    }

    public BlockBehaviour.Properties properties(
            WeatheringCopper.WeatherState state,
            WaxingState waxing
    ) {
        return BlockBehaviour.Properties.ofFullCopy(getBase(state, waxing));
    }

    public static String metalName(
            Weathering metal,
            WeatheringCopper.WeatherState state,
            WaxingState waxing,
            WeatheredShape shape
    ) {
        StringBuilder name = new StringBuilder();

        if (waxing.isWaxed()) {
            name.append("waxed_");
        }

        if (state != WeatheringCopper.WeatherState.UNAFFECTED) {
            name.append(state.getSerializedName()).append("_");
        }

        name.append(metal.getSerializedName());

        if (shape != null) {
            name.append("_").append(shape.getSerializedName());
        }

        return name.toString();
    }

    public boolean canCreate(WeatheredShape shape) {
        if (this == CUT_COPPER) {
            return shape != WeatheredShape.SLAB
                    && shape != WeatheredShape.STAIRS;
        }

        return true;
    }

    public record WeatheringAsset(
            Weathering metal,
            WeatheringCopper.WeatherState state,
            WaxingState waxing,
            WeatheredShape shape
    ) {

        public Block getBase() {
            return metal.getBase(state, waxing);
        }

        public BlockBehaviour.Properties properties() {
            return metal.properties(state, waxing);
        }
    }
}
