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

public class VerticalQuarterBlock extends Block implements SimpleWaterloggedBlock {
    public static final MapCodec<VerticalQuarterBlock> CODEC = simpleCodec(VerticalQuarterBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;

    // Voxel shapes: full height (0-16 Y), half depth (facing direction), half width (perpendicular to facing via hinge)
    // NORTH: thin Z 0-8, split X left 0-8 / right 8-16
    protected static final VoxelShape LEFT_NORTH = Block.box(0.0, 0.0, 0.0, 8.0, 16.0, 8.0);
    protected static final VoxelShape RIGHT_NORTH = Block.box(8.0, 0.0, 0.0, 16.0, 16.0, 8.0);

    // SOUTH: thin Z 8-16, split X reversed (left=8-16, right=0-8)
    protected static final VoxelShape LEFT_SOUTH = Block.box(8.0, 0.0, 8.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape RIGHT_SOUTH = Block.box(0.0, 0.0, 8.0, 8.0, 16.0, 16.0);

    // EAST: thin X 8-16, split Z left 0-8 / right 8-16
    protected static final VoxelShape LEFT_EAST = Block.box(8.0, 0.0, 0.0, 16.0, 16.0, 8.0);
    protected static final VoxelShape RIGHT_EAST = Block.box(8.0, 0.0, 8.0, 16.0, 16.0, 16.0);

    // WEST: thin X 0-8, split Z reversed (left=8-16, right=0-8)
    protected static final VoxelShape LEFT_WEST = Block.box(0.0, 0.0, 8.0, 8.0, 16.0, 16.0);
    protected static final VoxelShape RIGHT_WEST = Block.box(0.0, 0.0, 0.0, 8.0, 16.0, 8.0);

    @Override
    public MapCodec<? extends VerticalQuarterBlock> codec() {
        return CODEC;
    }

    // NEW: Required constructor for reflection in ModBlockVariants (takes base for properties inheritance)
    public VerticalQuarterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.defaultBlockState()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(HINGE, DoorHingeSide.LEFT)
                        .setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    @Override
    protected boolean useShapeForLightOcclusion(@NotNull BlockState state) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HINGE, WATERLOGGED);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        DoorHingeSide hinge = state.getValue(HINGE);

        if (direction == Direction.NORTH) {
            return hinge == DoorHingeSide.LEFT ? LEFT_NORTH : RIGHT_NORTH;
        } else if (direction == Direction.SOUTH) {
            return hinge == DoorHingeSide.LEFT ? LEFT_SOUTH : RIGHT_SOUTH;
        } else if (direction == Direction.EAST) {
            return hinge == DoorHingeSide.LEFT ? LEFT_EAST : RIGHT_EAST;
        } else { // WEST
            return hinge == DoorHingeSide.LEFT ? LEFT_WEST : RIGHT_WEST;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        FluidState fluidstate = context.getLevel().getFluidState(blockpos);
        BlockState state = this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())  // Thin on far side relative to player
                .setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));

        // Set HINGE (left/right) based on perpendicular click position
        Vec3 hitVec = context.getClickLocation();
        double relX = hitVec.x - blockpos.getX();
        double relZ = hitVec.z - blockpos.getZ();
        Direction facing = state.getValue(FACING);
        DoorHingeSide hinge;
        if (facing.getAxis() == Direction.Axis.Z) {  // North/South facing: split on X (width)
            boolean isRightSide = relX > 0.5;
            hinge = (facing == Direction.NORTH ? (isRightSide ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT)
                    : (isRightSide ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT));
        } else {  // East/West facing: split on Z (depth)
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
        return switch (pathComputationType) {
            case WATER -> state.getFluidState().is(FluidTags.WATER);
            default -> false;
        };
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
