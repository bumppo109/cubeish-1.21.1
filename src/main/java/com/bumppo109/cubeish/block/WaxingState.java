package com.bumppo109.cubeish.block;

import java.util.Locale;

public enum WaxingState {
    UNWAXED,
    WAXED;

    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public boolean isWaxed() {
        return this == WAXED;
    }
}