package com.bumppo109.cubeish.block.families;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public interface RegistryFlammable extends StringRepresentable {
    Supplier<Block> getBlock(Flammable.BlockType var1);

    Supplier<Block> getBase();

    int flammability();

    BlockBehaviour.Properties properties();

    boolean hasStair();

    boolean hasSlab();

}
