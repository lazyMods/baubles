package lazy.baubles.client.event;

import lazy.baubles.api.BaublesAPI;
import lazy.baubles.api.cap.CapabilityBaubles;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BaublesAPI.MOD_ID, value = Dist.CLIENT)
public class RingItemTooltip {

    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event) {
        if (!event.getItemStack().isEmpty()) {
            if (event.getItemStack().getCapability(CapabilityBaubles.ITEM_BAUBLE).isPresent()) {
                event.getItemStack().getCapability(CapabilityBaubles.ITEM_BAUBLE).ifPresent(bauble -> {
                    var bt = bauble.getBaubleType(event.getItemStack());
                    var text = Component.translatable("name." + bt);
                    text.withStyle(ChatFormatting.GOLD);
                    event.getToolTip().add(text);
                });
            }
        }
    }
}
