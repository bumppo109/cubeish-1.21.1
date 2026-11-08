//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bumppo109.cubeish.util.registry;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public interface IdHolder<T> extends Supplier<T> {
    DeferredHolder<? super T, T> holder();

    default ResourceLocation getId() {
        return this.holder().getId();
    }
}
