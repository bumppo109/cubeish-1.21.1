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
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class VerticalCornerSlabBlock extends Block implements SimpleWaterloggedBlock {
    public static final MapCodec<VerticalCornerSlabBlock> CODEC = simpleCodec(VerticalCornerSlabBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;

    @Override
    public MapCodec<? extends VerticalCornerSlabBlock> codec() {
        return CODEC;
    }

    public VerticalCornerSlabBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(HINGE, DoorHingeSide.LEFT)
                .setValue(HALF, Half.BOTTOM)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HINGE, HALF, WATERLOGGED);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        DoorHingeSide hinge = state.getValue(HINGE);
        Half half = state.getValue(HALF);

        double yMin = half == Half.BOTTOM ? 0.0 : 8.0;
        double yMax = yMin + 8.0;

        VoxelShape horizontalArm = switch (facing) {
            case NORTH -> Block.box(0, yMin, 0, 16, yMax, 8);
            case SOUTH -> Block.box(0, yMin, 8, 16, yMax, 16);
            case EAST  -> Block.box(8, yMin, 0, 16, yMax, 16);
            case WEST  -> Block.box(0, yMin, 0, 8, yMax, 16);
            default    -> Shapes.empty();
        };

        VoxelShape verticalArm = switch (facing) {
            case NORTH -> hinge == DoorHingeSide.LEFT
                    ? Block.box(0, 0, 0, 8, 16, 8)
                    : Block.box(8, 0, 0, 16, 16, 8);

            case SOUTH -> hinge == DoorHingeSide.LEFT
                    ? Block.box(8, 0, 8, 16, 16, 16)
                    : Block.box(0, 0, 8, 8, 16, 16);

            case EAST -> hinge == DoorHingeSide.LEFT
                    ? Block.box(8, 0, 0, 16, 16, 8)
                    : Block.box(8, 0, 8, 16, 16, 16);

            case WEST -> hinge == DoorHingeSide.LEFT
                    ? Block.box(0, 0, 8, 8, 16, 16)
                    : Block.box(0, 0, 0, 8, 16, 8);

            default -> Shapes.empty();
        };

        return Shapes.or(horizontalArm, verticalArm);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        FluidState fluid = context.getLevel().getFluidState(pos);

        Direction facing = context.getHorizontalDirection();

        Half half =
                context.getClickedFace() != Direction.DOWN &&
                        (context.getClickedFace() == Direction.UP ||
                                !(context.getClickLocation().y - pos.getY() > 0.5))
                        ? Half.BOTTOM
                        : Half.TOP;

        Vec3 hit = context.getClickLocation();

        double dx = hit.x - (pos.getX() + 0.5);
        double dz = hit.z - (pos.getZ() + 0.5);

        Direction right = facing.getClockWise();

        boolean rightSide =
                dx * right.getStepX() +
                        dz * right.getStepZ() > 0.0;

        DoorHingeSide hinge = rightSide
                ? DoorHingeSide.RIGHT
                : DoorHingeSide.LEFT;

        return defaultBlockState()
                .setValue(FACING, facing)
                .setValue(HALF, half)
                .setValue(HINGE, hinge)
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
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

    //TODO - check pathfinding
    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        switch (type) {
            case WATER -> {
                return state.getFluidState().is(FluidTags.WATER);
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        Direction direction = state.getValue(FACING);
        DoorHingeSide hinge = state.getValue(HINGE);
        Half half = state.getValue(HALF);
        DoorHingeSide newHinge = hinge == DoorHingeSide.LEFT ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT;
        Half newHalf = half == Half.BOTTOM ? Half.TOP : Half.BOTTOM;
        if (mirror == Mirror.LEFT_RIGHT) {
            if (direction.getAxis() == Direction.Axis.Z) {
                return state.setValue(HINGE, newHinge);
            } else {
                return state.setValue(HALF, newHalf);
            }
        } else if (mirror == Mirror.FRONT_BACK) {
            if (direction.getAxis() == Direction.Axis.X) {
                return state.setValue(HINGE, newHinge);
            } else {
                return state.setValue(HALF, newHalf);
            }
        }
        return super.mirror(state, mirror);
    }
}