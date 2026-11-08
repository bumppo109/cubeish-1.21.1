
package com.bumppo109.cubeish.mixin;

import com.bumppo109.cubeish.block.shapes.friction.IFrictionBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    private static final double SUPPORT_EPSILON = 1.0E-4D;

    @Inject(
            method = "getBlockPosBelowThatAffectsMyMovement",
            at = @At("RETURN"),
            cancellable = true
    )
    private void cubeish$customFrictionSupport(
            CallbackInfoReturnable<BlockPos> cir
    ) {
        Entity entity = (Entity) (Object) this;

        BlockPos customSupport = findCustomSupport(entity);

        if (customSupport != null) {
            cir.setReturnValue(customSupport);
        }
    }

    private static BlockPos findCustomSupport(Entity entity) {
        AABB entityBox = entity.getBoundingBox();
        BlockGetter level = entity.level();

        double feetY = entityBox.minY;

        int minX = (int) Math.floor(entityBox.minX);
        int maxX = (int) Math.floor(entityBox.maxX);

        int minZ = (int) Math.floor(entityBox.minZ);
        int maxZ = (int) Math.floor(entityBox.maxZ);

        int baseY = (int) Math.floor(feetY);

        // Check the block containing the feet and the block immediately below.
        for (int y = baseY; y >= baseY - 1; y--) {
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {

                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = level.getBlockState(pos);

                    if (!(state.getBlock() instanceof IFrictionBlock)) {
                        continue;
                    }

                    VoxelShape collisionShape = state.getCollisionShape(
                            level,
                            pos,
                            CollisionContext.of(entity)
                    );

                    if (collisionShape.isEmpty()) {
                        continue;
                    }

                    // Inspect the actual collision boxes, not the block's
                    // full 16x16x16 bounds.
                    for (AABB localBox : collisionShape.toAabbs()) {
                        AABB worldBox = localBox.move(
                                pos.getX(),
                                pos.getY(),
                                pos.getZ()
                        );

                        // The collision box must have a top surface
                        // at the player's feet.
                        if (Math.abs(worldBox.maxY - feetY) > SUPPORT_EPSILON) {
                            continue;
                        }

                        // The player's bounding box must overlap
                        // the horizontal collision surface.
                        boolean overlapsX =
                                worldBox.maxX > entityBox.minX + SUPPORT_EPSILON &&
                                        worldBox.minX < entityBox.maxX - SUPPORT_EPSILON;

                        boolean overlapsZ =
                                worldBox.maxZ > entityBox.minZ + SUPPORT_EPSILON &&
                                        worldBox.minZ < entityBox.maxZ - SUPPORT_EPSILON;

                        if (overlapsX && overlapsZ) {
                            return pos;
                        }
                    }
                }
            }
        }

        // No custom supporting surface: preserve vanilla behavior.
        return null;
    }
}
