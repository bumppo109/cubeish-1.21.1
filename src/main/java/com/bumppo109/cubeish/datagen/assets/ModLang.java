package com.bumppo109.cubeish.datagen.assets;

import com.bumppo109.cubeish.Cubeish;
import com.bumppo109.cubeish.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLang extends LanguageProvider {
    public ModLang(PackOutput output) {
        super(output, Cubeish.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("creativetab.cubeish.extended_blocks", "Extended Blocks");

    //Extended Shapes
        ModBlocks.COLORED_BLOCKS.forEach((block, colorMap) -> {
            colorMap.forEach(((colorType, blockTypeIdMap) -> {
                blockTypeIdMap.forEach((blockType, blockId) -> {
                    add(blockId.get());
                });
            }));
        });

        ModBlocks.MISC_BLOCKS.forEach((block, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                add(blockId.get());
            });
        });

        ModBlocks.MAGMA.forEach((block, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                add(blockId.get());
            });
        });

        ModBlocks.WOODS.forEach((modWood, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                add(blockId.get());
            });
        });

        ModBlocks.WEATHERING_METALS.forEach((metal, weatheringStateMap) -> {
            weatheringStateMap.forEach((weatheringState, waxMap) -> {
                waxMap.forEach((waxingState, shapeIdMap) -> {
                    shapeIdMap.forEach(((extendedShape, blockId) -> {
                        add(blockId.get());
                    }));
                });
            });
        });

        ModBlocks.CORALS.forEach((modCoral, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                add(blockId.get());
            });
        });

    }

    public void add(Block block) {
        add(block.getDescriptionId(), getBlockDisplayName(block));
    }

    private String cleanUpString(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        // Replace underscores with spaces
        String normalized = input.replace('_', ' ');

        // Split into words, capitalize each, and join back
        String[] words = normalized.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        // Remove trailing space
        return result.toString().trim();
    }

    private String getBlockDisplayName(Block block) {
        if (block == null) {
            return "Unknown Block";
        }

        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        if (id == null || id.equals(BuiltInRegistries.BLOCK.getDefaultKey())) {
            return "Air";
        }

        // Get the path part, e.g. "wood/planks/oak" or "metal/block/copper"
        String path = id.getPath();

        // Split on both "_" and "/" to handle folder-like paths nicely
        String[] parts = path.split("[_/]");

        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }

            // Capitalize first letter, lowercase the rest
            String word = Character.toUpperCase(part.charAt(0)) + part.substring(1).toLowerCase();
            sb.append(word);

            sb.append(" ");
            first = false;
        }

        // Trim trailing space
        String result = sb.toString().trim();
        return result;
    }

    private String getItemDisplayName(Item item) {
        if (item == null) {
            return "Unknown Item";
        }

        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        if (id == null || id.equals(BuiltInRegistries.ITEM.getDefaultKey())) {
            return "Air";
        }

        // Get the path part, e.g. "metal/ingot/copper" or "lumber/oak"
        String path = id.getPath();

        // Split on both "_" and "/" to handle folder-like paths nicely
        String[] parts = path.split("[_/]");

        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }

            // Capitalize first letter, lowercase the rest
            String word = Character.toUpperCase(part.charAt(0)) + part.substring(1).toLowerCase();
            sb.append(word).append(" ");
            first = false;
        }

        // Trim trailing space
        String result = sb.toString().trim();
        return result;
    }
}
