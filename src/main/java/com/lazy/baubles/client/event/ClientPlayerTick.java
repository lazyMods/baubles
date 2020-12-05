package com.lazy.baubles.client.event;

import com.lazy.baubles.Baubles;
import com.lazy.baubles.api.BaublesAPI;
import com.lazy.baubles.network.OpenBaublesInvPacket;
import com.lazy.baubles.network.PacketHandler;
import com.lazy.baubles.proxy.ClientProxy;
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
            if (ClientProxy.KEY_BAUBLES.isPressed() && Minecraft.getInstance().isGameFocused()) {
                PacketHandler.INSTANCE.sendToServer(new OpenBaublesInvPacket());
            }
        }
    }
}