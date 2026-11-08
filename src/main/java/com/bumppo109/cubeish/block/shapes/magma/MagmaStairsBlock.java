package com.bumppo109.cubeish.block.shapes.magma;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MagmaStairsBlock extends StairBlock implements IContactDamage{

    public MagmaStairsBlock(BlockState baseState, Properties properties) {
        super(baseState, properties);
    }

    @Override
    protected void entityInside(
            BlockState state,
            Level level,
            BlockPos pos,
            Entity entity
    ) {
        ContactDamageHandler.applyInternalDamage(
                state, level, pos, entity
        );
    }

    @Override
    public float getContactDamage() {
        return 1.0F;
    }
}
