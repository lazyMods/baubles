package lazy.baubles.client.event;

import lazy.baubles.client.gui.widget.BaublesButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RenderBaublesButton {

    @SuppressWarnings("rawtypes")
    @SubscribeEvent
    public static void onGuiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
        Screen screen = event.getGui();
        if (screen instanceof EffectRenderingInventoryScreen effectRenderingInventoryScreen) {
            if (event.getWidgetList() != null) {
                event.addWidget(new BaublesButton(effectRenderingInventoryScreen, 64, 9, 10, 10));
            }
        }
    }
}