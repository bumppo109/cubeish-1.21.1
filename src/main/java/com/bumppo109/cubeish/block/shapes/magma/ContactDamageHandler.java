package com.bumppo109.cubeish.block.shapes.magma;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.util.Mth;

public class ContactDamageHandler {

    private static final double EPSILON = 0.001;

    // INTERNAL VOXEL FACE DETECTION

    public static void applyInternalDamage(
            BlockState state,
            Level level,
            BlockPos pos,
            Entity entity
    ) {
        if (level.isClientSide || !(entity instanceof LivingEntity living)) {
            return;
        }

        if (!(state.getBlock() instanceof IContactDamage damageBlock)) {
            return;
        }

        VoxelShape collisionShape = state.getCollisionShape(
                level,
                pos,
                CollisionContext.of(entity)
        );

        AABB entityBox = entity.getBoundingBox().move(
                -pos.getX(),
                -pos.getY(),
                -pos.getZ()
        );

        boolean touching = collisionShape.toAabbs().stream()
                .anyMatch(box -> box.inflate(EPSILON).intersects(entityBox));

        if (touching) {
            living.hurt(
                    level.damageSources().cactus(),
                    damageBlock.getContactDamage()
            );
        }
    }

    // OUTER BLOCK FACE DETECTION

    public static void checkOuterContact(Entity entity) {
        if (!(entity.level() instanceof net.minecraft.server.level.ServerLevel level)
                || !(entity instanceof LivingEntity)) {
            return;
        }

        AABB box = entity.getBoundingBox();

        int minX = Mth.floor(box.minX - EPSILON);
        int minY = Mth.floor(box.minY - EPSILON);
        int minZ = Mth.floor(box.minZ - EPSILON);

        int maxX = Mth.floor(box.maxX + EPSILON);
        int maxY = Mth.floor(box.maxY + EPSILON);
        int maxZ = Mth.floor(box.maxZ + EPSILON);

        for (BlockPos pos : BlockPos.betweenClosed(
                minX, minY, minZ,
                maxX, maxY, maxZ
        )) {
            BlockState state = level.getBlockState(pos);

            if (!(state.getBlock() instanceof IContactDamage damageBlock)) {
                continue;
            }

            if (touchesOuterFace(pos, box)) {
                ((LivingEntity) entity).hurt(
                        level.damageSources().cactus(),
                        damageBlock.getContactDamage()
                );
            }
        }
    }

    private static boolean touchesOuterFace(BlockPos pos, AABB entityBox) {
        AABB blockBox = new AABB(pos);

        boolean west = Math.abs(entityBox.maxX - blockBox.minX) <= EPSILON;
        boolean east = Math.abs(entityBox.minX - blockBox.maxX) <= EPSILON;

        boolean north = Math.abs(entityBox.maxZ - blockBox.minZ) <= EPSILON;
        boolean south = Math.abs(entityBox.minZ - blockBox.maxZ) <= EPSILON;

        boolean down = Math.abs(entityBox.maxY - blockBox.minY) <= EPSILON;
        boolean up = Math.abs(entityBox.minY - blockBox.maxY) <= EPSILON;

        return
                ((west || east)
                        && overlaps(entityBox.minY, entityBox.maxY, blockBox.minY, blockBox.maxY)
                        && overlaps(entityBox.minZ, entityBox.maxZ, blockBox.minZ, blockBox.maxZ))
                        ||
                        ((north || south)
                                && overlaps(entityBox.minX, entityBox.maxX, blockBox.minX, blockBox.maxX)
                                && overlaps(entityBox.minY, entityBox.maxY, blockBox.minY, blockBox.maxY))
                        ||
                        ((up || down)
                                && overlaps(entityBox.minX, entityBox.maxX, blockBox.minX, blockBox.maxX)
                                && overlaps(entityBox.minZ, entityBox.maxZ, blockBox.minZ, blockBox.maxZ));
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