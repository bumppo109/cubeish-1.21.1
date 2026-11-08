package com.bumppo109.cubeish.block;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Locale;

public enum CoralType implements StringRepresentable {

    NONE(Blocks.AIR, Blocks.AIR),
    TUBE(Blocks.TUBE_CORAL_BLOCK, Blocks.DEAD_TUBE_CORAL_BLOCK),
    BRAIN(Blocks.BRAIN_CORAL_BLOCK, Blocks.DEAD_BRAIN_CORAL_BLOCK),
    BUBBLE(Blocks.BUBBLE_CORAL_BLOCK, Blocks.DEAD_BUBBLE_CORAL_BLOCK),
    FIRE(Blocks.FIRE_CORAL_BLOCK, Blocks.DEAD_FIRE_CORAL_BLOCK),
    HORN(Blocks.HORN_CORAL_BLOCK, Blocks.DEAD_HORN_CORAL_BLOCK);

    private final Block coralBlock;
    private final Block deadCoralBlock;

    CoralType(Block coralBlock, Block deadCoralBlock) {
        this.coralBlock = coralBlock;
        this.deadCoralBlock = deadCoralBlock;
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public boolean hasCoral() {
        return this != NONE;
    }

    public Block getCoralBlock() {
        return this.coralBlock;
    }

    public Block getDeadCoralBlock() {
        return this.deadCoralBlock;
    }

    public static CoralType fromItem(Item item) {

        if (item == Items.TUBE_CORAL ||
                item == Items.TUBE_CORAL_FAN) {
            return TUBE;
        }

        if (item == Items.BRAIN_CORAL ||
                item == Items.BRAIN_CORAL_FAN) {
            return BRAIN;
        }

        if (item == Items.BUBBLE_CORAL ||
                item == Items.BUBBLE_CORAL_FAN) {
            return BUBBLE;
        }

        if (item == Items.FIRE_CORAL ||
                item == Items.FIRE_CORAL_FAN) {
            return FIRE;
        }

        if (item == Items.HORN_CORAL ||
                item == Items.HORN_CORAL_FAN) {
            return HORN;
        }

        return NONE;
    }
}