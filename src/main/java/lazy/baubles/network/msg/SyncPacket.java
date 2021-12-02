package lazy.baubles.network.msg;

import lazy.baubles.api.cap.CapabilityBaubles;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncPacket {

    private final int playerId;
    private final byte slot;
    private final ItemStack bauble;

    public SyncPacket(FriendlyByteBuf buf) {
        this.playerId = buf.readInt();
        this.slot = buf.readByte();
        this.bauble = buf.readItem();
    }

    public SyncPacket(int playerId, byte slot, ItemStack bauble) {
        this.playerId = playerId;
        this.slot = slot;
        this.bauble = bauble;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.playerId);
        buf.writeByte(this.slot);
        buf.writeItem(this.bauble);
    }

    @SuppressWarnings("ConstantConditions")
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var world = Minecraft.getInstance().level;
            if (world == null) return;
            var p = world.getEntity(playerId);
            if (p instanceof Player) {
                p.getCapability(CapabilityBaubles.BAUBLES).ifPresent(b -> b.setStackInSlot(slot, bauble));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
