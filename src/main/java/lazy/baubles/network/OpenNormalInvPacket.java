package lazy.baubles.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenNormalInvPacket {

    public OpenNormalInvPacket(FriendlyByteBuf buf) {
    }

    public OpenNormalInvPacket() {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer playerEntity = ctx.get().getSender();
            if (playerEntity != null) {
                playerEntity.containerMenu.removed(playerEntity);
                playerEntity.containerMenu = playerEntity.inventoryMenu;
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
