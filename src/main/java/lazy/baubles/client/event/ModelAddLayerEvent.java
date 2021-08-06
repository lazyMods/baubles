package lazy.baubles.client.event;

import com.google.common.base.Preconditions;
import lazy.baubles.api.BaublesAPI;
import lazy.baubles.client.renderer.BaublesRenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = BaublesAPI.MOD_ID, value = Dist.CLIENT)
public class ModelAddLayerEvent {

    @SubscribeEvent
    public static void onAddLayer(EntityRenderersEvent.AddLayers event) {
        var defaultModel = (PlayerRenderer) event.getSkin("default");
        var slimModel = (PlayerRenderer) event.getSkin("slim");
        Preconditions.checkNotNull(defaultModel);
        Preconditions.checkNotNull(slimModel);
        defaultModel.addLayer(new BaublesRenderLayer<>(defaultModel));
        slimModel.addLayer(new BaublesRenderLayer<>(slimModel));
    }
}
