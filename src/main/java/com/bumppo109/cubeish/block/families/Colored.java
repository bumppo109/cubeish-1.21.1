package com.bumppo109.cubeish.block.families;

import com.bumppo109.cubeish.block.ColorType;
import com.bumppo109.cubeish.block.ExtendedShape;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Locale;
import java.util.function.Supplier;

public enum Colored {

    CONCRETE(() -> Blocks.WHITE_CONCRETE, true, true, true),
    STAINED_GLASS(() -> Blocks.WHITE_STAINED_GLASS, true, true, true),
    WOOL(() -> Blocks.WHITE_WOOL, true, true, true),
    TERRACOTTA(() -> Blocks.TERRACOTTA, true, true, true),
    GLAZED_TERRACOTTA(() -> Blocks.TERRACOTTA, true, true, true);

    public static final Colored[] VALUES = values();

    private final String serializedName;
    private final Supplier<Block> base;
    private final boolean generateSlab;
    private final boolean generateStairs;
    private final boolean generateWall;

    Colored(
            Supplier<Block> base,
            boolean generateSlab,
            boolean generateStairs,
            boolean generateWall
    ) {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.base = base;
        this.generateSlab = generateSlab;
        this.generateStairs = generateStairs;
        this.generateWall = generateWall;
    }

    public String getSerializedName() {
        return serializedName;
    }

    public Supplier<Block> getBase() {
        return base;
    }

    public boolean canCreate(ExtendedShape shape) {
        return switch (shape) {
            case BLOCK -> false;
            case SLAB -> generateSlab;
            case STAIRS -> generateStairs;
            case WALL -> generateWall;
            default -> true;
        };
    }

    public String nameFor(ColorType color, ExtendedShape shape) {
        String assetId = this.getSerializedName();

        return "%s_%s_%s".formatted(color, assetId, shape);
    }

    public Block create(ExtendedShape shape) {
        Block baseBlock = base.get();

        return shape.create(
                baseBlock,
                baseBlock.properties()
        );
    }
}