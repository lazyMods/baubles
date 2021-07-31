package lazy.baubles.api.cap;

import lazy.baubles.api.bauble.IBauble;
import lazy.baubles.api.bauble.IBaublesItemHandler;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityBaubles {

    @CapabilityInject(IBaublesItemHandler.class)
    public static final Capability<IBaublesItemHandler> BAUBLES = null;

    @CapabilityInject(IBauble.class)
    public static final Capability<IBauble> ITEM_BAUBLE = null;

    public static void register(){
        System.out.println("Registering baubles capabilities...");
        CapabilityManager.INSTANCE.register(IBaublesItemHandler.class);
        CapabilityManager.INSTANCE.register(IBauble.class);
    }
}