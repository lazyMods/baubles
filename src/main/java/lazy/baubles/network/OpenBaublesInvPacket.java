package lazy.baubles.network;

import lazy.baubles.client.util.GuiProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Supplier;

public class OpenBaublesInvPacket {

    public OpenBaublesInvPacket(PacketBuffer buf) {
    }

    public OpenBaublesInvPacket() {
    }

    public void toBytes(PacketBuffer buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ctx.get().getSender().closeContainer();
            NetworkHooks.openGui(ctx.get().getSender(), new GuiProvider());
        });
        ctx.get().setPacketHandled(true);
    }
}
