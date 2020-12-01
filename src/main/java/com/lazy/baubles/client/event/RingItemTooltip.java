package com.lazy.baubles.client.event;

import com.lazy.baubles.api.BaublesAPI;
import com.lazy.baubles.api.bauble.BaubleType;
import com.lazy.baubles.api.cap.BaublesCapabilities;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BaublesAPI.MOD_ID, value = Dist.CLIENT)
public class RingItemTooltip {

    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event) {
        if (!event.getItemStack().isEmpty()) {
            event.getItemStack().getCapability(BaublesCapabilities.ITEM_BAUBLE).ifPresent(bauble -> {
                BaubleType bt = bauble.getBaubleType();
                TranslationTextComponent text = new TranslationTextComponent("name." + bt);
                text.mergeStyle(TextFormatting.GOLD);
                event.getToolTip().add(text);
            });
        }
    }
}
