package com.bumppo109.cubeish.block.families;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public interface RegistryMagma extends StringRepresentable {
    Supplier<Block> getBlock(Magma.BlockType var1);

    BlockBehaviour.Properties properties();

    Supplier<Block> getBase();

    boolean hasStair();

    boolean hasSlab();
}
