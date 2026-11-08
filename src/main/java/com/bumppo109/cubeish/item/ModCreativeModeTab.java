package com.bumppo109.cubeish.item;

import com.bumppo109.cubeish.Cubeish;
import com.bumppo109.cubeish.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Cubeish.MODID);

    public static final Supplier<CreativeModeTab> MAIN_GAME_TAB = CREATIVE_MODE_TAB.register("extended_blocks",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Items.DIAMOND))
                    .title(Component.translatable("creativetab.cubeish.extended_blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        //Extended Shapes
                        ModBlocks.COLORED_BLOCKS.forEach((block, colorMap) -> {
                            colorMap.forEach(((colorType, blockTypeIdMap) -> {
                                blockTypeIdMap.forEach((blockType, blockId) -> {
                                    output.accept(blockId);
                                });
                            }));
                        });

                        ModBlocks.MISC_BLOCKS.forEach((block, blockTypeIdMap) -> {
                            blockTypeIdMap.forEach((blockType, blockId) -> {
                                output.accept(blockId);
                            });
                        });

                        ModBlocks.MAGMA.forEach((block, blockTypeIdMap) -> {
                            blockTypeIdMap.forEach((blockType, blockId) -> {
                                output.accept(blockId);
                            });
                        });

                        ModBlocks.WOODS.forEach((modWood, blockTypeIdMap) -> {
                            blockTypeIdMap.forEach((blockType, blockId) -> {
                                output.accept(blockId);
                            });
                        });

                        ModBlocks.WEATHERING_METALS.forEach((metal, weatheredState) -> {
                            weatheredState.forEach((shape, waxState) -> {
                                waxState.forEach((type, shapeMap) -> {
                                    shapeMap.forEach(((extendedShape, blockId) -> {
                                        output.accept(blockId);
                                    }));
                                });
                            });
                        });

                        ModBlocks.CORALS.forEach((modCoral, blockTypeIdMap) -> {
                            blockTypeIdMap.forEach((blockType, blockId) -> {
                                output.accept(blockId);
                            });
                        });

                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
