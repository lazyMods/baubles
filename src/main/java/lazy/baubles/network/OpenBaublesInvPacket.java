package lazy.baubles.network;

import lazy.baubles.client.util.GuiProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Supplier;

public class OpenBaublesInvPacket {

    public OpenBaublesInvPacket(PacketBuffer buf) {
    }

    public OpenBaublesInvPacket() {
    }

    public void toBytes(PacketBuffer buf) {}

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity playerEntity = ctx.get().getSender();
            if (playerEntity != null) {
                playerEntity.closeContainer();
                NetworkHooks.openGui(playerEntity, new GuiProvider());
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
