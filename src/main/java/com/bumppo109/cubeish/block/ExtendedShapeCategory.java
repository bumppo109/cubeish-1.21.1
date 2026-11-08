package com.bumppo109.cubeish.block;

import java.util.Locale;

public enum ExtendedShapeCategory {
    ALL(true, true, true, true, true, true, true, true, true, true),
    CARVED(true, false, false, true, true, true, false, false, true, false),
    LANDSCAPE(true, false, false, false, false, false, false, false, false, false)
    ;

    private final boolean hasSlab;
    private final boolean hasStairs;
    private final boolean hasWall;
    private final boolean hasVertSlab;
    private final boolean hasQuarter;
    private final boolean hasVertQuarter;
    private final boolean hasCornerSlab;
    private final boolean hasVertCornerSlab;
    private final boolean hasVertCorner;
    private final boolean hasEighth;

    ExtendedShapeCategory(
            boolean hasSlab,
            boolean hasStairs,
            boolean hasWall,
            boolean hasVertSlab,
            boolean hasQuarter,
            boolean hasVertQuarter,
            boolean hasCornerSlab,
            boolean hasVertCornerSlab,
            boolean hasVertCorner,
            boolean hasEighth
    ) {
        this.hasSlab = hasSlab;
        this.hasStairs = hasStairs;
        this.hasWall = hasWall;
        this.hasVertSlab = hasVertSlab;
        this.hasQuarter = hasQuarter;
        this.hasVertQuarter = hasVertQuarter;
        this.hasCornerSlab = hasCornerSlab;
        this.hasVertCornerSlab = hasVertCornerSlab;
        this.hasVertCorner = hasVertCorner;
        this.hasEighth = hasEighth;
    }

    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public boolean hasSlab() {
        return hasSlab;
    }

    public boolean hasStairs() {
        return hasStairs;
    }

    public boolean hasWall() {
        return hasWall;
    }

    public boolean hasVertSlab() {
        return hasVertSlab;
    }

    public boolean hasQuarter() {
        return hasQuarter;
    }

    public boolean hasVertQuarter() {
        return hasVertQuarter;
    }

    public boolean hasCornerSlab() {
        return hasCornerSlab;
    }

    public boolean hasVertCornerSlab() {
        return hasVertCornerSlab;
    }

    public boolean hasVertCorner() {
        return hasVertCorner;
    }

    public boolean hasEighth() {
        return hasEighth;
    }

    public boolean hasVariant(ExtendedShape shape) {
        return switch (shape) {
            case BLOCK -> false;
            case SLAB -> hasSlab();
            case STAIRS -> hasStairs();
            case WALL -> hasWall();
            case VERTICAL_SLAB -> hasVertSlab();
            case QUARTER -> hasQuarter();
            case VERTICAL_QUARTER -> hasVertQuarter();
            case CORNER_SLAB -> hasCornerSlab();
            case VERTICAL_CORNER_SLAB -> hasVertCornerSlab();
            case VERTICAL_CORNER -> hasVertCorner();
            case EIGHTH -> hasEighth();
        };
    }
}