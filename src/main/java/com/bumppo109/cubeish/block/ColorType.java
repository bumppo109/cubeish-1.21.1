package com.bumppo109.cubeish.block;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum ColorType implements StringRepresentable {

    WHITE(),
    LIGHT_GRAY(),
    GRAY(),
    BLACK(),
    BROWN(),
    RED(),
    ORANGE(),
    YELLOW(),
    LIME(),
    GREEN(),
    CYAN(),
    LIGHT_BLUE(),
    BLUE(),
    PURPLE(),
    MAGENTA(),
    PINK()
    ;

    ColorType() {}

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}