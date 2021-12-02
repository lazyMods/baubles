package lazy.baubles;

import lazy.baubles.api.BaublesAPI;
import lazy.baubles.api.cap.CapabilityBaubles;
import lazy.baubles.client.gui.PlayerExpandedScreen;
import lazy.baubles.network.PacketHandler;
import lazy.baubles.setup.ModConfigs;
import lazy.baubles.setup.ModItems;
import lazy.baubles.setup.ModMenus;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

@Mod(BaublesAPI.MOD_ID)
public class Baubles {

    public static KeyMapping KEY_BAUBLES = null;

    public Baubles() {
        ModConfigs.registerAndLoadConfig();
        ModItems.init();
        ModMenus.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupCommon);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
    }

    private void setupCommon(FMLCommonSetupEvent event) {
        PacketHandler.registerMessages();
    }

    private void setupClient(FMLClientSetupEvent event) {
        MenuScreens.register(ModMenus.PLAYER_BAUBLES.get(), PlayerExpandedScreen::new);
        KEY_BAUBLES = new KeyMapping("keybind.baublesinventory", GLFW.GLFW_KEY_B, "key.categories.inventory");
        ClientRegistry.registerKeyBinding(KEY_BAUBLES);
    }
}
