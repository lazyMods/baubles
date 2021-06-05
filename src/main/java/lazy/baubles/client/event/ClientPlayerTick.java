package lazy.baubles.client.event;

import lazy.baubles.api.BaublesAPI;
import lazy.baubles.network.OpenBaublesInvPacket;
import lazy.baubles.network.PacketHandler;
import lazy.baubles.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BaublesAPI.MOD_ID, value = Dist.CLIENT)
public class ClientPlayerTick {

    @SubscribeEvent
    public static void playerTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (ClientProxy.KEY_BAUBLES.consumeClick() && Minecraft.getInstance().isWindowActive()) {
                PacketHandler.INSTANCE.sendToServer(new OpenBaublesInvPacket());
            }
        }
    }
}