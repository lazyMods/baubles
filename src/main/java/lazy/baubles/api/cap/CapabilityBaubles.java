package lazy.baubles.api.cap;

import lazy.baubles.api.bauble.IBauble;
import lazy.baubles.api.bauble.IBaublesItemHandler;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class CapabilityBaubles {
    public static final Capability<IBaublesItemHandler> BAUBLES = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<IBauble> ITEM_BAUBLE = CapabilityManager.get(new CapabilityToken<>() {});
}