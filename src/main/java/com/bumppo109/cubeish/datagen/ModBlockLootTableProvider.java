package com.bumppo109.cubeish.datagen;

import com.bumppo109.cubeish.block.*;
import com.bumppo109.cubeish.block.families.Coral;
import com.bumppo109.cubeish.block.families.Flammable;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        ModBlocks.COLORED_BLOCKS.forEach((block, colorMap) -> {
            colorMap.forEach(((colorType, blockTypeIdMap) -> {
                blockTypeIdMap.forEach((blockType, blockId) -> {
                    if(blockType.equals(ExtendedShape.SLAB)) {
                        add(blockId.get(), createSlabItemTable(blockId.get()));
                    } else {
                        dropSelf(blockId.get());
                    }
                });
            }));
        });

        ModBlocks.MISC_BLOCKS.forEach((block, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                if(blockType.equals(ExtendedShape.SLAB)) {
                    add(blockId.get(), createSlabItemTable(blockId.get()));
                } else {
                    dropSelf(blockId.get());
                }
            });
        });

        ModBlocks.MAGMA.forEach((block, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                dropSelf(blockId.get());
            });
        });

        ModBlocks.WOODS.forEach((modWood, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                if(blockType.equals(Flammable.BlockType.SLAB)) {
                    add(blockId.get(), createSlabItemTable(blockId.get()));
                } else {
                    dropSelf(blockId.get());
                }
            });
        });

        ModBlocks.WEATHERING_METALS.forEach((metal, weatheringMap) -> {
            weatheringMap.forEach((weatheringState, waxingMap) -> {
                waxingMap.forEach((waxingState, extendedShapeIdMap) -> {
                    extendedShapeIdMap.forEach(((extendedShape, blockId) -> {
                        if (extendedShape.equals(ExtendedShape.SLAB)) {
                            add(blockId.get(), createSlabItemTable(blockId.get()));
                        } else {
                            dropSelf(blockId.get());
                        }
                    }));
                });
            });
        });

        ModBlocks.CORALS.forEach((modCoral, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                if(blockType.equals(Coral.BlockType.DEAD_SLAB) || blockType.equals(Coral.BlockType.SLAB)) {
                    add(blockId.get(), createSlabItemTable(blockId.get()));
                } else {
                    dropSelf(blockId.get());
                }
            });
        });
    }

    protected LootTable.Builder createMultipleOreDrops(Block pBlock, Item item, float minDrops, float maxDrops) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock, LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                        .apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))));
    }

    protected LootTable.Builder createCoralLoot(Block coral, Block deadCoral) {
        return this.createSilkTouchDispatchTable(coral, this.applyExplosionDecay(coral, LootItem.lootTableItem(deadCoral.asItem())));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        List<Block> knownBlocks = new ArrayList<>();

        ModBlocks.COLORED_BLOCKS.forEach((block, colorMap) -> {
            colorMap.forEach(((colorType, blockTypeIdMap) -> {
                blockTypeIdMap.forEach((blockType, blockId) -> {
                    knownBlocks.add(blockId.get());
                });
            }));
        });

        ModBlocks.MISC_BLOCKS.forEach((block, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                knownBlocks.add(blockId.get());
            });
        });

        ModBlocks.MAGMA.forEach((block, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                knownBlocks.add(blockId.get());
            });
        });

        ModBlocks.WOODS.forEach((modWood, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                knownBlocks.add(blockId.get());
            });
        });

        ModBlocks.WEATHERING_METALS.forEach((metal, weatheringMap) -> {
            weatheringMap.forEach((weatheringState, waxingMap) -> {
                waxingMap.forEach((waxingState, extendedShapeIdMap) -> {
                    extendedShapeIdMap.forEach(((extendedShape, blockId) -> {
                       knownBlocks.add(blockId.get());
                    }));
                });
            });
        });

        ModBlocks.CORALS.forEach((modCoral, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                knownBlocks.add(blockId.get());
            });
        });

        return knownBlocks;
    }
}
