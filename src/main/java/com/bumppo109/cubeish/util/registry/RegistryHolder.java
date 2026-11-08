//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bumppo109.cubeish.util.registry;

import net.neoforged.neoforge.registries.DeferredHolder;

public interface RegistryHolder<R, T extends R> extends IdHolder<T>, HolderHolder<R> {
    DeferredHolder<R, T> holder();

    default T get() {
        return (T)this.holder().get();
    }
}
