
package com.bumppo109.cubeish.event;

import com.bumppo109.cubeish.Cubeish;
import com.bumppo109.cubeish.block.shapes.magma.ContactDamageHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = Cubeish.MODID)
public class ContactDamageEvents {

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        ContactDamageHandler.checkContact(event.getEntity());
    }
}
