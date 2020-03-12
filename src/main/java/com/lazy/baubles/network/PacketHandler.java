package com.lazy.baubles.network;

import com.lazy.baubles.Baubles;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Baubles.MODID, "netchannel"), () -> "1.0", s -> true, s -> true);

        INSTANCE.registerMessage(nextID(), OpenBaublesInvPacket.class, OpenBaublesInvPacket::toBytes, OpenBaublesInvPacket::new, OpenBaublesInvPacket::handle);
        INSTANCE.registerMessage(nextID(), OpenNormalInvPacket.class, OpenNormalInvPacket::toBytes, OpenNormalInvPacket::new, OpenNormalInvPacket::handle);
        INSTANCE.registerMessage(nextID(), SyncPacket.class, SyncPacket::toBytes, SyncPacket::new, SyncPacket::handle);
    }
}
