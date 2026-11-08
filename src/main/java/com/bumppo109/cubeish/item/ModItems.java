package com.bumppo109.cubeish.item;

import com.bumppo109.cubeish.Cubeish;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Cubeish.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
