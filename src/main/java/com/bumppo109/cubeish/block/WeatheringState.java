package com.bumppo109.cubeish.block;

public enum WeatheringState {
    UNAFFECTED,
    EXPOSED,
    WEATHERED,
    OXIDIZED;

    public String getSerializedName() {
        return name().toLowerCase(java.util.Locale.ROOT);
    }
}