package com.bumppo109.cubeish.datagen.assets;

import com.bumppo109.cubeish.Cubeish;
import com.bumppo109.cubeish.block.*;
import com.bumppo109.cubeish.assetdata.BlockAsset;
import com.bumppo109.cubeish.assetdata.BlockAssets;
import com.bumppo109.cubeish.assetdata.BlockTextureSlot;
import com.bumppo109.cubeish.block.families.ExtendedShapeBlocks;
import com.bumppo109.cubeish.block.families.Friction;
import com.bumppo109.cubeish.block.shapes.base.*;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.Half;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Cubeish.MODID, exFileHelper);

        BlockAssets.bootstrap();
    }

    @Override
    protected void registerStatesAndModels() {
        ModBlocks.MISC_BLOCKS.forEach((block, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                if (!block.equals(ExtendedShapeBlocks.GRASS)) {
                    BlockAsset blockAsset = block.getBase() == null ? BlockAssets.get(ModBlocks.MISC_BLOCKS.get(block).get(ExtendedShape.BLOCK).get())
                            : BlockAssets.get(block.getBase().get());

                    ResourceLocation texture = switch (block) {
                        case DIRT_PATH, PODZOL, MYCELIUM, WARPED_NYLIUM, CRIMSON_NYLIUM -> blockAsset.textures().get(BlockTextureSlot.TOP);
                        default -> blockAsset.textures().get(BlockTextureSlot.SIDE);
                    };

                    switchBlockType(blockType, blockId.get(), texture);
                }
            });
        });

        ModBlocks.MAGMA.forEach((blockType, blockId) -> {
            ResourceLocation texture = ResourceLocation.withDefaultNamespace("block/magma");

            switchBlockType(blockType, blockId.get(), texture);
        });

        ModBlocks.ICE.forEach((blockType, blockId) -> {
            ResourceLocation texture = ResourceLocation.withDefaultNamespace("block/ice");

            switchBlockType(blockType, blockId.get(), texture);
        });

        ModBlocks.FRICTION.forEach((block, blockIdMap) -> {
            blockIdMap.forEach(((blockType, blockId) -> {
                ResourceLocation texture = BlockAssets.get(block.getBase().get()).textures().get(BlockTextureSlot.SIDE);

                switch (blockType) {
                    case SLAB -> slabWithItem(blockId.get(), texture);
                    case STAIRS -> stairsWithItem(blockId.get(), texture);
                    case WALL -> wallWithItem(blockId.get(), texture);

                    case VERTICAL_SLAB -> verticalSlabWithItem(blockId.get(), texture);
                    case QUARTER -> quarterWithItem(blockId.get(), texture);
                    case VERTICAL_QUARTER -> verticalQuarterWithItem(blockId.get(), texture);
                    case CORNER_SLAB -> cornerSlabWithItem(blockId.get(), texture);
                    case VERTICAL_CORNER_SLAB -> verticalCornerSlabWithItem(blockId.get(), texture);
                    case VERTICAL_CORNER -> verticalCornerWithItem(blockId.get(), texture);
                    case EIGHTH -> eighthBlockWithItem(blockId.get(), texture);
                }
            }));
        });

        ModBlocks.COLORED_BLOCKS.forEach((block, colorMap) -> {
            colorMap.forEach(((colorType, blockTypeIdMap) -> {
                blockTypeIdMap.forEach((blockType, blockId) -> {
                    ResourceLocation texture = BlockAssets.get(
                            ResourceLocation.withDefaultNamespace(colorType.getSerializedName() + "_" + block.getSerializedName())
                    ).textures().get(BlockTextureSlot.SIDE);

                    switchBlockType(blockType, blockId.get(), texture);
                });
            }));
        });

        ModBlocks.WOODS.forEach((modWood, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {

                ResourceLocation texture = BlockAssets.get(modWood.getBase().get()).textures().get(BlockTextureSlot.SIDE);

                switch (blockType) {
                    case SLAB -> slabWithItem(blockId.get(), texture);
                    case STAIRS -> stairsWithItem(blockId.get(), texture);
                    case WALL -> wallWithItem(blockId.get(), texture);

                    case VERTICAL_SLAB -> verticalSlabWithItem(blockId.get(), texture);
                    case QUARTER -> quarterWithItem(blockId.get(), texture);
                    case VERTICAL_QUARTER -> verticalQuarterWithItem(blockId.get(), texture);
                    case CORNER_SLAB -> cornerSlabWithItem(blockId.get(), texture);
                    case VERTICAL_CORNER_SLAB -> verticalCornerSlabWithItem(blockId.get(), texture);
                    case VERTICAL_CORNER -> verticalCornerWithItem(blockId.get(), texture);
                    case EIGHTH -> eighthBlockWithItem(blockId.get(), texture);
                }
            });
        });

        ModBlocks.WEATHERING_BLOCKS.forEach((metal, weatheringMap) -> {
            weatheringMap.forEach((weatheredState, WaxMap) -> {
                WaxMap.forEach((type, shapeMap) -> {
                    shapeMap.forEach(((extendedShape, blockId) -> {
                        ResourceLocation texture = BlockAssets.get(metal.getBase(weatheredState)).textures().get(BlockTextureSlot.SIDE);

                        switch (extendedShape) {
                            case SLAB -> slabWithItem(blockId.get(), texture, texture, texture);
                            case STAIRS -> stairsWithItem(blockId.get(), texture);
                            case WALL -> wallWithItem(blockId.get(), texture);

                            case VERTICAL_SLAB -> verticalSlabWithItem(blockId.get(), texture);
                            case QUARTER -> quarterWithItem(blockId.get(), texture);
                            case VERTICAL_QUARTER -> verticalQuarterWithItem(blockId.get(), texture);
                            case VERTICAL_CORNER -> verticalCornerWithItem(blockId.get(), texture);
                            case CORNER_SLAB -> cornerSlabWithItem(blockId.get(), texture);
                            case VERTICAL_CORNER_SLAB -> verticalCornerSlabWithItem(blockId.get(), texture);
                            case EIGHTH -> eighthBlockWithItem(blockId.get(), texture);
                            default -> Cubeish.LOGGER.debug("BlockStateProvider WEATHERING_METALS - unexpected shape");
                        }
                    }));
                });
            });
        });

        ModBlocks.CORALS.forEach((modCoral, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                ResourceLocation texture;

                if (blockType.name().startsWith("DEAD")) {
                    texture = BlockAssets.get(modCoral.dead()).textures().get(BlockTextureSlot.SIDE);
                } else {
                    texture = BlockAssets.get(modCoral.alive()).textures().get(BlockTextureSlot.SIDE);
                }


                if (blockType.name().endsWith("VERTICAL_SLAB")) {
                    verticalSlabWithItem(blockId.get(), texture);
                } else if (blockType.name().endsWith("VERTICAL_QUARTER")) {
                    verticalQuarterWithItem(blockId.get(), texture);
                } else if (blockType.name().endsWith("QUARTER")) {
                    quarterWithItem(blockId.get(), texture);
                } else if (blockType.name().endsWith("VERTICAL_CORNER")) {
                    verticalCornerWithItem(blockId.get(), texture);
                } else if (blockType.name().endsWith("VERTICAL_CORNER_SLAB")) {
                    verticalCornerSlabWithItem(blockId.get(), texture);
                } else if (blockType.name().endsWith("CORNER_SLAB")) {
                    cornerSlabWithItem(blockId.get(), texture);
                } else if (blockType.name().endsWith("SLAB")) {
                    slabWithItem(blockId.get(), texture, texture, texture);
                } else if (blockType.name().endsWith("EIGHTH")) {
                    eighthBlockWithItem(blockId.get(), texture);
                } else if (blockType.name().endsWith("STAIRS")) {
                    stairsWithItem(blockId.get(), texture);
                } else if (blockType.name().endsWith("WALL")) {
                    wallWithItem(blockId.get(), texture);
                }
            });
        });
    }

    private void switchBlockType(
            ExtendedShape blockType,
            Block block,
            ResourceLocation texture
    ) {
        switch (blockType) {
            case BLOCK -> cubeAll(block, texture);
            case SLAB -> slabWithItem(block, texture);
            case STAIRS -> stairsWithItem(block, texture);
            case WALL -> wallWithItem(block, texture);

            case VERTICAL_SLAB -> verticalSlabWithItem(block, texture);
            case QUARTER -> quarterWithItem(block, texture);
            case VERTICAL_QUARTER -> verticalQuarterWithItem(block, texture);
            case CORNER_SLAB -> cornerSlabWithItem(block, texture);
            case VERTICAL_CORNER_SLAB -> verticalCornerSlabWithItem(block, texture);
            case VERTICAL_CORNER -> verticalCornerWithItem(block, texture);
            case EIGHTH -> eighthBlockWithItem(block, texture);
        }
    }

    private void switchBlockType(
            MagmaShape blockType,
            Block block,
            ResourceLocation texture
    ) {
        switch (blockType) {
            case SLAB -> slabWithItem(block, texture);
            case STAIRS -> stairsWithItem(block, texture);
            case WALL -> wallWithItem(block, texture);

            case VERTICAL_SLAB -> verticalSlabWithItem(block, texture);
            case QUARTER -> quarterWithItem(block, texture);
            case VERTICAL_QUARTER -> verticalQuarterWithItem(block, texture);
            case CORNER_SLAB -> cornerSlabWithItem(block, texture);
            case VERTICAL_CORNER_SLAB -> verticalCornerSlabWithItem(block, texture);
            case VERTICAL_CORNER -> verticalCornerWithItem(block, texture);
            case EIGHTH -> eighthBlockWithItem(block, texture);
        }
    }

    private void switchBlockType(
            IceShape blockType,
            Block block,
            ResourceLocation texture
    ) {
        switch (blockType) {
            case SLAB -> slabWithItem(block, texture);
            case STAIRS -> stairsWithItem(block, texture);
            case WALL -> wallWithItem(block, texture);

            case VERTICAL_SLAB -> verticalSlabWithItem(block, texture);
            case QUARTER -> quarterWithItem(block, texture);
            case VERTICAL_QUARTER -> verticalQuarterWithItem(block, texture);
            case CORNER_SLAB -> cornerSlabWithItem(block, texture);
            case VERTICAL_CORNER_SLAB -> verticalCornerSlabWithItem(block, texture);
            case VERTICAL_CORNER -> verticalCornerWithItem(block, texture);
            case EIGHTH -> eighthBlockWithItem(block, texture);
        }
    }

    //Block with Item Helpers
    private void cubeAll(Block block, ResourceLocation textureAll) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile model = models().cubeAll(blockRes.getPath(), textureAll);

        simpleBlock(block, model);
        blockItem(block);
    }

    private void slabWithItem(Block block, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile bottomSlab = models().slab(blockRes.getPath(), side, bottom, top);
        ModelFile topSlab = models().slabTop(blockRes.getPath() + "_top", side, bottom, top);
        ModelFile full = models().cubeBottomTop(blockRes.getPath() + "_double", side, bottom, top);

        slabBlock((SlabBlock) block, bottomSlab, topSlab, full);
        blockItem(block);
    }

    private void slabWithItem(Block block, ResourceLocation textureAll) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile bottomSlab = models().slab(blockRes.getPath(), textureAll, textureAll, textureAll);
        ModelFile topSlab = models().slabTop(blockRes.getPath() + "_top", textureAll, textureAll, textureAll);
        ModelFile full = models().cubeBottomTop(blockRes.getPath() + "_double", textureAll, textureAll, textureAll);

        slabBlock((SlabBlock) block, bottomSlab, topSlab, full);
        blockItem(block);
    }

    private void stairsWithItem(Block block, ResourceLocation textureAll) {
        stairsBlock((StairBlock) block, textureAll);
        blockItem(block);
    }

    private void wallWithItem(Block block, ResourceLocation textureAll) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);
        ModelFile inventory = models().wallInventory(blockRes.getPath(), textureAll);

        wallBlock((WallBlock) block, textureAll);
        simpleBlockItem(block, inventory);
    }

    private void verticalSlabWithItem(Block block, ResourceLocation textureAll) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        verticalSlabWithItem(block, verticalSlabModel(blockRes.getPath(), textureAll));
    }

    private void verticalSlabWithItem(Block block, ModelFile model) {
        getVariantBuilder(block)
                .partialState().with(VerticalSlabBlock.FACING, Direction.NORTH)
                .modelForState().modelFile(model).addModel()
                .partialState().with(VerticalSlabBlock.FACING, Direction.SOUTH)
                .modelForState().modelFile(model).rotationY(180).addModel()
                .partialState().with(VerticalSlabBlock.FACING, Direction.EAST)
                .modelForState().modelFile(model).rotationY(90).addModel()
                .partialState().with(VerticalSlabBlock.FACING, Direction.WEST)
                .modelForState().modelFile(model).rotationY(270).addModel();

        simpleBlockItem(block, model);
    }

    private void quarterWithItem(Block block, ResourceLocation textureAll) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        quarterWithItem(block, quarterBottomModel(blockRes.getPath(), textureAll), quarterTopModel(blockRes.getPath(), textureAll));
    }

    private void quarterWithItem(Block block, ModelFile bottomModel, ModelFile topModel) {
        getVariantBuilder(block)
                .partialState().with(QuarterBlock.FACING, Direction.NORTH).with(QuarterBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomModel).addModel()
                .partialState().with(QuarterBlock.FACING, Direction.NORTH).with(QuarterBlock.HALF, Half.TOP)
                .modelForState().modelFile(topModel).addModel()
                .partialState().with(QuarterBlock.FACING, Direction.SOUTH).with(QuarterBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomModel).rotationY(180).addModel()
                .partialState().with(QuarterBlock.FACING, Direction.SOUTH).with(QuarterBlock.HALF, Half.TOP)
                .modelForState().modelFile(topModel).rotationY(180).addModel()
                .partialState().with(QuarterBlock.FACING, Direction.EAST).with(QuarterBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomModel).rotationY(90).addModel()
                .partialState().with(QuarterBlock.FACING, Direction.EAST).with(QuarterBlock.HALF, Half.TOP)
                .modelForState().modelFile(topModel).rotationY(90).addModel()
                .partialState().with(QuarterBlock.FACING, Direction.WEST).with(QuarterBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomModel).rotationY(270).addModel()
                .partialState().with(QuarterBlock.FACING, Direction.WEST).with(QuarterBlock.HALF, Half.TOP)
                .modelForState().modelFile(topModel).rotationY(270).addModel();

        simpleBlockItem(block, bottomModel);
    }

    private void verticalQuarterWithItem(Block block, ResourceLocation textureAll) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        verticalQuarterWithItem(block, verticalQuarterLeftModel(blockRes.getPath(), textureAll), verticalQuarterRightModel(blockRes.getPath(), textureAll));
    }

    private void verticalQuarterWithItem(Block block, ModelFile leftModel, ModelFile rightModel) {
        getVariantBuilder(block)
                .partialState().with(VerticalQuarterBlock.FACING, Direction.NORTH).with(VerticalQuarterBlock.HINGE, DoorHingeSide.LEFT)
                .modelForState().modelFile(leftModel).addModel()
                .partialState().with(VerticalQuarterBlock.FACING, Direction.NORTH).with(VerticalQuarterBlock.HINGE, DoorHingeSide.RIGHT)
                .modelForState().modelFile(rightModel).addModel()
                .partialState().with(VerticalQuarterBlock.FACING, Direction.SOUTH).with(VerticalQuarterBlock.HINGE, DoorHingeSide.LEFT)
                .modelForState().modelFile(leftModel).rotationY(180).addModel()
                .partialState().with(VerticalQuarterBlock.FACING, Direction.SOUTH).with(VerticalQuarterBlock.HINGE, DoorHingeSide.RIGHT)
                .modelForState().modelFile(rightModel).rotationY(180).addModel()
                .partialState().with(VerticalQuarterBlock.FACING, Direction.EAST).with(VerticalQuarterBlock.HINGE, DoorHingeSide.LEFT)
                .modelForState().modelFile(leftModel).rotationY(90).addModel()
                .partialState().with(VerticalQuarterBlock.FACING, Direction.EAST).with(VerticalQuarterBlock.HINGE, DoorHingeSide.RIGHT)
                .modelForState().modelFile(rightModel).rotationY(90).addModel()
                .partialState().with(VerticalQuarterBlock.FACING, Direction.WEST).with(VerticalQuarterBlock.HINGE, DoorHingeSide.LEFT)
                .modelForState().modelFile(leftModel).rotationY(270).addModel()
                .partialState().with(VerticalQuarterBlock.FACING, Direction.WEST).with(VerticalQuarterBlock.HINGE, DoorHingeSide.RIGHT)
                .modelForState().modelFile(rightModel).rotationY(270).addModel();

        simpleBlockItem(block, leftModel);
    }

    private void cornerSlabWithItem(Block block, ResourceLocation textureAll) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        cornerSlabWithItem(block, cornerSlabBottomLeftModel(blockRes.getPath(), textureAll), cornerSlabBottomRightModel(blockRes.getPath(), textureAll), cornerSlabTopLeftModel(blockRes.getPath(), textureAll), cornerSlabTopRightModel(blockRes.getPath(), textureAll));
    }

    private void cornerSlabWithItem(Block block, ModelFile bottomLeft, ModelFile bottomRight, ModelFile topLeft, ModelFile topRight) {
        getVariantBuilder(block)
                .partialState().with(CornerSlabBlock.FACING, Direction.NORTH).with(CornerSlabBlock.HINGE, DoorHingeSide.LEFT).with(CornerSlabBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomLeft).addModel()
                .partialState().with(CornerSlabBlock.FACING, Direction.NORTH).with(CornerSlabBlock.HINGE, DoorHingeSide.RIGHT).with(CornerSlabBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomRight).addModel()
                .partialState().with(CornerSlabBlock.FACING, Direction.NORTH).with(CornerSlabBlock.HINGE, DoorHingeSide.LEFT).with(CornerSlabBlock.HALF, Half.TOP)
                .modelForState().modelFile(topLeft).addModel()
                .partialState().with(CornerSlabBlock.FACING, Direction.NORTH).with(CornerSlabBlock.HINGE, DoorHingeSide.RIGHT).with(CornerSlabBlock.HALF, Half.TOP)
                .modelForState().modelFile(topRight).addModel()

                .partialState().with(CornerSlabBlock.FACING, Direction.SOUTH).with(CornerSlabBlock.HINGE, DoorHingeSide.LEFT).with(CornerSlabBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomLeft).rotationY(180).addModel()
                .partialState().with(CornerSlabBlock.FACING, Direction.SOUTH).with(CornerSlabBlock.HINGE, DoorHingeSide.RIGHT).with(CornerSlabBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomRight).rotationY(180).addModel()
                .partialState().with(CornerSlabBlock.FACING, Direction.SOUTH).with(CornerSlabBlock.HINGE, DoorHingeSide.LEFT).with(CornerSlabBlock.HALF, Half.TOP)
                .modelForState().modelFile(topLeft).rotationY(180).addModel()
                .partialState().with(CornerSlabBlock.FACING, Direction.SOUTH).with(CornerSlabBlock.HINGE, DoorHingeSide.RIGHT).with(CornerSlabBlock.HALF, Half.TOP)
                .modelForState().modelFile(topRight).rotationY(180).addModel()

                .partialState().with(CornerSlabBlock.FACING, Direction.EAST).with(CornerSlabBlock.HINGE, DoorHingeSide.LEFT).with(CornerSlabBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomLeft).rotationY(90).addModel()
                .partialState().with(CornerSlabBlock.FACING, Direction.EAST).with(CornerSlabBlock.HINGE, DoorHingeSide.RIGHT).with(CornerSlabBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomRight).rotationY(90).addModel()
                .partialState().with(CornerSlabBlock.FACING, Direction.EAST).with(CornerSlabBlock.HINGE, DoorHingeSide.LEFT).with(CornerSlabBlock.HALF, Half.TOP)
                .modelForState().modelFile(topLeft).rotationY(90).addModel()
                .partialState().with(CornerSlabBlock.FACING, Direction.EAST).with(CornerSlabBlock.HINGE, DoorHingeSide.RIGHT).with(CornerSlabBlock.HALF, Half.TOP)
                .modelForState().modelFile(topRight).rotationY(90).addModel()

                .partialState().with(CornerSlabBlock.FACING, Direction.WEST).with(CornerSlabBlock.HINGE, DoorHingeSide.LEFT).with(CornerSlabBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomLeft).rotationY(270).addModel()
                .partialState().with(CornerSlabBlock.FACING, Direction.WEST).with(CornerSlabBlock.HINGE, DoorHingeSide.RIGHT).with(CornerSlabBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomRight).rotationY(270).addModel()
                .partialState().with(CornerSlabBlock.FACING, Direction.WEST).with(CornerSlabBlock.HINGE, DoorHingeSide.LEFT).with(CornerSlabBlock.HALF, Half.TOP)
                .modelForState().modelFile(topLeft).rotationY(270).addModel()
                .partialState().with(CornerSlabBlock.FACING, Direction.WEST).with(CornerSlabBlock.HINGE, DoorHingeSide.RIGHT).with(CornerSlabBlock.HALF, Half.TOP)
                .modelForState().modelFile(topRight).rotationY(270).addModel();

        simpleBlockItem(block, bottomLeft);
    }

    private void eighthBlockWithItem(Block block, ResourceLocation textureAll) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        eighthBlockWithItem(block, eighthBottomLeftModel(blockRes.getPath(), textureAll), eighthBottomRightModel(blockRes.getPath(), textureAll), eighthTopLeftModel(blockRes.getPath(), textureAll), eighthTopRightModel(blockRes.getPath(), textureAll));
    }

    private void eighthBlockWithItem(Block block, ModelFile bottomLeft, ModelFile bottomRight, ModelFile topLeft, ModelFile topRight) {
        getVariantBuilder(block)
                .partialState().with(EighthBlock.FACING, Direction.NORTH).with(EighthBlock.HINGE, DoorHingeSide.LEFT).with(EighthBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomLeft).addModel()
                .partialState().with(EighthBlock.FACING, Direction.NORTH).with(EighthBlock.HINGE, DoorHingeSide.RIGHT).with(EighthBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomRight).addModel()
                .partialState().with(EighthBlock.FACING, Direction.NORTH).with(EighthBlock.HINGE, DoorHingeSide.LEFT).with(EighthBlock.HALF, Half.TOP)
                .modelForState().modelFile(topLeft).addModel()
                .partialState().with(EighthBlock.FACING, Direction.NORTH).with(EighthBlock.HINGE, DoorHingeSide.RIGHT).with(EighthBlock.HALF, Half.TOP)
                .modelForState().modelFile(topRight).addModel()

                .partialState().with(EighthBlock.FACING, Direction.SOUTH).with(EighthBlock.HINGE, DoorHingeSide.LEFT).with(EighthBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomLeft).rotationY(180).addModel()
                .partialState().with(EighthBlock.FACING, Direction.SOUTH).with(EighthBlock.HINGE, DoorHingeSide.RIGHT).with(EighthBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomRight).rotationY(180).addModel()
                .partialState().with(EighthBlock.FACING, Direction.SOUTH).with(EighthBlock.HINGE, DoorHingeSide.LEFT).with(EighthBlock.HALF, Half.TOP)
                .modelForState().modelFile(topLeft).rotationY(180).addModel()
                .partialState().with(EighthBlock.FACING, Direction.SOUTH).with(EighthBlock.HINGE, DoorHingeSide.RIGHT).with(EighthBlock.HALF, Half.TOP)
                .modelForState().modelFile(topRight).rotationY(180).addModel()

                .partialState().with(EighthBlock.FACING, Direction.EAST).with(EighthBlock.HINGE, DoorHingeSide.LEFT).with(EighthBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomLeft).rotationY(90).addModel()
                .partialState().with(EighthBlock.FACING, Direction.EAST).with(EighthBlock.HINGE, DoorHingeSide.RIGHT).with(EighthBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomRight).rotationY(90).addModel()
                .partialState().with(EighthBlock.FACING, Direction.EAST).with(EighthBlock.HINGE, DoorHingeSide.LEFT).with(EighthBlock.HALF, Half.TOP)
                .modelForState().modelFile(topLeft).rotationY(90).addModel()
                .partialState().with(EighthBlock.FACING, Direction.EAST).with(EighthBlock.HINGE, DoorHingeSide.RIGHT).with(EighthBlock.HALF, Half.TOP)
                .modelForState().modelFile(topRight).rotationY(90).addModel()

                .partialState().with(EighthBlock.FACING, Direction.WEST).with(EighthBlock.HINGE, DoorHingeSide.LEFT).with(EighthBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomLeft).rotationY(270).addModel()
                .partialState().with(EighthBlock.FACING, Direction.WEST).with(EighthBlock.HINGE, DoorHingeSide.RIGHT).with(EighthBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomRight).rotationY(270).addModel()
                .partialState().with(EighthBlock.FACING, Direction.WEST).with(EighthBlock.HINGE, DoorHingeSide.LEFT).with(EighthBlock.HALF, Half.TOP)
                .modelForState().modelFile(topLeft).rotationY(270).addModel()
                .partialState().with(EighthBlock.FACING, Direction.WEST).with(EighthBlock.HINGE, DoorHingeSide.RIGHT).with(EighthBlock.HALF, Half.TOP)
                .modelForState().modelFile(topRight).rotationY(270).addModel();

        simpleBlockItem(block, bottomLeft);
    }

    private void verticalCornerSlabWithItem(Block block, ResourceLocation textureAll) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        verticalCornerSlabWithItem(block, verticalCornerSlabBottomLeftModel(blockRes.getPath(), textureAll), verticalCornerSlabBottomRightModel(blockRes.getPath(), textureAll), verticalCornerSlabTopLeftModel(blockRes.getPath(), textureAll), verticalCornerSlabTopRightModel(blockRes.getPath(), textureAll));
    }

    private void verticalCornerSlabWithItem(Block block, ModelFile bottomLeft, ModelFile bottomRight, ModelFile topLeft, ModelFile topRight) {
        getVariantBuilder(block)
                .partialState().with(VerticalCornerSlabBlock.FACING, Direction.NORTH).with(VerticalCornerSlabBlock.HINGE, DoorHingeSide.LEFT).with(VerticalCornerSlabBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomLeft).addModel()
                .partialState().with(VerticalCornerSlabBlock.FACING, Direction.NORTH).with(VerticalCornerSlabBlock.HINGE, DoorHingeSide.RIGHT).with(VerticalCornerSlabBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomRight).addModel()
                .partialState().with(VerticalCornerSlabBlock.FACING, Direction.NORTH).with(VerticalCornerSlabBlock.HINGE, DoorHingeSide.LEFT).with(VerticalCornerSlabBlock.HALF, Half.TOP)
                .modelForState().modelFile(topLeft).addModel()
                .partialState().with(VerticalCornerSlabBlock.FACING, Direction.NORTH).with(VerticalCornerSlabBlock.HINGE, DoorHingeSide.RIGHT).with(VerticalCornerSlabBlock.HALF, Half.TOP)
                .modelForState().modelFile(topRight).addModel()

                .partialState().with(VerticalCornerSlabBlock.FACING, Direction.SOUTH).with(VerticalCornerSlabBlock.HINGE, DoorHingeSide.LEFT).with(VerticalCornerSlabBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomLeft).rotationY(180).addModel()
                .partialState().with(VerticalCornerSlabBlock.FACING, Direction.SOUTH).with(VerticalCornerSlabBlock.HINGE, DoorHingeSide.RIGHT).with(VerticalCornerSlabBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomRight).rotationY(180).addModel()
                .partialState().with(VerticalCornerSlabBlock.FACING, Direction.SOUTH).with(VerticalCornerSlabBlock.HINGE, DoorHingeSide.LEFT).with(VerticalCornerSlabBlock.HALF, Half.TOP)
                .modelForState().modelFile(topLeft).rotationY(180).addModel()
                .partialState().with(VerticalCornerSlabBlock.FACING, Direction.SOUTH).with(VerticalCornerSlabBlock.HINGE, DoorHingeSide.RIGHT).with(VerticalCornerSlabBlock.HALF, Half.TOP)
                .modelForState().modelFile(topRight).rotationY(180).addModel()

                .partialState().with(VerticalCornerSlabBlock.FACING, Direction.EAST).with(VerticalCornerSlabBlock.HINGE, DoorHingeSide.LEFT).with(VerticalCornerSlabBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomLeft).rotationY(90).addModel()
                .partialState().with(VerticalCornerSlabBlock.FACING, Direction.EAST).with(VerticalCornerSlabBlock.HINGE, DoorHingeSide.RIGHT).with(VerticalCornerSlabBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomRight).rotationY(90).addModel()
                .partialState().with(VerticalCornerSlabBlock.FACING, Direction.EAST).with(VerticalCornerSlabBlock.HINGE, DoorHingeSide.LEFT).with(VerticalCornerSlabBlock.HALF, Half.TOP)
                .modelForState().modelFile(topLeft).rotationY(90).addModel()
                .partialState().with(VerticalCornerSlabBlock.FACING, Direction.EAST).with(VerticalCornerSlabBlock.HINGE, DoorHingeSide.RIGHT).with(VerticalCornerSlabBlock.HALF, Half.TOP)
                .modelForState().modelFile(topRight).rotationY(90).addModel()

                .partialState().with(VerticalCornerSlabBlock.FACING, Direction.WEST).with(VerticalCornerSlabBlock.HINGE, DoorHingeSide.LEFT).with(VerticalCornerSlabBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomLeft).rotationY(270).addModel()
                .partialState().with(VerticalCornerSlabBlock.FACING, Direction.WEST).with(VerticalCornerSlabBlock.HINGE, DoorHingeSide.RIGHT).with(VerticalCornerSlabBlock.HALF, Half.BOTTOM)
                .modelForState().modelFile(bottomRight).rotationY(270).addModel()
                .partialState().with(VerticalCornerSlabBlock.FACING, Direction.WEST).with(VerticalCornerSlabBlock.HINGE, DoorHingeSide.LEFT).with(VerticalCornerSlabBlock.HALF, Half.TOP)
                .modelForState().modelFile(topLeft).rotationY(270).addModel()
                .partialState().with(VerticalCornerSlabBlock.FACING, Direction.WEST).with(VerticalCornerSlabBlock.HINGE, DoorHingeSide.RIGHT).with(VerticalCornerSlabBlock.HALF, Half.TOP)
                .modelForState().modelFile(topRight).rotationY(270).addModel();

        simpleBlockItem(block, bottomLeft);
    }

    private void verticalCornerWithItem(Block block, ResourceLocation textureAll) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        verticalCornerWithItem(block, verticalCornerLeftModel(blockRes.getPath(), textureAll), verticalCornerRightModel(blockRes.getPath(), textureAll));
    }

    private void verticalCornerWithItem(Block block, ModelFile left, ModelFile right) {
        getVariantBuilder(block)
                .partialState().with(VerticalCornerBlock.FACING, Direction.NORTH).with(VerticalCornerBlock.HINGE, DoorHingeSide.LEFT)
                .modelForState().modelFile(left).addModel()
                .partialState().with(VerticalCornerSlabBlock.FACING, Direction.NORTH).with(VerticalCornerSlabBlock.HINGE, DoorHingeSide.RIGHT)
                .modelForState().modelFile(right).addModel()
                .partialState().with(VerticalCornerBlock.FACING, Direction.SOUTH).with(VerticalCornerBlock.HINGE, DoorHingeSide.LEFT)
                .modelForState().modelFile(left).rotationY(180).addModel()
                .partialState().with(VerticalCornerSlabBlock.FACING, Direction.SOUTH).with(VerticalCornerSlabBlock.HINGE, DoorHingeSide.RIGHT)
                .modelForState().modelFile(right).rotationY(180).addModel()
                .partialState().with(VerticalCornerBlock.FACING, Direction.EAST).with(VerticalCornerBlock.HINGE, DoorHingeSide.LEFT)
                .modelForState().modelFile(left).rotationY(90).addModel()
                .partialState().with(VerticalCornerSlabBlock.FACING, Direction.EAST).with(VerticalCornerSlabBlock.HINGE, DoorHingeSide.RIGHT)
                .modelForState().modelFile(right).rotationY(90).addModel()
                .partialState().with(VerticalCornerBlock.FACING, Direction.WEST).with(VerticalCornerBlock.HINGE, DoorHingeSide.LEFT)
                .modelForState().modelFile(left).rotationY(270).addModel()
                .partialState().with(VerticalCornerSlabBlock.FACING, Direction.WEST).with(VerticalCornerSlabBlock.HINGE, DoorHingeSide.RIGHT)
                .modelForState().modelFile(right).rotationY(270).addModel();

        simpleBlockItem(block, left);
    }


    // BlockState Variants
    private void blockItem(Block block) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        simpleBlockItem(
                block,
                new ModelFile.UncheckedModelFile(
                        ResourceLocation.fromNamespaceAndPath(
                                Cubeish.MODID,
                                "block/" + blockRes.getPath()
                        )
                )
        );
    }

    // Model Helpers
    private ModelFile verticalSlabModel(String baseName, ResourceLocation texture) {
        return models().withExistingParent(baseName, modLoc("block/template/vertical_slab/base_all")).texture("texture", texture);
    }
    private ModelFile verticalQuarterLeftModel(String baseName, ResourceLocation texture) {
        return models().withExistingParent(baseName + "_left", modLoc("block/template/vertical_quarter/left_all")).texture("texture", texture);
    }
    private ModelFile verticalQuarterRightModel(String baseName, ResourceLocation texture) {
        return models().withExistingParent(baseName + "_right", modLoc("block/template/vertical_quarter/right_all")).texture("texture", texture);
    }
    private ModelFile verticalCornerSlabBottomLeftModel(String baseName, ResourceLocation texture) {
        return models().withExistingParent(baseName + "_bottom_left", modLoc("block/template/vertical_corner_slab/bottom_left_all")).texture("texture", texture);
    }
    private ModelFile verticalCornerSlabBottomRightModel(String baseName, ResourceLocation texture) {
        return models().withExistingParent(baseName + "_bottom_right", modLoc("block/template/vertical_corner_slab/bottom_right_all")).texture("texture", texture);
    }
    private ModelFile verticalCornerSlabTopLeftModel(String baseName, ResourceLocation texture) {
        return models().withExistingParent(baseName + "_top_left", modLoc("block/template/vertical_corner_slab/top_left_all")).texture("texture", texture);
    }
    private ModelFile verticalCornerSlabTopRightModel(String baseName, ResourceLocation texture) {
        return models().withExistingParent(baseName + "_top_right", modLoc("block/template/vertical_corner_slab/top_right_all")).texture("texture", texture);
    }
    private ModelFile verticalCornerLeftModel(String baseName, ResourceLocation texture) {
        return models().withExistingParent(baseName + "_left", modLoc("block/template/vertical_corner/left_all")).texture("texture", texture);
    }
    private ModelFile verticalCornerRightModel(String baseName, ResourceLocation texture) {
        return models().withExistingParent(baseName + "_right", modLoc("block/template/vertical_corner/right_all")).texture("texture", texture);
    }
    private ModelFile quarterBottomModel(String baseName, ResourceLocation texture) {
        return models().withExistingParent(baseName + "_bottom", modLoc("block/template/quarter/bottom_all")).texture("texture", texture);
    }
    private ModelFile quarterTopModel(String baseName, ResourceLocation texture) {
        return models().withExistingParent(baseName + "_top", modLoc("block/template/quarter/top_all")).texture("texture", texture);
    }
    private ModelFile eighthBottomLeftModel(String baseName, ResourceLocation texture) {
        return models().withExistingParent(baseName + "_bottom_left", modLoc("block/template/eighth/bottom_left_all")).texture("texture", texture);
    }
    private ModelFile eighthBottomRightModel(String baseName, ResourceLocation texture) {
        return models().withExistingParent(baseName + "_bottom_right", modLoc("block/template/eighth/bottom_right_all")).texture("texture", texture);
    }
    private ModelFile eighthTopLeftModel(String baseName, ResourceLocation texture) {
        return models().withExistingParent(baseName + "_top_left", modLoc("block/template/eighth/top_left_all")).texture("texture", texture);
    }
    private ModelFile eighthTopRightModel(String baseName, ResourceLocation texture) {
        return models().withExistingParent(baseName + "_top_right", modLoc("block/template/eighth/top_right_all")).texture("texture", texture);
    }
    private ModelFile cornerSlabBottomLeftModel(String baseName, ResourceLocation texture) {
        return models().withExistingParent(baseName + "_bottom_left", modLoc("block/template/corner_slab/bottom_left_all")).texture("texture", texture);
    }
    private ModelFile cornerSlabBottomRightModel(String baseName, ResourceLocation texture) {
        return models().withExistingParent(baseName + "_bottom_right", modLoc("block/template/corner_slab/bottom_right_all")).texture("texture", texture);
    }
    private ModelFile cornerSlabTopLeftModel(String baseName, ResourceLocation texture) {
        return models().withExistingParent(baseName + "_top_left", modLoc("block/template/corner_slab/top_left_all")).texture("texture", texture);
    }
    private ModelFile cornerSlabTopRightModel(String baseName, ResourceLocation texture) {
        return models().withExistingParent(baseName + "_top_right", modLoc("block/template/corner_slab/top_right_all")).texture("texture", texture);
    }
}