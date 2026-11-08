package com.bumppo109.cubeish.block.families;

import com.bumppo109.cubeish.block.ExtendedShape;
import com.bumppo109.cubeish.block.ExtendedShapeCategory;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Locale;
import java.util.function.Supplier;

public enum ExtendedShapeBlocks
{
    GLOWSTONE(() -> Blocks.GLOWSTONE, false, false, false),
    SEA_LANTERN(() -> Blocks.SEA_LANTERN, false, false, false),
    SHROOMLIGHT(() -> Blocks.SHROOMLIGHT, false, false, false),
    HONEYCOMB(() -> Blocks.HONEYCOMB_BLOCK, false, false, false),

    OCHRE_FROGLIGHT(() -> Blocks.OCHRE_FROGLIGHT, false, false, false),
    PEARLESCENT_FROGLIGHT(() -> Blocks.PEARLESCENT_FROGLIGHT, false, false, false),
    VERDANT_FROGLIGHT(() -> Blocks.VERDANT_FROGLIGHT, false, false, false),

    PRISMARINE(() -> Blocks.PRISMARINE, true, true, true),
    PRISMARINE_BRICKS(() -> Blocks.PRISMARINE_BRICKS, true, true, false),
    DARK_PRISMARINE(() -> Blocks.DARK_PRISMARINE, true, true, false),

    NETHER_BRICKS(() -> Blocks.NETHER_BRICKS, true, true, true),
    CRACKED_NETHER_BRICKS(() -> Blocks.CRACKED_NETHER_BRICKS, false, false, false),
    CHISELED_NETHER_BRICKS(() -> Blocks.CHISELED_NETHER_BRICKS, false, false, false),
    RED_NETHER_BRICKS(() -> Blocks.RED_NETHER_BRICKS, true, true, true),

    BRICKS(() -> Blocks.BRICKS, true, true, true),
    TERRACOTTA(() -> Blocks.TERRACOTTA, false, false, false),
    GLASS(() -> Blocks.GLASS, false, false, false),
    TINTED_GLASS(() -> Blocks.TINTED_GLASS, false, false, false),

    PURPUR(() -> Blocks.PURPUR_BLOCK, true, true, false),
    PURPUR_PILLAR(() -> Blocks.PURPUR_PILLAR, false, false, false, ExtendedShapeCategory.CARVED),

    SANDSTONE(() -> Blocks.SANDSTONE, true, true, true),
    SMOOTH_SANDSTONE(() -> Blocks.SMOOTH_SANDSTONE, true, true, false),
    CUT_SANDSTONE(() -> Blocks.CUT_SANDSTONE, true, false, false, ExtendedShapeCategory.CARVED),
    CHISELED_SANDSTONE(() -> Blocks.CHISELED_SANDSTONE, true, false, false, ExtendedShapeCategory.CARVED),

    RED_SANDSTONE(() -> Blocks.RED_SANDSTONE, true, true, true),
    SMOOTH_RED_SANDSTONE(() -> Blocks.SMOOTH_RED_SANDSTONE, true, true, false),
    CUT_RED_SANDSTONE(() -> Blocks.CUT_RED_SANDSTONE, true, false, false, ExtendedShapeCategory.CARVED),
    CHISELED_RED_SANDSTONE(() -> Blocks.CHISELED_RED_SANDSTONE, true, false, false, ExtendedShapeCategory.CARVED),

    AMETHYST(() -> Blocks.AMETHYST_BLOCK, false, false, false),
    EMERALD(() -> Blocks.EMERALD_BLOCK, false, false, false),
    GOLD(() -> Blocks.GOLD_BLOCK, false, false, false),
    IRON(() -> Blocks.IRON_BLOCK, false, false, false),
    LAPIS(() -> Blocks.LAPIS_BLOCK, false, false, false),
    NETHERITE(() -> Blocks.NETHERITE_BLOCK, false, false, false),
    DIAMOND(() -> Blocks.DIAMOND_BLOCK, false, false, false),
    COAL(() -> Blocks.COAL_BLOCK, false, false, false),

    RAW_COPPER(() -> Blocks.RAW_COPPER_BLOCK, false, false, false),
    RAW_IRON(() -> Blocks.RAW_IRON_BLOCK, false, false, false),
    RAW_GOLD(() -> Blocks.RAW_GOLD_BLOCK, false, false, false),

    BONE(() -> Blocks.BONE_BLOCK, false, false, false),
    MUD_BRICKS(() -> Blocks.MUD_BRICKS, true, true, true),
    MUD(() -> Blocks.MUD, false, false, false, ExtendedShapeCategory.LANDSCAPE),
    PACKED_MUD(() -> Blocks.PACKED_MUD, false, false, false),

    NETHER_WART(() -> Blocks.NETHER_WART_BLOCK, false, false, false),
    WARPED_WART(() -> Blocks.WARPED_WART_BLOCK, false, false, false),

    BROWN_MUSHROOM(() -> Blocks.BROWN_MUSHROOM_BLOCK, false, false, false),
    RED_MUSHROOM(() -> Blocks.RED_MUSHROOM_BLOCK, false, false, false),
    MUSHROOM_STEM(() -> Blocks.MUSHROOM_STEM, false, false, false),

    WARPED_STEM(() -> Blocks.WARPED_STEM, false, false, false, ExtendedShapeCategory.WOOD),
    CRIMSON_STEM(() -> Blocks.CRIMSON_STEM, false, false, false, ExtendedShapeCategory.WOOD),

    SNOW(() -> Blocks.SNOW_BLOCK, false, false, false),

    OBSIDIAN(() -> Blocks.OBSIDIAN, false, false, false),
    CRYING_OBSIDIAN(() -> Blocks.CRYING_OBSIDIAN, false, false, false),

    HAY(() -> Blocks.HAY_BLOCK, false, false, false),

    DIRT(() -> Blocks.DIRT, false, false, false, ExtendedShapeCategory.LANDSCAPE),
    ROOTED_DIRT(() -> Blocks.ROOTED_DIRT, false, false, false, ExtendedShapeCategory.LANDSCAPE),
    COARSE_DIRT(() -> Blocks.COARSE_DIRT, false, false, false, ExtendedShapeCategory.LANDSCAPE),
    DIRT_PATH(() -> Blocks.DIRT_PATH, false, false, false, ExtendedShapeCategory.LANDSCAPE),
    GRASS(() -> Blocks.GRASS_BLOCK, false, false, false, ExtendedShapeCategory.LANDSCAPE),
    PODZOL(() -> Blocks.PODZOL, false, false, false, ExtendedShapeCategory.LANDSCAPE),
    MYCELIUM(() -> Blocks.MYCELIUM, false, false, false, ExtendedShapeCategory.LANDSCAPE),
    WARPED_NYLIUM(() -> Blocks.WARPED_NYLIUM, false, false, false, ExtendedShapeCategory.LANDSCAPE),
    CRIMSON_NYLIUM(() -> Blocks.CRIMSON_NYLIUM, false, false, false, ExtendedShapeCategory.LANDSCAPE),
    CLAY(() -> Blocks.CLAY, false, false, false, ExtendedShapeCategory.LANDSCAPE),

    MOSS(() -> Blocks.MOSS_BLOCK, false, false, false, ExtendedShapeCategory.LANDSCAPE),
    DRIED_KELP(() -> Blocks.DRIED_KELP_BLOCK, false, false, false),

    SOUL_SOIL(() -> Blocks.SOUL_SOIL, false, false, false, ExtendedShapeCategory.LANDSCAPE),

    STONE(() -> Blocks.STONE, true, true, false),
    SMOOTH_STONE(() -> Blocks.SMOOTH_STONE, true, false, false),
    CRACKED_STONE_BRICKS(() -> Blocks.CRACKED_STONE_BRICKS, false, false, false),
    CHISELED_STONE_BRICKS(() -> Blocks.CHISELED_STONE_BRICKS, false, false, false, ExtendedShapeCategory.CARVED),
    COBBLESTONE(() -> Blocks.COBBLESTONE, true, true, true),
    MOSSY_COBBLESTONE(() -> Blocks.MOSSY_COBBLESTONE, true, true, true),
    STONE_BRICKS(() -> Blocks.STONE_BRICKS, true, true, true),
    MOSSY_STONE_BRICKS(() -> Blocks.MOSSY_STONE_BRICKS, true, true, true),

    DEEPSLATE(() -> Blocks.DEEPSLATE, false, false, false),
    REINFORCED_DEEPSLATE(() -> Blocks.REINFORCED_DEEPSLATE, false, false, false),
    CHISELED_DEEPSLATE(() -> Blocks.CHISELED_DEEPSLATE, false, false, false, ExtendedShapeCategory.CARVED),
    CRACKED_DEEPSLATE_BRICKS(() -> Blocks.CRACKED_DEEPSLATE_BRICKS, false, false, false),
    CRACKED_DEEPSLATE_TILES(() -> Blocks.CRACKED_DEEPSLATE_TILES, false, false, false),
    COBBLED_DEEPSLATE(() -> Blocks.COBBLED_DEEPSLATE, true, true, true),
    POLISHED_DEEPSLATE(() -> Blocks.POLISHED_DEEPSLATE, true, true, true),
    DEEPSLATE_BRICKS(() -> Blocks.DEEPSLATE_BRICKS, true, true, true),
    DEEPSLATE_TILES(() -> Blocks.DEEPSLATE_TILES, true, true, true),

    POLISHED_ANDESITE(() -> Blocks.POLISHED_ANDESITE, true, true, false),
    POLISHED_DIORITE(() -> Blocks.POLISHED_DIORITE, true, true, false),
    POLISHED_GRANITE(() -> Blocks.POLISHED_GRANITE, true, true, false),
    ANDESITE(() -> Blocks.ANDESITE, true, true, true),
    DIORITE(() -> Blocks.DIORITE, true, true, true),
    GRANITE(() -> Blocks.GRANITE, true, true, true),

    CHISELED_TUFF(() -> Blocks.CHISELED_TUFF, false, false, false, ExtendedShapeCategory.CARVED),
    CHISELED_TUFF_BRICKS(() -> Blocks.CHISELED_TUFF_BRICKS, false, false, false, ExtendedShapeCategory.CARVED),
    TUFF(() -> Blocks.TUFF, true, true, true),
    POLISHED_TUFF(() -> Blocks.POLISHED_TUFF, true, true, true),
    TUFF_BRICKS(() -> Blocks.TUFF_BRICKS, true, true, true),

    CALCITE(() -> Blocks.CALCITE, false, false, false),

    DRIPSTONE(() -> Blocks.DRIPSTONE_BLOCK, false, false, false),

    BASALT(() -> Blocks.BASALT, false, false, false),
    POLISHED_BASALT(() -> Blocks.POLISHED_BASALT, false, false, false),
    SMOOTH_BASALT(() -> Blocks.SMOOTH_BASALT, false, false, false),

    NETHERRACK(() -> Blocks.NETHERRACK, false, false, false),

    END_STONE(() -> Blocks.END_STONE, false, false, false),
    END_STONE_BRICKS(() -> Blocks.END_STONE_BRICKS, true, true, true),

    CHISELED_POLISHED_BLACKSTONE(() -> Blocks.CHISELED_POLISHED_BLACKSTONE, false, false, false, ExtendedShapeCategory.CARVED),
    CRACKED_POLISHED_BLACKSTONE_BRICKS(() -> Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, false, false, false),
    BLACKSTONE(() -> Blocks.BLACKSTONE, true, true, true),
    POLISHED_BLACKSTONE(() -> Blocks.POLISHED_BLACKSTONE, true, true, true),
    POLISHED_BLACKSTONE_BRICKS(() -> Blocks.POLISHED_BLACKSTONE_BRICKS, true, true, true),
    GILDED_BLACKSTONE(() -> Blocks.GILDED_BLACKSTONE, false, false, false),

    QUARTZ(() -> Blocks.QUARTZ_BLOCK, true, true, false),
    QUARTZ_PILLAR(() -> Blocks.QUARTZ_PILLAR, false, false, false, ExtendedShapeCategory.CARVED),
    CHISELED_QUARTZ(() -> Blocks.QUARTZ_BLOCK, false, false, false, ExtendedShapeCategory.CARVED),
    SMOOTH_QUARTZ(() -> Blocks.SMOOTH_QUARTZ, true, true, false),
    QUARTZ_BRICKS(() -> Blocks.QUARTZ_BRICKS, false, false, false)

    ;

    public static final ExtendedShapeBlocks[] VALUES = values();

    private final String serializedName;
    private final Supplier<Block> base;
    private final BlockBehaviour.Properties properties;
    private final boolean hasSlab;
    private final boolean hasStairs;
    private final boolean hasWall;
    private final ExtendedShapeCategory category;

    ExtendedShapeBlocks(Supplier<Block> base, boolean hasSlab, boolean hasStairs, boolean hasWall) {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.base = base;
        this.properties = BlockBehaviour.Properties.ofFullCopy(base.get());
        this.hasSlab = hasSlab;
        this.hasStairs = hasStairs;
        this.hasWall = hasWall;
        this.category = ExtendedShapeCategory.ALL;
    }

    ExtendedShapeBlocks(Supplier<Block> base, boolean hasSlab, boolean hasStairs, boolean hasWall, ExtendedShapeCategory category) {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.base = base;
        this.properties = BlockBehaviour.Properties.ofFullCopy(base.get());
        this.hasSlab = hasSlab;
        this.hasStairs = hasStairs;
        this.hasWall = hasWall;
        this.category = category;
    }

    ExtendedShapeBlocks(Supplier<Block> base, BlockBehaviour.Properties properties, boolean hasSlab, boolean hasStairs, boolean hasWall, ExtendedShapeCategory category) {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.base = base;
        this.properties = properties;
        this.hasSlab = hasSlab;
        this.hasStairs = hasStairs;
        this.hasWall = hasWall;
        this.category = category;
    }

    public String getSerializedName() {
        return serializedName;
    }

    public Supplier<Block> getBase() {
        return this.base;
    }

    public BlockBehaviour.Properties properties() {
        return this.properties;
    }

    public boolean hasSlab() {
        return this.hasSlab;
    }

    public boolean hasStairs() {
        return this.hasStairs;
    }

    public boolean hasWall() {
        return this.hasWall;
    }

    public boolean canCreate(ExtendedShape shape) {
        return switch (shape) {
            case BLOCK -> base.get() == null;
            case SLAB -> !hasSlab && category.hasSlab();
            case STAIRS -> !hasStairs && category.hasStairs();
            case WALL -> !hasWall && category.hasWall();
            default -> category.hasVariant(shape);
        };
    }

    public String nameFor(ExtendedShape shape) {
        String assetId = this.getSerializedName();

        if (assetId.endsWith("bricks") || assetId.endsWith("tiles")) {
            assetId = assetId.substring(0, assetId.length() - 1);
        }

        return "%s_%s".formatted(assetId, shape);
    }

    public Block create(ExtendedShape shape) {
        return shape.create(
                base.get(),
                properties()
        );
    }
}