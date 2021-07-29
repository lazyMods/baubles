package lazy.baubles.proxy;

import lazy.baubles.api.bauble.IBauble;
import lazy.baubles.api.cap.IBaublesItemHandler;
import lazy.baubles.network.PacketHandler;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class CommonProxy {

    public void init() {
        this.addToBus();
    }

    public void addToBus() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupCommon);
    }

    public void setupCommon(FMLCommonSetupEvent event) {
        this.capRegistry();
        PacketHandler.registerMessages();
    }

    private void capRegistry() {
        CapabilityManager.INSTANCE.register(IBauble.class);
        CapabilityManager.INSTANCE.register(IBaublesItemHandler.class);
    }
}
