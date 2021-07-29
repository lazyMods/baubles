package lazy.baubles;

import lazy.baubles.api.BaublesAPI;
import lazy.baubles.api.cap.CapabilityBaubles;
import lazy.baubles.client.gui.PlayerExpandedScreen;
import lazy.baubles.network.PacketHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

@Mod(BaublesAPI.MOD_ID)
public class Baubles {

    public static final KeyMapping KEY_BAUBLES = new KeyMapping("keybind.baublesinventory", GLFW.GLFW_KEY_B, "key.categories.inventory");

    public Baubles() {
        ModMenus.init();
        MinecraftForge.EVENT_BUS.addListener(this::setupCommon);
        MinecraftForge.EVENT_BUS.addListener(this::setupClient);
    }

    private void setupCommon(FMLCommonSetupEvent event) {
        CapabilityBaubles.register();
        PacketHandler.registerMessages();
    }

    private void setupClient(FMLClientSetupEvent event) {
        MenuScreens.register(ModMenus.PLAYER_BAUBLES.get(), PlayerExpandedScreen::new);
        ClientRegistry.registerKeyBinding(KEY_BAUBLES);
    }

    private void loadComplete(FMLLoadCompleteEvent event){
        //TODO: Add IRenderBauble layers.
        /*Map<String, EntityRenderer<? extends Player>> skinMap = Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap();
        PlayerRenderer render;
        render = skinMap.get("default");
        render.addLayer(new BaublesRenderLayer(render));

        render = skinMap.get("slim");
        render.addLayer(new BaublesRenderLayer(render));*/
    }
}
