package com.bumppo109.cubeish.block.shapes.base;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class QuarterBlock extends Block implements SimpleWaterloggedBlock {
    public static final MapCodec<QuarterBlock> CODEC = simpleCodec(QuarterBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;

    protected static final VoxelShape TOP_N = Block.box(0.0, 8.0, 0.0, 16.0, 16.0, 8.0);
    protected static final VoxelShape TOP_S = Block.box(0.0, 8.0, 8.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape TOP_E = Block.box(8.0, 8.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape TOP_W = Block.box(0.0, 8.0, 0.0, 8.0, 16.0, 16.0);
    protected static final VoxelShape BOTTOM_N = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 8.0);
    protected static final VoxelShape BOTTOM_S = Block.box(0.0, 0.0, 8.0, 16.0, 8.0, 16.0);
    protected static final VoxelShape BOTTOM_E = Block.box(8.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    protected static final VoxelShape BOTTOM_W = Block.box(0.0, 0.0, 0.0, 8.0, 8.0, 16.0);

    @Override
    public MapCodec<? extends QuarterBlock> codec() {
        return CODEC;
    }

    public QuarterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.defaultBlockState()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(HALF, Half.BOTTOM)
                        .setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    @Override
    protected boolean useShapeForLightOcclusion(@NotNull BlockState state) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, WATERLOGGED);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);

        if(state.getValue(HALF) == Half.TOP) {
            switch (direction) {
                case NORTH:
                    return TOP_N;
                case SOUTH:
                    return TOP_S;
                case EAST:
                    return TOP_E;
                case WEST:
                    return TOP_W;
            }
        } else {
            switch (direction) {
                case NORTH:
                    return BOTTOM_N;
                case SOUTH:
                    return BOTTOM_S;
                case EAST:
                    return BOTTOM_E;
                case WEST:
                    return BOTTOM_W;
            }
        }
        return BOTTOM_N;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        BlockPos blockpos = context.getClickedPos();
        FluidState fluidstate = context.getLevel().getFluidState(blockpos);
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(HALF,
                        direction != Direction.DOWN && (direction == Direction.UP || !(context.getClickLocation().y - (double)blockpos.getY() > 0.5))
                                ? Half.BOTTOM
                                : Half.TOP
                )
                .setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        return SimpleWaterloggedBlock.super.placeLiquid(level, pos, state, fluidState);
    }

    @Override
    public boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return SimpleWaterloggedBlock.super.canPlaceLiquid(player, level, pos, state, fluid);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return switch (pathComputationType) {
            case WATER -> state.getFluidState().is(FluidTags.WATER);
            default -> false;
        };
    }
}
