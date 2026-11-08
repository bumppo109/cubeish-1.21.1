
package com.bumppo109.cubeish.block.shapes.magma;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ContactDamageHandler {

    private static final double EPSILON = 0.001;

    /**
     * Checks all collision-shape surfaces around an entity.
     * Supports both custom IContactDamage blocks and vanilla magma.
     */
    public static void checkContact(Entity entity) {

        if (!(entity.level() instanceof ServerLevel level)
                || !(entity instanceof LivingEntity living)) {
            return;
        }

        AABB entityBox = entity.getBoundingBox();

        int minX = Mth.floor(entityBox.minX - EPSILON);
        int minY = Mth.floor(entityBox.minY - EPSILON);
        int minZ = Mth.floor(entityBox.minZ - EPSILON);

        int maxX = Mth.floor(entityBox.maxX + EPSILON);
        int maxY = Mth.floor(entityBox.maxY + EPSILON);
        int maxZ = Mth.floor(entityBox.maxZ + EPSILON);

        for (BlockPos pos : BlockPos.betweenClosed(
                minX, minY, minZ,
                maxX, maxY, maxZ
        )) {

            BlockState state = level.getBlockState(pos);

            float damage;
            boolean vanillaMagma = state.is(Blocks.MAGMA_BLOCK);

            if (vanillaMagma) {
                damage = 1.0F;
            } else if (state.getBlock() instanceof IContactDamage contactDamage) {
                damage = contactDamage.getContactDamage();
            } else {
                continue;
            }

            // Preserve magma's vanilla sneaking protection.
            if (vanillaMagma && entity.isSteppingCarefully()) {
                continue;
            }

            VoxelShape shape = state.getCollisionShape(
                    level,
                    pos,
                    CollisionContext.of(entity)
            );

            // Convert entity bounding box into block-local coordinates.
            AABB localEntityBox = entityBox.move(
                    -pos.getX(),
                    -pos.getY(),
                    -pos.getZ()
            );

            boolean touching = shape.toAabbs().stream()
                    .anyMatch(shapeBox ->
                            touchesSurface(shapeBox, localEntityBox)
                    );

            if (touching) {

                // Vanilla MagmaBlock.stepOn() already handles top contact.
                if (vanillaMagma && isTouchingTop(pos, entityBox)) {
                    continue;
                }

                if (vanillaMagma) {
                    living.hurt(
                            level.damageSources().hotFloor(),
                            damage
                    );
                } else {
                    living.hurt(
                            level.damageSources().cactus(),
                            damage
                    );
                }

                return;
            }
        }
    }

    private static boolean isTouchingTop(BlockPos pos, AABB entityBox) {

        double topY = pos.getY() + 1.0;

        return Math.abs(entityBox.minY - topY) <= EPSILON
                && entityBox.maxX > pos.getX()
                && entityBox.minX < pos.getX() + 1.0
                && entityBox.maxZ > pos.getZ()
                && entityBox.minZ < pos.getZ() + 1.0;
    }

    /**
     * Detects contact with any of the six surfaces of a collision AABB.
     *
     * A collision AABB can represent an external face of a block
     * or an internal face of a custom voxel shape.
     */
    private static boolean touchesSurface(
            AABB shape,
            AABB entity
    ) {

        boolean west =
                Math.abs(entity.maxX - shape.minX) <= EPSILON;

        boolean east =
                Math.abs(entity.minX - shape.maxX) <= EPSILON;

        boolean north =
                Math.abs(entity.maxZ - shape.minZ) <= EPSILON;

        boolean south =
                Math.abs(entity.minZ - shape.maxZ) <= EPSILON;

        boolean down =
                Math.abs(entity.maxY - shape.minY) <= EPSILON;

        boolean up =
                Math.abs(entity.minY - shape.maxY) <= EPSILON;

        return
                (west || east)
                        && overlaps(entity.minY, entity.maxY, shape.minY, shape.maxY)
                        && overlaps(entity.minZ, entity.maxZ, shape.minZ, shape.maxZ)

                        ||

                        (north || south)
                                && overlaps(entity.minX, entity.maxX, shape.minX, shape.maxX)
                                && overlaps(entity.minY, entity.maxY, shape.minY, shape.maxY)

                        ||

                        (up || down)
                                && overlaps(entity.minX, entity.maxX, shape.minX, shape.maxX)
                                && overlaps(entity.minZ, entity.maxZ, shape.minZ, shape.maxZ);
    }

    private static boolean overlaps(
            double min1,
            double max1,
            double min2,
            double max2
    ) {
        return max1 > min2 && min1 < max2;
    }
}
