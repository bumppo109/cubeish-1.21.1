
package com.bumppo109.cubeish.datagen;

import com.bumppo109.cubeish.block.ModBlocks;
import com.bumppo109.cubeish.block.WaxingState;
import com.bumppo109.cubeish.block.WeatheredShape;
import com.bumppo109.cubeish.block.ModBlocks.Id;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Oxidizable;
import net.neoforged.neoforge.registries.datamaps.builtin.Waxable;

import java.util.Map;
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

        ModBlocks.WEATHERING_BLOCKS.forEach((metal, stateMap) -> {

            stateMap.forEach((state, waxingMap) -> {

                Map<WeatheredShape, Id<Block>> unwaxedBlocks =
                        waxingMap.get(WaxingState.UNWAXED);

                Map<WeatheredShape, Id<Block>> waxedBlocks =
                        waxingMap.get(WaxingState.WAXED);

                WeatheringCopper.WeatherState nextState =
                        nextOxidation(state);

                Map<WeatheredShape, Id<Block>> nextBlocks =
                        nextState == null
                                ? Map.of()
                                : stateMap.get(nextState)
                                .get(WaxingState.UNWAXED);

                unwaxedBlocks.forEach((shape, unwaxedHolder) -> {

                    Block unwaxed = unwaxedHolder.get();
                    Block waxed = waxedBlocks.get(shape).get();

                    // Waxing: unwaxed -> waxed
                    addWaxed(unwaxed, waxed);

                    // Oxidation: current -> next oxidation stage
                    if (nextState != null) {
                        Block next = nextBlocks.get(shape).get();
                        addOxidized(unwaxed, next);
                    }
                });
            });
        });
    }

    private static WeatheringCopper.WeatherState nextOxidation(
            WeatheringCopper.WeatherState state
    ) {
        return switch (state) {
            case UNAFFECTED -> WeatheringCopper.WeatherState.EXPOSED;
            case EXPOSED -> WeatheringCopper.WeatherState.WEATHERED;
            case WEATHERED -> WeatheringCopper.WeatherState.OXIDIZED;
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
