package com.bumppo109.cubeish;

import com.bumppo109.cubeish.block.ModBlocks;
import com.bumppo109.cubeish.block.families.Colored;
import com.bumppo109.cubeish.block.families.ExtendedShapeBlocks;
import com.bumppo109.cubeish.block.families.Weathering;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.util.function.Predicate;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = Cubeish.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = Cubeish.MODID, value = Dist.CLIENT)
public class CubeishClient {
    public CubeishClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {

        final RenderType solid = RenderType.solid();
        final RenderType cutout = RenderType.cutout();
        final RenderType cutoutMipped = RenderType.cutoutMipped();
        final RenderType translucent = RenderType.translucent();
        final Predicate<RenderType> ghostBlock = rt -> rt == cutoutMipped || rt == Sheets.translucentCullBlockSheet();

        ModBlocks.COLORED_BLOCKS.forEach((block, colorMap) -> {
            colorMap.forEach(((colorType, blockTypeIdMap) -> {
                blockTypeIdMap.forEach((blockType, blockId) -> {
                    if(block.equals(Colored.STAINED_GLASS)) {
                        ItemBlockRenderTypes.setRenderLayer(blockId.get(), translucent);
                    }
                });
            }));
        });

        ModBlocks.MISC_BLOCKS.forEach((block, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                if(block.equals(ExtendedShapeBlocks.GLASS)) {
                    ItemBlockRenderTypes.setRenderLayer(blockId.get(), cutout);
                }
                if(block.equals(ExtendedShapeBlocks.TINTED_GLASS)) {
                    ItemBlockRenderTypes.setRenderLayer(blockId.get(), translucent);
                }
            });
        });

        ModBlocks.WEATHERING_BLOCKS.forEach(((weathering, weatheringStateMapMap) -> {
            weatheringStateMapMap.forEach(((weatheringState, waxingStateMapMap) -> {
                waxingStateMapMap.forEach((waxingState, extendedShapeIdMap) -> {
                    extendedShapeIdMap.forEach((shape, blockId) -> {
                        if (weathering.equals(Weathering.COPPER_GRATE)) {
                            ItemBlockRenderTypes.setRenderLayer(blockId.get(), cutout);
                        }
                    });
                });
            }));
        }));
    }
}
