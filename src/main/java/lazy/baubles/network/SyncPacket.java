package lazy.baubles.network;

import lazy.baubles.api.cap.BaublesCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncPacket {

    public int playerId;
    public byte slot;
    ItemStack bauble;

    public SyncPacket(PacketBuffer buf) {
        this.playerId = buf.readInt();
        this.slot = buf.readByte();
        this.bauble = buf.readItem();
    }

    public SyncPacket(int playerId, byte slot, ItemStack bauble) {
        this.playerId = playerId;
        this.slot = slot;
        this.bauble = bauble;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(this.playerId);
        buf.writeByte(this.slot);
        buf.writeItem(this.bauble);
    }

    @SuppressWarnings("ConstantConditions")
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientWorld world = Minecraft.getInstance().level;
            if (world == null) return;
            Entity p = world.getEntity(playerId);
            if (p instanceof PlayerEntity) {
                p.getCapability(BaublesCapabilities.BAUBLES).ifPresent(b -> b.setStackInSlot(slot, bauble));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
