package lazy.baubles.proxy;

import lazy.baubles.Baubles;
import lazy.baubles.client.gui.PlayerExpandedScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class ClientProxy extends CommonProxy {

    public static final KeyMapping KEY_BAUBLES = new KeyMapping("keybind.baublesinventory", GLFW.GLFW_KEY_B, "key.categories.inventory");

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
        MenuScreens.register(Baubles.Registration.PLAYER_BAUBLES, PlayerExpandedScreen::new);
        ClientRegistry.registerKeyBinding(KEY_BAUBLES);
    }

    public void setupOnLoaded(FMLLoadCompleteEvent event) {
        this.addLayers();
    }

    //TODO
    private void addLayers() {
        /*Map<String, EntityRenderer<? extends Player>> skinMap = Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap();
        PlayerRenderer render;
        render = skinMap.get("default");
        render.addLayer(new BaublesRenderLayer(render));

        render = skinMap.get("slim");
        render.addLayer(new BaublesRenderLayer(render));*/
    }
}
