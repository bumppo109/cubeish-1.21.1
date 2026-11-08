package com.bumppo109.cubeish.datagen.tags;

import com.bumppo109.cubeish.Cubeish;
import com.bumppo109.cubeish.block.*;
import com.bumppo109.cubeish.block.families.Coral;
import com.bumppo109.cubeish.block.families.Flammable;
import com.bumppo109.cubeish.block.families.Magma;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Cubeish.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        ModBlocks.WEATHERING_METALS.forEach((metal, weatheringStateMap) -> {
            weatheringStateMap.forEach((weatheringState, waxMap) -> {
                waxMap.forEach((waxingState, shapeIdMap) -> {
                    shapeIdMap.forEach(((extendedShape, blockId) -> {
                        if (shapeIdMap.containsKey(extendedShape)) {
                            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(blockId.get());

                            if(extendedShape.equals(ExtendedShape.WALL)) {
                                tag(BlockTags.WALLS).add(blockId.get());
                            }
                            if(extendedShape.equals(ExtendedShape.STAIRS)) {
                                tag(BlockTags.STAIRS).add(blockId.get());
                            }
                        }
                    }));
                });
            });
        });

        ModBlocks.WOODS.forEach((modWood, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                if (blockTypeIdMap.containsKey(blockType)) {
                    tag(BlockTags.MINEABLE_WITH_AXE).add(blockId.get());

                    if(blockType.equals(Flammable.BlockType.WALL)) {
                        tag(BlockTags.WALLS).add(blockId.get());
                    }
                    if(blockType.equals(Flammable.BlockType.STAIRS)) {
                        tag(BlockTags.STAIRS).add(blockId.get());
                    }
                }
            });
        });

        ModBlocks.COLORED_BLOCKS.forEach((block, colorMap) -> {
            colorMap.forEach(((colorType, blockTypeIdMap) -> {
                blockTypeIdMap.forEach((blockType, blockId) -> {
                    if (blockTypeIdMap.containsKey(blockType)) {
                        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(blockId.get());

                        if(blockType.equals(ExtendedShape.WALL)) {
                            tag(BlockTags.WALLS).add(blockId.get());
                        }
                        if(blockType.equals(ExtendedShape.STAIRS)) {
                            tag(BlockTags.STAIRS).add(blockId.get());
                        }
                    }
                });
            }));
        });

        ModBlocks.MISC_BLOCKS.forEach((block, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                if (blockTypeIdMap.containsKey(blockType)) {
                    tag(BlockTags.MINEABLE_WITH_PICKAXE).add(blockId.get());

                    if(blockType.equals(ExtendedShape.WALL)) {
                        tag(BlockTags.WALLS).add(blockId.get());
                    }
                    if(blockType.equals(ExtendedShape.STAIRS)) {
                        tag(BlockTags.STAIRS).add(blockId.get());
                    }
                }
            });
        });

        ModBlocks.MAGMA.forEach((block, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                if(blockType.equals(Magma.BlockType.WALL)) {
                    tag(BlockTags.WALLS).add(blockId.get());
                }
                if(blockType.equals(Magma.BlockType.STAIRS)) {
                    tag(BlockTags.STAIRS).add(blockId.get());
                }
            });
        });

        ModBlocks.CORALS.forEach((modCoral, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                if (blockTypeIdMap.containsKey(blockType)) {
                    tag(BlockTags.MINEABLE_WITH_PICKAXE).add(blockId.get());

                    if(blockType.equals(Coral.BlockType.WALL)) {
                        tag(BlockTags.WALLS).add(blockId.get());
                    }
                    if(blockType.equals(Coral.BlockType.STAIRS)) {
                        tag(BlockTags.STAIRS).add(blockId.get());
                    }
                }
            });
        });
    }
}
