package com.bumppo109.cubeish.block.families;

import com.bumppo109.cubeish.block.ModBlocks;
import com.bumppo109.cubeish.block.shapes.flammable.FlammableBlock;
import com.bumppo109.cubeish.block.shapes.magma.*;
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

public enum Magma implements RegistryMagma
{
    MAGMA(null, BlockBehaviour.Properties.ofFullCopy(Blocks.MAGMA_BLOCK),false,false)
    ;

    public static final Magma[] VALUES = values();

    private final String serializedName;
    private final Supplier<Block> base;
    private final BlockBehaviour.Properties properties;
    private final boolean hasStair;
    private final boolean hasSlab;

    Magma(Supplier<Block> base, BlockBehaviour.Properties properties, boolean hasStair, boolean hasSlab) {
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
        return ModBlocks.MAGMA.get(this).get(type);
    }
    
    public BlockBehaviour.Properties properties() {
        return properties;
    }

    @Override
    public Supplier<Block> getBase() {
        return base != null ? base : ModBlocks.MAGMA.get(this).get(BlockType.BLOCK);
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
        BLOCK(magma -> new MagmaBaseBlock(magma.properties())),
        SLAB(magma -> new MagmaSlabBlock(magma.properties())),
        STAIRS(magma -> new MagmaStairsBlock(magma.getBase().get().defaultBlockState(), magma.properties())),
        WALL(magma -> new MagmaWallBlock(magma.properties())),

        VERTICAL_SLAB(magma -> new MagmaVerticalSlabBlock(magma.properties())),
        QUARTER(magma -> new MagmaQuarterBlock(magma.properties())),
        VERTICAL_QUARTER(magma -> new MagmaVerticalQuarterBlock(magma.properties())),
        CORNER_SLAB(magma -> new MagmaCornerSlabBlock(magma.properties())),
        VERTICAL_CORNER_SLAB(magma -> new MagmaVerticalCornerSlabBlock(magma.properties())),
        VERTICAL_CORNER(magma -> new MagmaVerticalCornerBlock(magma.properties())),
        EIGHTH(magma -> new MagmaEighthBlock(magma.properties())),
        ;

        @SuppressWarnings("unchecked")
        private static <B extends Block> Supplier<? extends B> getBlock(RegistryMagma magma, BlockType type)
        {
            return (Supplier<? extends B>) magma.getBlock(type);
        }

        private final BiFunction<BlockType, RegistryMagma, Block> blockFactory;
        private final TriFunction<Block, Item.Properties, RegistryMagma, ? extends BlockItem> blockItemFactory;

        BlockType(Function<RegistryMagma, Block> blockFactory)
        {
            this((self, magma) -> blockFactory.apply(magma));
        }

        BlockType(BiFunction<BlockType, RegistryMagma, Block> blockFactory)
        {
            this(blockFactory, BlockItem::new);
        }

        BlockType(BiFunction<BlockType, RegistryMagma, Block> blockFactory, BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory)
        {
            this(blockFactory, (block, properties, self) -> blockItemFactory.apply(block, properties));
        }

        BlockType(BiFunction<BlockType, RegistryMagma, Block> blockFactory, TriFunction<Block, Item.Properties, RegistryMagma, ? extends BlockItem> blockItemFactory)
        {
            this.blockFactory = blockFactory;
            this.blockItemFactory = blockItemFactory;
        }

        public boolean canCreate(Magma magma, BlockType blockType) {
            return switch (blockType) {
                case BLOCK -> magma.base == null;
                case STAIRS -> !magma.hasStair;
                case SLAB -> !magma.hasSlab;
                default -> true;
            };
        }

        @Nullable
        public Function<Block, BlockItem> createBlockItem(RegistryMagma magma, Item.Properties properties)
        {
            return needsItem() ? block -> blockItemFactory.apply(block, properties, magma) : null;
        }

        public boolean needsItem()
        {
            return true;
        }

        public Supplier<Block> create(RegistryMagma magma)
        {
            return () -> blockFactory.apply(this, magma);
        }
    }
}
