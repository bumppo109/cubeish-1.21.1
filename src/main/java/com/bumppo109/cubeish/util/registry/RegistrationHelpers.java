//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bumppo109.cubeish.util.registry;

import com.mojang.datafixers.types.Type;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.Builder;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class RegistrationHelpers {
    public static <T extends Block> DeferredHolder<Block, T> registerBlock(DeferredRegister<Block> blocks, DeferredRegister<Item> items, String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory) {
        String actualName = name.toLowerCase(Locale.ROOT);
        DeferredHolder<Block, T> block = blocks.register(actualName, blockSupplier);
        if (blockItemFactory != null) {
            items.register(actualName, () -> (BlockItem)blockItemFactory.apply((T) block.get()));
        }

        return block;
    }

    public static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(DeferredRegister<BlockEntityType<?>> blockEntities, String name, BlockEntityType.BlockEntitySupplier<T> factory, Supplier<? extends Block> block) {
        return blockEntities.register(name, () -> Builder.of(factory, new Block[]{(Block)block.get()}).build((Type)null));
    }

    public static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(DeferredRegister<BlockEntityType<?>> blockEntities, String name, BlockEntityType.BlockEntitySupplier<T> factory, Stream<? extends Supplier<? extends Block>> blocks) {
        return blockEntities.register(name, () -> Builder.of(factory, (Block[])blocks.map(Supplier::get).toArray((x$0) -> new Block[x$0])).build((Type)null));
    }

    public static <C extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<C>> registerContainer(DeferredRegister<MenuType<?>> containers, String name, IContainerFactory<C> factory) {
        return containers.register(name, () -> new MenuType(factory, FeatureFlags.DEFAULT_FLAGS));
    }
}
