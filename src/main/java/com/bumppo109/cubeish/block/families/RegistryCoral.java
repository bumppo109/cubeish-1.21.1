package com.bumppo109.cubeish.block.families;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public interface RegistryCoral extends StringRepresentable {

    Supplier<Block> getBlock(Coral.BlockType type);

    default Supplier<Block> getDeadBlock(Coral.BlockType type) {
        return getBlock(type.dead());
    }

    Block alive();

    Block dead();

}