package lazy.baubles.capability;

import lazy.baubles.api.bauble.IBaublesItemHandler;
import lazy.baubles.api.cap.CapabilityBaubles;
import lazy.baubles.event.EventHandlerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class BaublesContainer extends ItemStackHandler implements IBaublesItemHandler {

    private final static int BAUBLE_SLOTS = 7;
    private final ItemStack[] previous = new ItemStack[BAUBLE_SLOTS];
    private final boolean[] changed = new boolean[BAUBLE_SLOTS];
    private boolean blockEvents = false;
    private final LivingEntity holder;

    public BaublesContainer(LivingEntity player) {
        super(BAUBLE_SLOTS);
        this.holder = player;
        Arrays.fill(this.previous, ItemStack.EMPTY);
    }

    @Override
    public void setSize(int size) {
        if (size != BAUBLE_SLOTS) System.out.println("Cannot resize baubles container");
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring
     * stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        var baubleCap = stack.getCapability(CapabilityBaubles.ITEM_BAUBLE);
        if (stack.isEmpty() || !baubleCap.isPresent()) return false;
        var bauble = baubleCap.orElseThrow(NullPointerException::new);
        return bauble.canEquip(holder) && bauble.getBaubleType(stack).hasSlot(slot);
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (stack.isEmpty() || this.isItemValidForSlot(slot, stack)) super.setStackInSlot(slot, stack);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (!this.isItemValidForSlot(slot, stack)) return stack;
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public boolean isEventBlocked() {
        return this.blockEvents;
    }

    @Override
    public void setEventBlock(boolean blockEvents) {
        this.blockEvents = blockEvents;
    }

    @Override
    protected void onContentsChanged(int slot) {
        this.changed[slot] = true;
    }

    public void tick() {
        for (int i = 0; i < getSlots(); i++) {
            var stack = getStackInSlot(i);
            stack.getCapability(CapabilityBaubles.ITEM_BAUBLE).ifPresent(b -> b.onWornTick(this.holder, stack));
        }
        this.sync();
    }

    private void sync() {
        if (!(holder instanceof ServerPlayer)) return;

        final var holder = (ServerPlayer) this.holder;

        List<ServerPlayer> receivers = null;
        for (byte i = 0; i < getSlots(); i++) {
            final var stack = getStackInSlot(i);
            boolean autoSync = stack.getCapability(CapabilityBaubles.ITEM_BAUBLE).map(b -> b.willAutoSync(this.holder)).orElse(false);
            if (changed[i] || autoSync && !ItemStack.isSame(stack, previous[i])) {
                if (receivers == null) {
                    receivers = new ArrayList<>(((ServerLevel) this.holder.level).getPlayers((serverPlayerEntity) -> true));
                    receivers.add(holder);
                }
                EventHandlerEntity.syncSlot(holder, i, stack, receivers);
                this.changed[i] = false;
                previous[i] = stack.copy();
            }
        }
    }
}
