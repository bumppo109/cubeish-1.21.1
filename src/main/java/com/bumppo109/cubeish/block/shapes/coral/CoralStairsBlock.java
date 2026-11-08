package com.bumppo109.cubeish.block.shapes.coral;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import javax.annotation.Nullable;

public class CoralStairsBlock extends StairBlock {
    public static final MapCodec<Block> DEAD_CORAL_FIELD = BuiltInRegistries.BLOCK.byNameCodec().fieldOf("dead");
    public static final MapCodec<Block> BASE_FIELD = BuiltInRegistries.BLOCK.byNameCodec().fieldOf("base");

    public static final MapCodec<CoralStairsBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    BASE_FIELD.forGetter(CoralStairsBlock::getBaseBlock),
                    DEAD_CORAL_FIELD.forGetter(block -> block.deadBlock),
                    propertiesCodec()
            ).apply(instance, CoralStairsBlock::new)
    );
    private final Block baseBlock;
    private final Block deadBlock;

    public CoralStairsBlock(Block baseBlock, Block deadBlock, Properties properties) {
        super(baseBlock.defaultBlockState(), properties);
        this.baseBlock = baseBlock;
        this.deadBlock = deadBlock;
    }

    public Block getBaseBlock() {
        return baseBlock;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!this.scanForWater(level, pos)) {
            level.setBlock(pos, this.deadBlock.defaultBlockState()
                    .setValue(FACING, state.getValue(FACING))
                    .setValue(HALF, state.getValue(HALF))
                    .setValue(SHAPE, state.getValue(SHAPE)), 2);
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (!this.scanForWater(level, currentPos)) {
            level.scheduleTick(currentPos, this, 60 + level.getRandom().nextInt(40));
        }

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    protected boolean scanForWater(BlockGetter level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            FluidState fluidstate = level.getFluidState(pos.relative(direction));
            if (fluidstate.is(FluidTags.WATER)) {
                return true;
            }
        }

        return false;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState placedState = super.getStateForPlacement(context);
        if (placedState != null && !this.scanForWater(context.getLevel(), context.getClickedPos())) {
            context.getLevel().scheduleTick(context.getClickedPos(), this, 60 + context.getLevel().getRandom().nextInt(40));
        }

        return placedState;
    }
}
