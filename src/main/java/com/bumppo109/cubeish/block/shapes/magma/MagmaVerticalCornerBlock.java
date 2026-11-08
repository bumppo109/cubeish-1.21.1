package com.bumppo109.cubeish.block.shapes.magma;

import com.bumppo109.cubeish.block.shapes.base.VerticalCornerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MagmaVerticalCornerBlock extends VerticalCornerBlock implements IContactDamage {

    public MagmaVerticalCornerBlock(Properties properties) {
        super(properties);
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
