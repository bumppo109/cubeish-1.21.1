package com.bumppo109.cubeish.block.shapes.magma;

import net.minecraft.world.level.block.Block;

public class MagmaBaseBlock extends Block implements IContactDamage {

    public MagmaBaseBlock(Properties properties) {
        super(properties);
    }

    @Override
    public float getContactDamage() {
        return 1.0F;
    }
}