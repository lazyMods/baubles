package lazy.baubles.network.msg;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class OpenNormalInvPacket {

    public OpenNormalInvPacket(FriendlyByteBuf buf) {
    }

    public OpenNormalInvPacket() {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var playerEntity = ctx.get().getSender();
            if (playerEntity != null) {
                playerEntity.containerMenu.removed(playerEntity);
                playerEntity.containerMenu = playerEntity.inventoryMenu;
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
