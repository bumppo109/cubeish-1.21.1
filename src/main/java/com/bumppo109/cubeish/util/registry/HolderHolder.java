//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bumppo109.cubeish.util.registry;

import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Objects;

public interface HolderHolder<R> {
    DeferredHolder<R, ? extends R> holder();

    default ResourceKey<R> key() {
        return (ResourceKey)Objects.requireNonNull(this.holder().getKey());
    }
}
