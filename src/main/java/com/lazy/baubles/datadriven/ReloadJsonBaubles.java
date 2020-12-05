package com.lazy.baubles.datadriven;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ReloadJsonBaubles {

    @SubscribeEvent
    public static void onReloadCommand(CommandEvent event) {
        if (!ForgeRegistries.ITEMS.containsKey(new ResourceLocation("baubles:ring"))) return;
        if (event.getParseResults().getReader().getRead().equals("/reload")) {
            BaubleJsonItem ring = (BaubleJsonItem) ForgeRegistries.ITEMS.getValue(new ResourceLocation("baubles:ring"));
            if (ring != null) ring.setModel(BaubleJson.loadBaubles());
        }
    }
}
