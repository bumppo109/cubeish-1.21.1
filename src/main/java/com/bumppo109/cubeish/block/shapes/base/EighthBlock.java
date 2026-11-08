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
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EighthBlock extends Block implements SimpleWaterloggedBlock {
    public static final MapCodec<EighthBlock> CODEC = simpleCodec(EighthBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;

    // Voxel shapes for NORTH facing
    protected static final VoxelShape BOTTOM_LEFT_NORTH = Block.box(0.0, 0.0, 0.0, 8.0, 8.0, 8.0);
    protected static final VoxelShape BOTTOM_RIGHT_NORTH = Block.box(8.0, 0.0, 0.0, 16.0, 8.0, 8.0);
    protected static final VoxelShape TOP_LEFT_NORTH = Block.box(0.0, 8.0, 0.0, 8.0, 16.0, 8.0);
    protected static final VoxelShape TOP_RIGHT_NORTH = Block.box(8.0, 8.0, 0.0, 16.0, 16.0, 8.0);

    // Voxel shapes for SOUTH facing (left/right reversed relative to facing direction)
    protected static final VoxelShape BOTTOM_LEFT_SOUTH = Block.box(8.0, 0.0, 8.0, 16.0, 8.0, 16.0);
    protected static final VoxelShape BOTTOM_RIGHT_SOUTH = Block.box(0.0, 0.0, 8.0, 8.0, 8.0, 16.0);
    protected static final VoxelShape TOP_LEFT_SOUTH = Block.box(8.0, 8.0, 8.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape TOP_RIGHT_SOUTH = Block.box(0.0, 8.0, 8.0, 8.0, 16.0, 16.0);

    // Voxel shapes for EAST facing
    protected static final VoxelShape BOTTOM_LEFT_EAST = Block.box(8.0, 0.0, 0.0, 16.0, 8.0, 8.0);
    protected static final VoxelShape BOTTOM_RIGHT_EAST = Block.box(8.0, 0.0, 8.0, 16.0, 8.0, 16.0);
    protected static final VoxelShape TOP_LEFT_EAST = Block.box(8.0, 8.0, 0.0, 16.0, 16.0, 8.0);
    protected static final VoxelShape TOP_RIGHT_EAST = Block.box(8.0, 8.0, 8.0, 16.0, 16.0, 16.0);

    // Voxel shapes for WEST facing (left/right reversed relative to facing direction)
    protected static final VoxelShape BOTTOM_LEFT_WEST = Block.box(0.0, 0.0, 8.0, 8.0, 8.0, 16.0);
    protected static final VoxelShape BOTTOM_RIGHT_WEST = Block.box(0.0, 0.0, 0.0, 8.0, 8.0, 8.0);
    protected static final VoxelShape TOP_LEFT_WEST = Block.box(0.0, 8.0, 8.0, 8.0, 16.0, 16.0);
    protected static final VoxelShape TOP_RIGHT_WEST = Block.box(0.0, 8.0, 0.0, 8.0, 16.0, 8.0);

    @Override
    public MapCodec<? extends EighthBlock> codec() {
        return CODEC;
    }

    public EighthBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.defaultBlockState()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(HALF, Half.BOTTOM)
                        .setValue(HINGE, DoorHingeSide.LEFT)
                        .setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    @Override
    protected boolean useShapeForLightOcclusion(@NotNull BlockState state) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, HINGE, WATERLOGGED);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        Half half = state.getValue(HALF);
        DoorHingeSide hinge = state.getValue(HINGE);

        if (direction == Direction.NORTH) {
            if (half == Half.BOTTOM) {
                return hinge == DoorHingeSide.LEFT ? BOTTOM_LEFT_NORTH : BOTTOM_RIGHT_NORTH;
            } else {
                return hinge == DoorHingeSide.LEFT ? TOP_LEFT_NORTH : TOP_RIGHT_NORTH;
            }
        } else if (direction == Direction.SOUTH) {
            if (half == Half.BOTTOM) {
                return hinge == DoorHingeSide.LEFT ? BOTTOM_LEFT_SOUTH : BOTTOM_RIGHT_SOUTH;
            } else {
                return hinge == DoorHingeSide.LEFT ? TOP_LEFT_SOUTH : TOP_RIGHT_SOUTH;
            }
        } else if (direction == Direction.EAST) {
            if (half == Half.BOTTOM) {
                return hinge == DoorHingeSide.LEFT ? BOTTOM_LEFT_EAST : BOTTOM_RIGHT_EAST;
            } else {
                return hinge == DoorHingeSide.LEFT ? TOP_LEFT_EAST : TOP_RIGHT_EAST;
            }
        } else { // WEST
            if (half == Half.BOTTOM) {
                return hinge == DoorHingeSide.LEFT ? BOTTOM_LEFT_WEST : BOTTOM_RIGHT_WEST;
            } else {
                return hinge == DoorHingeSide.LEFT ? TOP_LEFT_WEST : TOP_RIGHT_WEST;
            }
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clickedFace = context.getClickedFace();
        BlockPos blockpos = context.getClickedPos();
        FluidState fluidstate = context.getLevel().getFluidState(blockpos);
        BlockState state = this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));

        // Set HALF based on vertical click
        state = state.setValue(HALF,
                clickedFace != Direction.DOWN && (clickedFace == Direction.UP || !(context.getClickLocation().y - (double)blockpos.getY() > 0.5))
                        ? Half.BOTTOM
                        : Half.TOP);

        // Set HINGE based on horizontal perpendicular click (always compute for consistency across all placements)
        Vec3 hitVec = context.getClickLocation();
        double relX = hitVec.x - blockpos.getX();
        double relZ = hitVec.z - blockpos.getZ();
        Direction facing = state.getValue(FACING);
        DoorHingeSide hinge;
        if (facing.getAxis() == Direction.Axis.Z) {  // North/South: use X
            boolean isRightSide = relX > 0.5;
            hinge = (facing == Direction.NORTH ? (isRightSide ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT)
                    : (isRightSide ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT));
        } else {  // East/West: use Z
            boolean isRightSide = relZ > 0.5;
            hinge = (facing == Direction.EAST ? (isRightSide ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT)
                    : (isRightSide ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT));
        }
        state = state.setValue(HINGE, hinge);

        return state;
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
        switch (pathComputationType) {
            case LAND:
                return false;
            case WATER:
                return state.getFluidState().is(FluidTags.WATER);
            case AIR:
                return false;
            default:
                return false;
        }
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed blockstate.
     */
    @Override
    protected BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed blockstate.
     */
    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        Direction direction = state.getValue(FACING);
        DoorHingeSide hinge = state.getValue(HINGE);
        DoorHingeSide newHinge = hinge == DoorHingeSide.LEFT ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT;
        switch (mirror) {
            case LEFT_RIGHT:
                if (direction.getAxis() == Direction.Axis.Z) {
                    return state.setValue(HINGE, newHinge);
                }
                break;
            case FRONT_BACK:
                if (direction.getAxis() == Direction.Axis.X) {
                    return state.setValue(HINGE, newHinge);
                }
                break;
        }
        return super.mirror(state, mirror);
    }

}