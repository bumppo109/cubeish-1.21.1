package com.bumppo109.cubeish.block.families;

import com.bumppo109.cubeish.block.ModBlocks;
import com.bumppo109.cubeish.block.shapes.base.*;
import com.bumppo109.cubeish.block.shapes.coral.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.apache.commons.lang3.function.TriFunction;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public enum Coral implements RegistryCoral
{
    TUBE_CORAL(Blocks.TUBE_CORAL_BLOCK, Blocks.DEAD_TUBE_CORAL_BLOCK),
    BRAIN_CORAL(Blocks.BRAIN_CORAL_BLOCK, Blocks.DEAD_BRAIN_CORAL_BLOCK),
    BUBBLE_CORAL(Blocks.BUBBLE_CORAL_BLOCK, Blocks.DEAD_BUBBLE_CORAL_BLOCK),
    HORN_CORAL(Blocks.HORN_CORAL_BLOCK, Blocks.DEAD_HORN_CORAL_BLOCK),
    FIRE_CORAL(Blocks.FIRE_CORAL_BLOCK, Blocks.DEAD_FIRE_CORAL_BLOCK)
    ;

    public static final Coral[] VALUES = values();

    private final String serializedName;
    private Block alive;
    private Block dead;

    Coral(Block alive, Block dead) {
        this.serializedName = this.name().toLowerCase(Locale.ROOT);
        this.alive = alive;
        this.dead = dead;
    }

    @Override
    public String getSerializedName()
    {
        return serializedName;
    }
    public Block alive() {
        return alive;
    }
    public Block dead() {
        return dead;
    }
    
    @Override
    public Supplier<Block> getBlock(BlockType type) {
        return ModBlocks.CORALS.get(this).get(type);
    }
    public enum BlockType
    {

        DEAD_SLAB(null,coral -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(coral.dead()))),
        SLAB(DEAD_SLAB, (type, coral) -> new CoralSlabBlock(coral.getDeadBlock(type).get(), BlockBehaviour.Properties.ofFullCopy(coral.alive()))),
        DEAD_STAIRS(null,coral -> new StairBlock(coral.dead().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(coral.dead()))),
        STAIRS(DEAD_STAIRS, (type, coral) -> new CoralStairsBlock(coral.alive(), coral.getDeadBlock(type).get(), BlockBehaviour.Properties.ofFullCopy(coral.alive()))),
        DEAD_WALL(null,coral -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(coral.dead()))),
        WALL(DEAD_WALL, (type, coral) -> new CoralWallBlock(coral.getDeadBlock(type).get(), BlockBehaviour.Properties.ofFullCopy(coral.alive()))),

        DEAD_VERTICAL_SLAB(null,coral -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(coral.dead()))),
        VERTICAL_SLAB(DEAD_VERTICAL_SLAB, (type, coral) -> new CoralVerticalSlabBlock(coral.getDeadBlock(type).get(), BlockBehaviour.Properties.ofFullCopy(coral.alive()))),
        DEAD_QUARTER(null,coral -> new QuarterBlock(BlockBehaviour.Properties.ofFullCopy(coral.dead()))),
        QUARTER(DEAD_QUARTER, (type, coral) -> new CoralQuarterBlock(coral.getDeadBlock(type).get(), BlockBehaviour.Properties.ofFullCopy(coral.alive()))),
        DEAD_VERTICAL_QUARTER(null,coral -> new VerticalQuarterBlock(BlockBehaviour.Properties.ofFullCopy(coral.dead()))),
        VERTICAL_QUARTER(DEAD_VERTICAL_QUARTER, (type, coral) -> new CoralVerticalQuarterBlock(coral.getDeadBlock(type).get(), BlockBehaviour.Properties.ofFullCopy(coral.alive()))),
        DEAD_VERTICAL_CORNER(null,coral -> new VerticalCornerBlock(BlockBehaviour.Properties.ofFullCopy(coral.dead()))),
        VERTICAL_CORNER(DEAD_VERTICAL_CORNER, (type, coral) -> new CoralVerticalCornerBlock(coral.getDeadBlock(type).get(), BlockBehaviour.Properties.ofFullCopy(coral.alive()))),
        DEAD_CORNER_SLAB(null,coral -> new CornerSlabBlock(BlockBehaviour.Properties.ofFullCopy(coral.dead()))),
        CORNER_SLAB(DEAD_CORNER_SLAB, (type, coral) -> new CoralCornerSlabBlock(coral.getDeadBlock(type).get(), BlockBehaviour.Properties.ofFullCopy(coral.alive()))),
        DEAD_VERTICAL_CORNER_SLAB(null,coral -> new VerticalCornerSlabBlock(BlockBehaviour.Properties.ofFullCopy(coral.dead()))),
        VERTICAL_CORNER_SLAB(DEAD_VERTICAL_CORNER_SLAB, (type, coral) -> new CoralVerticalCornerSlabBlock(coral.getDeadBlock(type).get(), BlockBehaviour.Properties.ofFullCopy(coral.alive()))),
        DEAD_EIGHTH(null,coral -> new EighthBlock(BlockBehaviour.Properties.ofFullCopy(coral.dead()))),
        EIGHTH(DEAD_EIGHTH, (type, coral) -> new CoralEighthBlock(coral.getDeadBlock(type).get(), BlockBehaviour.Properties.ofFullCopy(coral.alive()))),
        ;

        @SuppressWarnings("unchecked")
        private static <B extends Block> Supplier<? extends B> getBlock(RegistryCoral coral, BlockType type)
        {
            return (Supplier<? extends B>) coral.getBlock(type);
        }

        private final @Nullable BlockType deadVariant;
        private final BiFunction<BlockType, RegistryCoral, Block> blockFactory;
        private final TriFunction<Block, Item.Properties, RegistryCoral, ? extends BlockItem> blockItemFactory;

        /** Master constructor */
        BlockType(
                @Nullable BlockType deadVariant,
                BiFunction<BlockType, RegistryCoral, Block> blockFactory,
                TriFunction<Block, Item.Properties, RegistryCoral, ? extends BlockItem> blockItemFactory) {
            this.deadVariant = deadVariant;
            this.blockFactory = blockFactory;
            this.blockItemFactory = blockItemFactory;
        }

        /** Keeps the coral -> syntax */
        BlockType(
                @Nullable BlockType deadVariant,
                Function<RegistryCoral, Block> blockFactory) {
            this(
                    deadVariant,
                    (self, coral) -> blockFactory.apply(coral),
                    BlockItem::new
            );
        }

        /** Keeps the (type, coral) -> syntax */
        BlockType(
                @Nullable BlockType deadVariant,
                BiFunction<BlockType, RegistryCoral, Block> blockFactory) {
            this(
                    deadVariant,
                    blockFactory,
                    BlockItem::new
            );
        }

        /** Custom BlockItem overload */
        BlockType(
                @Nullable BlockType deadVariant,
                BiFunction<BlockType, RegistryCoral, Block> blockFactory,
                BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory) {
            this(
                    deadVariant,
                    blockFactory,
                    (block, props, coral) -> blockItemFactory.apply(block, props)
            );
        }

        public BlockType dead() {
            return deadVariant == null ? this : deadVariant;
        }

        @Nullable
        public Function<Block, BlockItem> createBlockItem(RegistryCoral wood, Item.Properties properties)
        {
            return needsItem() ? block -> blockItemFactory.apply(block, properties, wood) : null;
        }

        public String coralName(RegistryCoral coral)
        {
            return switch (this)
            {
                case DEAD_STAIRS -> "dead_%s_stairs".formatted(coral.getSerializedName());
                case DEAD_SLAB -> "dead_%s_slab".formatted(coral.getSerializedName());
                case DEAD_WALL -> "dead_%s_wall".formatted(coral.getSerializedName());
                case DEAD_VERTICAL_SLAB -> "dead_%s_vertical_slab".formatted(coral.getSerializedName());
                case DEAD_QUARTER -> "dead_%s_quarter".formatted(coral.getSerializedName());
                case DEAD_VERTICAL_QUARTER -> "dead_%s_vertical_quarter".formatted(coral.getSerializedName());
                case DEAD_VERTICAL_CORNER -> "dead_%s_vertical_corner".formatted(coral.getSerializedName());
                case DEAD_CORNER_SLAB -> "dead_%s_corner_slab".formatted(coral.getSerializedName());
                case DEAD_VERTICAL_CORNER_SLAB -> "dead_%s_vertical_corner_slab".formatted(coral.getSerializedName());
                case DEAD_EIGHTH -> "dead_%s_eighth".formatted(coral.getSerializedName());
                default -> "%s_%s".formatted(coral.getSerializedName(), name().toLowerCase(Locale.ROOT));
            };
        }

        public boolean needsItem()
        {
            return true;
        }

        public Supplier<Block> create(RegistryCoral coral)
        {
            return () -> blockFactory.apply(this, coral);
        }
    }
}
