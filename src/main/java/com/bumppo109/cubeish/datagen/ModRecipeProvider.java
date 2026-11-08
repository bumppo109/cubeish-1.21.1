package com.bumppo109.cubeish.datagen;

import com.bumppo109.cubeish.Cubeish;
import com.bumppo109.cubeish.block.ExtendedShape;
import com.bumppo109.cubeish.block.ModBlocks;
import com.bumppo109.cubeish.block.families.Flammable;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.data.recipes.RecipeCategory.BUILDING_BLOCKS;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ModBlocks.MISC_BLOCKS.forEach((extendedShapeBlocks, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                Block parentBlock = extendedShapeBlocks.getBase().get() == null ? ModBlocks.MISC_BLOCKS.get(extendedShapeBlocks).get(ExtendedShape.BLOCK).get()
                        : extendedShapeBlocks.getBase().get();

                if(blockType.equals(ExtendedShape.SLAB)) {
                    simpleSlab(recipeOutput, blockId.get(), parentBlock);
                    stonecutterResultFromBase(recipeOutput, BUILDING_BLOCKS, blockId.get(), parentBlock, 2);
                } else if(blockType.equals(ExtendedShape.STAIRS)) {
                    simpleStairs(recipeOutput, blockId.get(), parentBlock);
                    stonecutterResultFromBase(recipeOutput, BUILDING_BLOCKS, blockId.get(), parentBlock);
                } else if(blockType.equals(ExtendedShape.WALL)) {
                    simpleWall(recipeOutput, blockId.get(), parentBlock);
                    stonecutterResultFromBase(recipeOutput, BUILDING_BLOCKS, blockId.get(), parentBlock);
                } else if(blockType.equals(ExtendedShape.CORNER_SLAB) || blockType.equals(ExtendedShape.VERTICAL_CORNER_SLAB) || blockType.equals(ExtendedShape.VERTICAL_SLAB)) {
                    stonecutterResultFromBase(recipeOutput, BUILDING_BLOCKS, blockId.get(), parentBlock, 2);
                } else if(blockType.equals(ExtendedShape.QUARTER) || blockType.equals(ExtendedShape.VERTICAL_QUARTER)) {
                    stonecutterResultFromBase(recipeOutput, BUILDING_BLOCKS, blockId.get(), parentBlock, 4);
                } else if(blockType.equals(ExtendedShape.EIGHTH)) {
                    stonecutterResultFromBase(recipeOutput, BUILDING_BLOCKS, blockId.get(), parentBlock, 8);
                } else if(blockType.equals(ExtendedShape.VERTICAL_CORNER)) {
                    stonecutterResultFromBase(recipeOutput, BUILDING_BLOCKS, blockId.get(), parentBlock);
                }
            });
        });

        ModBlocks.WOODS.forEach((wood, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                Block parentBlock = wood.getBase().get() == null ? ModBlocks.WOODS.get(wood).get(Flammable.BlockType.BASE).get()
                        : wood.getBase().get();

                if(blockType.equals(Flammable.BlockType.SLAB)) {
                    simpleSlab(recipeOutput, blockId.get(), parentBlock);
                    stonecutterResultFromBase(recipeOutput, BUILDING_BLOCKS, blockId.get(), parentBlock, 2);
                } else if(blockType.equals(Flammable.BlockType.STAIRS)) {
                    simpleStairs(recipeOutput, blockId.get(), parentBlock);
                    stonecutterResultFromBase(recipeOutput, BUILDING_BLOCKS, blockId.get(), parentBlock);
                } else if(blockType.equals(Flammable.BlockType.WALL)) {
                    simpleWall(recipeOutput, blockId.get(), parentBlock);
                    stonecutterResultFromBase(recipeOutput, BUILDING_BLOCKS, blockId.get(), parentBlock);
                } else if(blockType.equals(Flammable.BlockType.CORNER_SLAB) || blockType.equals(Flammable.BlockType.VERTICAL_CORNER_SLAB) || blockType.equals(Flammable.BlockType.VERTICAL_SLAB)) {
                    stonecutterResultFromBase(recipeOutput, BUILDING_BLOCKS, blockId.get(), parentBlock, 2);
                } else if(blockType.equals(Flammable.BlockType.QUARTER) || blockType.equals(Flammable.BlockType.VERTICAL_QUARTER)) {
                    stonecutterResultFromBase(recipeOutput, BUILDING_BLOCKS, blockId.get(), parentBlock, 4);
                } else if(blockType.equals(Flammable.BlockType.EIGHTH)) {
                    stonecutterResultFromBase(recipeOutput, BUILDING_BLOCKS, blockId.get(), parentBlock, 8);
                } else if(blockType.equals(Flammable.BlockType.VERTICAL_CORNER)) {
                    stonecutterResultFromBase(recipeOutput, BUILDING_BLOCKS, blockId.get(), parentBlock);
                }
            });
        });
    }


    protected static void simpleSlab(RecipeOutput recipeOutput, Block output, Block parentBlock) {
        ResourceLocation outputRes = BuiltInRegistries.BLOCK.getKey(output);
        String parentName = BuiltInRegistries.BLOCK.getKey(parentBlock).getPath();

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output, 6)
                .pattern("BBB")
                .define('B', parentBlock)
                .unlockedBy("has_" + parentName, has(parentBlock))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Cubeish.MODID, outputRes.getPath()));
    }

    protected static void simpleStairs(RecipeOutput recipeOutput, Block output, Block parentBlock) {
        ResourceLocation outputRes = BuiltInRegistries.BLOCK.getKey(output);
        String parentName = BuiltInRegistries.BLOCK.getKey(parentBlock).getPath();

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output, 4)
                .pattern("B  ")
                .pattern("BB ")
                .pattern("BBB")
                .define('B', parentBlock)
                .unlockedBy("has_" + parentName, has(parentBlock))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Cubeish.MODID, outputRes.getPath()));;
    }

    protected static void simpleWall(RecipeOutput recipeOutput, Block output, Block parentBlock) {
        ResourceLocation outputRes = BuiltInRegistries.BLOCK.getKey(output);
        String parentName = BuiltInRegistries.BLOCK.getKey(parentBlock).getPath();

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output, 6)
                .pattern("BBB")
                .pattern("BBB")
                .define('B', parentBlock)
                .unlockedBy("has_" + parentName, has(parentBlock))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Cubeish.MODID, outputRes.getPath()));;
    }

    protected static void stonecutterBlockSlab(RecipeOutput recipeOutput, Block input, Block outputBlock, Block outputSlab) {
        stonecutterResultFromBase(recipeOutput, BUILDING_BLOCKS, outputBlock, input);
        stonecutterResultFromBase(recipeOutput, BUILDING_BLOCKS, outputSlab, input, 2);
        stonecutterResultFromBase(recipeOutput, BUILDING_BLOCKS, outputSlab, outputBlock, 2);
    }

    protected static void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory,
                                                                       List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, Cubeish.MODID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}
