package com.bumppo109.cubeish.datagen;

import com.bumppo109.cubeish.block.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Oxidizable;
import net.neoforged.neoforge.registries.datamaps.builtin.Waxable;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {

    public ModDataMapProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider
    ) {
        super(output, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {

        ModBlocks.WEATHERING_METALS.forEach((metal, stateMap) -> {

            stateMap.forEach((state, waxingMap) -> {

                // Only unwaxed blocks need data map entries.
                waxingMap.get(WaxingState.UNWAXED).forEach((shape, blockHolder) -> {

                    Block from = blockHolder.get();

                    // 1. Waxing mapping
                    Block waxed = ModBlocks.WEATHERING_METALS
                            .get(metal)
                            .get(state)
                            .get(WaxingState.WAXED)
                            .get(shape)
                            .get();

                    addWaxed(from, waxed);

                    // 2. Oxidation mapping
                    WeatheringState nextState = nextOxidation(state);

                    if (nextState != null) {

                        Block oxidized = ModBlocks.WEATHERING_METALS
                                .get(metal)
                                .get(nextState)
                                .get(WaxingState.UNWAXED)
                                .get(shape)
                                .get();

                        addOxidized(from, oxidized);
                    }
                });
            });
        });
    }

    /**
     * Returns the next oxidation state.
     * Returns null when the block is already fully oxidized.
     */
    private WeatheringState nextOxidation(WeatheringState state) {
        return switch (state) {
            case UNAFFECTED -> WeatheringState.EXPOSED;
            case EXPOSED -> WeatheringState.WEATHERED;
            case WEATHERED -> WeatheringState.OXIDIZED;
            case OXIDIZED -> null;
        };
    }

    private void addOxidized(Block from, Block to) {
        builder(NeoForgeDataMaps.OXIDIZABLES)
                .add(
                        from.builtInRegistryHolder(),
                        new Oxidizable(to),
                        false
                );
    }

    private void addWaxed(Block from, Block to) {
        builder(NeoForgeDataMaps.WAXABLES)
                .add(
                        from.builtInRegistryHolder(),
                        new Waxable(to),
                        false
                );
    }
}