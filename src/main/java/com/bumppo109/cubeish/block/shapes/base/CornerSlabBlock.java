package com.bumppo109.cubeish.block.shapes.base;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class CornerSlabBlock extends Block implements SimpleWaterloggedBlock {
    public static final MapCodec<CornerSlabBlock> CODEC = simpleCodec(CornerSlabBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;

    @Override
    public MapCodec<? extends CornerSlabBlock> codec() {
        return CODEC;
    }

    public CornerSlabBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, Half.BOTTOM)
                .setValue(HINGE, DoorHingeSide.LEFT)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, HINGE, WATERLOGGED);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Half half = state.getValue(HALF);
        Direction facing = state.getValue(FACING);
        DoorHingeSide hinge = state.getValue(HINGE);

        double yMin = half == Half.BOTTOM ? 0 : 8;
        double yMax = yMin + 8;

        VoxelShape fullLayer = switch (facing) {

            case NORTH -> hinge == DoorHingeSide.LEFT
                    // Missing SE
                    ? Shapes.or(
                    Block.box(0, yMin, 0, 16, yMax, 8),   // North row
                    Block.box(0, yMin, 8, 8, yMax, 16)    // West south quadrant
            )
                    // Missing SW
                    : Shapes.or(
                    Block.box(0, yMin, 0, 16, yMax, 8),
                    Block.box(8, yMin, 8, 16, yMax, 16)
            );

            case EAST -> hinge == DoorHingeSide.LEFT
                    // Missing SW
                    ? Shapes.or(
                    Block.box(8, yMin, 0, 16, yMax, 16),
                    Block.box(0, yMin, 0, 8, yMax, 8)
            )
                    // Missing NW
                    : Shapes.or(
                    Block.box(8, yMin, 0, 16, yMax, 16),
                    Block.box(0, yMin, 8, 8, yMax, 16)
            );

            case SOUTH -> hinge == DoorHingeSide.LEFT
                    // Missing NW
                    ? Shapes.or(
                    Block.box(0, yMin, 8, 16, yMax, 16),
                    Block.box(8, yMin, 0, 16, yMax, 8)
            )
                    // Missing NE
                    : Shapes.or(
                    Block.box(0, yMin, 8, 16, yMax, 16),
                    Block.box(0, yMin, 0, 8, yMax, 8)
            );

            case WEST -> hinge == DoorHingeSide.LEFT
                    // Missing NE
                    ? Shapes.or(
                    Block.box(0, yMin, 0, 8, yMax, 16),
                    Block.box(0, yMin, 8, 16, yMax, 16)
            )
                    // Missing SE
                    : Shapes.or(
                    Block.box(0, yMin, 0, 8, yMax, 16),
                    Block.box(0, yMin, 0, 16, yMax, 8)
            );

            default -> Shapes.empty();
        };

        return fullLayer;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clickedFace = context.getClickedFace();
        BlockPos blockpos = context.getClickedPos();
        FluidState fluidstate = context.getLevel().getFluidState(blockpos);
        BlockState state = this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);

        // Set HALF based on vertical click
        double yRel = context.getClickLocation().y - (double) blockpos.getY();
        state = state.setValue(HALF, (clickedFace != Direction.DOWN && (clickedFace == Direction.UP || yRel <= 0.5)) ? Half.BOTTOM : Half.TOP);

        // Set HINGE based on perpendicular horizontal click
        Direction facing = state.getValue(FACING);

        Direction right = facing.getClockWise();

        double dx = context.getClickLocation().x - (blockpos.getX() + 0.5);
        double dz = context.getClickLocation().z - (blockpos.getZ() + 0.5);

        boolean rightSide =
                dx * right.getStepX() +
                        dz * right.getStepZ() > 0;

        state = state.setValue(
                HINGE,
                rightSide ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT
        );

        return state;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
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
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        switch (type) {
            case LAND -> {
                return false;
            }
            case WATER -> {
                return state.getFluidState().is(FluidTags.WATER);
            }
            case AIR -> {
                return false;
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        Direction direction = state.getValue(FACING);
        DoorHingeSide hinge = state.getValue(HINGE);
        switch (mirror) {
            case LEFT_RIGHT -> {
                if (direction.getAxis() == Direction.Axis.Z) {
                    return state.setValue(HINGE, hinge == DoorHingeSide.LEFT ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT);
                }
            }
            case FRONT_BACK -> {
                if (direction.getAxis() == Direction.Axis.X) {
                    return state.setValue(HINGE, hinge == DoorHingeSide.LEFT ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT);
                }
            }
        }
        return super.mirror(state, mirror);
    }
}
