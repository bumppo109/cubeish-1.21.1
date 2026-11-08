package com.bumppo109.cubeish.block.families;

import com.bumppo109.cubeish.block.ModBlocks;
import com.bumppo109.cubeish.block.shapes.flammable.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.apache.commons.lang3.function.TriFunction;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public enum Flammable implements RegistryFlammable
{
    ACACIA(() -> Blocks.ACACIA_PLANKS, true, true),
    BIRCH(() -> Blocks.BIRCH_PLANKS, true, true),
    CHERRY(() -> Blocks.CHERRY_PLANKS, true, true),
    DARK_OAK(() -> Blocks.DARK_OAK_PLANKS, true, true),
    JUNGLE(() -> Blocks.JUNGLE_PLANKS, true, true),
    MANGROVE(() -> Blocks.MANGROVE_PLANKS, true, true),
    OAK(() -> Blocks.OAK_PLANKS, true, true),
    SPRUCE(() -> Blocks.SPRUCE_PLANKS, true, true),

    ACACIA_WOOD(() -> Blocks.ACACIA_LOG, logProp(MapColor.STONE), false, false),
    BIRCH_WOOD(() -> Blocks.BIRCH_LOG, logProp(MapColor.STONE), false, false),
    CHERRY_WOOD(() -> Blocks.CHERRY_LOG, logProp(MapColor.STONE), false, false),
    DARK_OAK_WOOD(() -> Blocks.DARK_OAK_LOG, logProp(MapColor.STONE), false, false),
    JUNGLE_WOOD(() -> Blocks.JUNGLE_LOG, logProp(MapColor.STONE), false, false),
    MANGROVE_WOOD(() -> Blocks.MANGROVE_LOG, logProp(MapColor.STONE), false, false),
    OAK_WOOD(() -> Blocks.OAK_LOG, logProp(MapColor.STONE), false, false),
    SPRUCE_WOOD(() -> Blocks.SPRUCE_LOG, logProp(MapColor.STONE), false, false),

    STRIPPED_ACACIA_WOOD(() -> Blocks.STRIPPED_ACACIA_LOG, logProp(MapColor.COLOR_ORANGE), false, false),
    STRIPPED_BIRCH_WOOD(() -> Blocks.STRIPPED_BIRCH_LOG, logProp(MapColor.SAND), false, false),
    STRIPPED_CHERRY_WOOD(() -> Blocks.STRIPPED_CHERRY_LOG, logProp(MapColor.TERRACOTTA_WHITE), false, false),
    STRIPPED_DARK_OAK_WOOD(() -> Blocks.STRIPPED_DARK_OAK_LOG, logProp(MapColor.COLOR_BROWN), false, false),
    STRIPPED_JUNGLE_WOOD(() -> Blocks.STRIPPED_JUNGLE_LOG, logProp(MapColor.PODZOL), false, false),
    STRIPPED_MANGROVE_WOOD(() -> Blocks.STRIPPED_MANGROVE_LOG, logProp(MapColor.COLOR_RED), false, false),
    STRIPPED_OAK_WOOD(() -> Blocks.STRIPPED_OAK_LOG, logProp(MapColor.WOOD), false, false),
    STRIPPED_SPRUCE_WOOD(() -> Blocks.STRIPPED_SPRUCE_LOG, logProp(MapColor.WOOD), false, false),
    
    BAMBOO_BLOCK(() -> Blocks.BAMBOO_BLOCK, BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.BAMBOO_WOOD).ignitedByLava(), false, false),
    STRIPPED_BAMBOO_BLOCK(() -> Blocks.STRIPPED_BAMBOO_BLOCK, BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.BAMBOO_WOOD).ignitedByLava(), false, false),
    BAMBOO(() -> Blocks.BAMBOO_PLANKS,true, true),
    BAMBOO_MOSAIC(() -> Blocks.BAMBOO_MOSAIC,true, true)
    ;

    public static final Flammable[] VALUES = values();

    private final String serializedName;
    private final Supplier<Block> base;
    private final BlockBehaviour.Properties properties;
    private final boolean hasStair;
    private final boolean hasSlab;

    Flammable(Supplier<Block> base, boolean hasStair, boolean hasSlab) {
        this.serializedName = this.name().toLowerCase(Locale.ROOT);
        this.base = base;
        this.properties = BlockBehaviour.Properties.ofFullCopy(base.get());
        this.hasStair = hasStair;
        this.hasSlab = hasSlab;
    }

    Flammable(Supplier<Block> base, BlockBehaviour.Properties properties, boolean hasStair, boolean hasSlab) {
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
        return ModBlocks.WOODS.get(this).get(type);
    }
    
    public BlockBehaviour.Properties properties() {
        return properties;
    }
    
    public boolean hasStair() {
        return hasStair;
    }
    
    public boolean hasSlab() {
        return hasSlab;
    }
    
    public Supplier<Block> getBase() {
        return base != null ? base : ModBlocks.WOODS.get(this).get(BlockType.BASE);
    }
    
    public int flammability() {
        return this.name().endsWith("_WOOD") ? 5 : 20;
    }

    public static BlockBehaviour.Properties logProp(MapColor mapColor) {
        return BlockBehaviour.Properties.of().mapColor(mapColor).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava();
    }

    public String nameFor(BlockType shape) {
        String assetId = this.getSerializedName();

        return "%s_%s".formatted(assetId, shape);
    }

    public enum BlockType
    {
        BASE(wood -> new FlammableBlock(wood.properties(), wood.flammability(), 5)),
        SLAB(wood -> new FlammableSlabBlock(wood.properties(), wood.flammability(), 5)),
        STAIRS(wood -> new FlammableStairsBlock(wood.getBase().get().defaultBlockState(), wood.properties(), wood.flammability(), 5)),
        WALL(wood -> new FlammableWallBlock(wood.properties(), wood.flammability(), 5)),

        VERTICAL_SLAB(wood -> new FlammableVerticalSlabBlock(wood.properties(), wood.flammability(), 5)),
        QUARTER(wood -> new FlammableQuarterBlock(wood.properties(), wood.flammability(), 5)),
        VERTICAL_QUARTER(wood -> new FlammableVerticalQuarterBlock(wood.properties(), wood.flammability(), 5)),
        CORNER_SLAB(wood -> new FlammableCornerSlabBlock(wood.properties(), wood.flammability(), 5)),
        VERTICAL_CORNER_SLAB(wood -> new FlammableVerticalCornerSlabBlock(wood.properties(), wood.flammability(), 5)),
        VERTICAL_CORNER(wood -> new FlammableVerticalCornerBlock(wood.properties(), wood.flammability(), 5)),
        EIGHTH(wood -> new FlammableEighthBlock(wood.properties(), wood.flammability(), 5)),
        ;

        @SuppressWarnings("unchecked")
        private static <B extends Block> Supplier<? extends B> getBlock(RegistryFlammable wood, BlockType type)
        {
            return (Supplier<? extends B>) wood.getBlock(type);
        }

        private final BiFunction<BlockType, RegistryFlammable, Block> blockFactory;
        private final TriFunction<Block, Item.Properties, RegistryFlammable, ? extends BlockItem> blockItemFactory;

        BlockType(Function<RegistryFlammable, Block> blockFactory)
        {
            this((self, wood) -> blockFactory.apply(wood));
        }

        BlockType(BiFunction<BlockType, RegistryFlammable, Block> blockFactory)
        {
            this(blockFactory, BlockItem::new);
        }

        BlockType(BiFunction<BlockType, RegistryFlammable, Block> blockFactory, BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory)
        {
            this(blockFactory, (block, properties, self) -> blockItemFactory.apply(block, properties));
        }

        BlockType(BiFunction<BlockType, RegistryFlammable, Block> blockFactory, TriFunction<Block, Item.Properties, RegistryFlammable, ? extends BlockItem> blockItemFactory)
        {
            this.blockFactory = blockFactory;
            this.blockItemFactory = blockItemFactory;
        }

        public boolean canCreate(Flammable wood, BlockType blockType) {
            return switch (blockType) {
                case BASE -> wood.base == null;
                case STAIRS -> !wood.hasStair;
                case SLAB -> !wood.hasSlab;
                default -> true;
            };
        }

        @Nullable
        public Function<Block, BlockItem> createBlockItem(RegistryFlammable wood, Item.Properties properties)
        {
            return needsItem() ? block -> blockItemFactory.apply(block, properties, wood) : null;
        }

        public boolean needsItem()
        {
            return true;
        }

        public Supplier<Block> create(RegistryFlammable wood)
        {
            return () -> blockFactory.apply(this, wood);
        }
    }
}
