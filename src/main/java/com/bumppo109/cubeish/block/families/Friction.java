package com.bumppo109.cubeish.block.families;

import com.bumppo109.cubeish.block.ModBlocks;
import com.bumppo109.cubeish.block.shapes.friction.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.apache.commons.lang3.function.TriFunction;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public enum Friction implements RegistryFriction
{
    PACKED_ICE(() -> Blocks.PACKED_ICE),
    BLUE_ICE(() -> Blocks.BLUE_ICE)
    ;

    public static final Friction[] VALUES = values();

    private final String serializedName;
    private final Supplier<Block> base;
    private final BlockBehaviour.Properties properties;
    private final boolean hasStair;
    private final boolean hasSlab;

    Friction(Supplier<Block> base) {
        this.serializedName = this.name().toLowerCase(Locale.ROOT);
        this.base = base;
        this.properties = BlockBehaviour.Properties.ofFullCopy(base.get());
        this.hasStair = false;
        this.hasSlab = false;
    }

    Friction(Supplier<Block> base, BlockBehaviour.Properties properties, boolean hasStair, boolean hasSlab) {
        this.serializedName = this.name().toLowerCase(Locale.ROOT);
        this.base = base;
        this.properties = properties;
        this.hasStair = hasStair;
        this.hasSlab = hasSlab;
    }

    @Override
    public String getSerializedName()
    {
        return serializedName;
    }
    
    @Override
    public Supplier<Block> getBlock(BlockType type) {
        return ModBlocks.FRICTION.get(this).get(type);
    }
    
    public BlockBehaviour.Properties properties() {
        return properties;
    }

    @Override
    public Supplier<Block> getBase() {
        return base;
    }

    public boolean hasStair() {
        return hasStair;
    }
    
    public boolean hasSlab() {
        return hasSlab;
    }

    public String nameFor(BlockType shape) {
        String assetId = this.getSerializedName();

        return "%s_%s".formatted(assetId, shape);
    }

    public enum BlockType
    {
        SLAB(block -> new FrictionSlabBlock(block.properties())),
        STAIRS(block -> new FrictionStairBlock(block.getBase().get().defaultBlockState(), block.properties())),
        WALL(block -> new FrictionWallBlock(block.properties())),

        VERTICAL_SLAB(block -> new FrictionVerticalSlabBlock(block.properties())),
        QUARTER(block -> new FrictionQuarterBlock(block.properties())),
        VERTICAL_QUARTER(block -> new FrictionVerticalQuarterBlock(block.properties())),
        CORNER_SLAB(block -> new FrictionCornerSlabBlock(block.properties())),
        VERTICAL_CORNER_SLAB(block -> new FrictionVerticalCornerSlabBlock(block.properties())),
        VERTICAL_CORNER(block -> new FrictionVerticalCornerBlock(block.properties())),
        EIGHTH(block -> new FrictionEighthBlock(block.properties())),
        ;

        @SuppressWarnings("unchecked")
        private static <B extends Block> Supplier<? extends B> getBlock(RegistryFriction block, BlockType type)
        {
            return (Supplier<? extends B>) block.getBlock(type);
        }

        private final BiFunction<BlockType, RegistryFriction, Block> blockFactory;
        private final TriFunction<Block, Item.Properties, RegistryFriction, ? extends BlockItem> blockItemFactory;

        BlockType(Function<RegistryFriction, Block> blockFactory)
        {
            this((self, block) -> blockFactory.apply(block));
        }

        BlockType(BiFunction<BlockType, RegistryFriction, Block> blockFactory)
        {
            this(blockFactory, BlockItem::new);
        }

        BlockType(BiFunction<BlockType, RegistryFriction, Block> blockFactory, BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory)
        {
            this(blockFactory, (block, properties, self) -> blockItemFactory.apply(block, properties));
        }

        BlockType(BiFunction<BlockType, RegistryFriction, Block> blockFactory, TriFunction<Block, Item.Properties, RegistryFriction, ? extends BlockItem> blockItemFactory)
        {
            this.blockFactory = blockFactory;
            this.blockItemFactory = blockItemFactory;
        }

        public boolean canCreate(Friction block, BlockType blockType) {
            return switch (blockType) {
                case STAIRS -> !block.hasStair;
                case SLAB -> !block.hasSlab;
                default -> true;
            };
        }

        @Nullable
        public Function<Block, BlockItem> createBlockItem(RegistryFriction friction, Item.Properties properties)
        {
            return needsItem() ? block -> blockItemFactory.apply(block, properties, friction) : null;
        }

        public boolean needsItem()
        {
            return true;
        }

        public Supplier<Block> create(RegistryFriction block)
        {
            return () -> blockFactory.apply(this, block);
        }
    }
}
