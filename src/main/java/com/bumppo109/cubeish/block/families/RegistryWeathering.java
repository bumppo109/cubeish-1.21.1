package com.bumppo109.cubeish.block.families;

import com.bumppo109.cubeish.block.ExtendedShape;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

public interface RegistryWeathering extends StringRepresentable {
    Supplier<Block> getBlock(Weathering metal, ExtendedShape type, ExtendedShape shape);

    MapColor unaffectedColor();

    MapColor exposedColor();

    MapColor weatheredColor();

    MapColor oxidizedColor();

    Rarity rarity();

    default boolean weatheredParts() {
        return this.weatheringResistance() != -1.0F;
    }

    float weatheringResistance();
}