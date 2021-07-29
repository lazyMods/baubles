package lazy.baubles.client.event;

import lazy.baubles.api.BaublesAPI;
import lazy.baubles.api.bauble.BaubleType;
import lazy.baubles.api.cap.CapabilityBaubles;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BaublesAPI.MOD_ID, value = Dist.CLIENT)
public class RingItemTooltip {

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event) {
        if (!event.getItemStack().isEmpty()) {
            if(event.getItemStack().getCapability(CapabilityBaubles.ITEM_BAUBLE).isPresent()){
                event.getItemStack().getCapability(CapabilityBaubles.ITEM_BAUBLE).ifPresent(bauble -> {
                    BaubleType bt = bauble.getBaubleType(event.getItemStack());
                    TranslatableComponent text = new TranslatableComponent("name." + bt);
                    text.withStyle(ChatFormatting.GOLD);
                    event.getToolTip().add(text);
                });
            }
        }
    }
}
