package lazy.baubles.client.event;

import lazy.baubles.client.gui.widget.BaublesButton;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RenderBaublesButton {

    @SubscribeEvent
    public static void onGuiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
        Screen screen = event.getGui();
        if (screen instanceof DisplayEffectsScreen) {
            DisplayEffectsScreen displayEffectsScreen = (DisplayEffectsScreen) screen;
            if (event.getWidgetList() != null) {
                event.addWidget(new BaublesButton(displayEffectsScreen, 64, 9, 10, 10));
            }
        }
    }
}