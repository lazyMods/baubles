package lazy.baubles.proxy;

import lazy.baubles.Baubles;
import lazy.baubles.client.gui.PlayerExpandedScreen;
import lazy.baubles.client.renderer.BaublesRenderLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

import java.util.Map;

public class ClientProxy extends CommonProxy {

    public static final KeyBinding KEY_BAUBLES = new KeyBinding("keybind.baublesinventory", GLFW.GLFW_KEY_B, "key.categories.inventory");

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void addToBus() {
        super.addToBus();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupOnLoaded);
    }

    public void setupClient(FMLClientSetupEvent event) {
        ScreenManager.register(Baubles.Registration.PLAYER_BAUBLES, PlayerExpandedScreen::new);
        ClientRegistry.registerKeyBinding(KEY_BAUBLES);
    }

    public void setupOnLoaded(FMLLoadCompleteEvent event) {
        this.addLayers();
    }

    private void addLayers() {
        Map<String, PlayerRenderer> skinMap = Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap();
        PlayerRenderer render;
        render = skinMap.get("default");
        render.addLayer(new BaublesRenderLayer(render));

        render = skinMap.get("slim");
        render.addLayer(new BaublesRenderLayer(render));
    }
}
