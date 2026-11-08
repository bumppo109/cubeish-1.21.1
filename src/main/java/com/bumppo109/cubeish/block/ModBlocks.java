package com.bumppo109.cubeish.block;

import com.bumppo109.cubeish.Cubeish;
import com.bumppo109.cubeish.block.families.*;
import com.bumppo109.cubeish.util.registry.Helpers;
import com.bumppo109.cubeish.util.registry.RegistrationHelpers;
import com.bumppo109.cubeish.util.registry.RegistryHolder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.bumppo109.cubeish.item.ModItems.ITEMS;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Cubeish.MODID);

    public static final Map<Magma, Map<Magma.BlockType, Id<Block>>> MAGMA = Helpers.mapOf(Magma.class, magma ->
            Arrays.stream(Magma.BlockType.values())
                    .filter(type -> type.canCreate(magma, type))
                    .collect(Collectors.toMap(
                            type -> type,
                            type -> register(magma.nameFor(type), createMagma(magma, type), type.createBlockItem(magma, new Item.Properties()))
                    ))
    );

    public static final Map<Colored, Map<ColorType, Map<ExtendedShape, Id<Block>>>> COLORED_BLOCKS =
            Helpers.mapOf(Colored.class, asset ->
                    Helpers.mapOf(ColorType.class, color ->
                            Helpers.mapOf(ExtendedShape.class, asset::canCreate,
                                    shape -> register(asset.nameFor(color, shape),
                                            () -> asset.create(shape),block -> shape.createBlockItem(block,new Item.Properties())
                                    )
                            )
                    )
            );

    public static final Map<
            ExtendedShapeBlocks,
            Map<ExtendedShape, Id<Block>>
            > MISC_BLOCKS =
            Helpers.mapOf(ExtendedShapeBlocks.class, asset ->
                    Helpers.mapOf(
                            ExtendedShape.class,
                            asset::canCreate,
                            shape -> register(
                                    asset.nameFor(shape),
                                    () -> asset.create(shape),
                                    block -> shape.createBlockItem(
                                            block,
                                            new Item.Properties()
                                    )
                            )
                    )
            );

    public static final Map<Flammable, Map<Flammable.BlockType, Id<Block>>> WOODS = Helpers.mapOf(Flammable.class, wood ->
                    Arrays.stream(Flammable.BlockType.values())
                                    .filter(type -> type.canCreate(wood, type))
                                            .collect(Collectors.toMap(
                                                    type -> type,
                                                    type -> register(wood.nameFor(type), createWood(wood, type), type.createBlockItem(wood, new Item.Properties()))
                                            ))
    );

    public static final Map<
            Weathering,
            Map<WeatheringState,
                    Map<WaxingState,
                            Map<ExtendedShape, Id<Block>>>>
            > WEATHERING_METALS =
            Helpers.mapOf(Weathering.class, metal ->
                    Helpers.mapOf(WeatheringState.class, state ->
                            Helpers.mapOf(WaxingState.class, waxing ->
                                    Helpers.mapOf(
                                            ExtendedShape.class,
                                            metal::canCreate,
                                            shape -> {
                                                Weathering.WeatheringAsset asset =
                                                        new Weathering.WeatheringAsset(
                                                                metal,
                                                                state,
                                                                waxing,
                                                                shape
                                                        );

                                                return register(
                                                        Weathering.metalName(metal, state, waxing, shape),
                                                        () -> shape.create(
                                                                asset.getBase(),
                                                                asset.properties()
                                                        ),
                                                        block -> shape.createBlockItem(
                                                                block,
                                                                new Item.Properties()
                                                        )
                                                );
                                            }
                                    )
                            )
                    )
            );

    public static final Map<Coral, Map<Coral.BlockType, Id<Block>>> CORALS = Helpers.mapOf(Coral.class, coral ->
            Helpers.mapOf(Coral.BlockType.class, type ->
                    register(type.coralName(coral), createCoral(coral, type), type.createBlockItem(coral, new Item.Properties()))
            )
    );



    private static <T extends Block> Id<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory)
    {
        return new Id<>(RegistrationHelpers.registerBlock(ModBlocks.BLOCKS, ITEMS, name, blockSupplier, blockItemFactory));
    }

    private static <T extends Block> DeferredBlock<T> registerBlockNoItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {

        DeferredBlock<T> registered = BLOCKS.register(name, block);
        registerBlockItem(name, registered);

        return registered;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        Cubeish.LOGGER.debug("Current Block - {}", name);
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static Supplier<Block> createWood(Flammable wood, Flammable.BlockType blockType)
    {
        return blockType.create(wood);
    }

    public static Supplier<Block> createMagma(Magma magma, Magma.BlockType blockType)
    {
        return blockType.create(magma);
    }

    public static Supplier<Block> createCoral(Coral coral, Coral.BlockType blockType)
    {
        return blockType.create(coral);
    }
    
    public record Id<T extends Block>(DeferredHolder<Block, T> holder) implements RegistryHolder<Block, T>, ItemLike
    {
        @Override
        public Item asItem()
        {
            return get().asItem();
        }
    }
}