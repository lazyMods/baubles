package lazy.baubles.capability;

import lazy.baubles.api.bauble.IBauble;
import lazy.baubles.api.cap.BaublesCapabilities;
import lazy.baubles.api.cap.IBaublesItemHandler;
import lazy.baubles.event.EventHandlerEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.antlr.v4.runtime.atn.SemanticContext;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaublesContainer extends ItemStackHandler implements IBaublesItemHandler {

    private final static int BAUBLE_SLOTS = 7;
    private final ItemStack[] previous = new ItemStack[BAUBLE_SLOTS];
    private boolean[] changed = new boolean[BAUBLE_SLOTS];
    private boolean blockEvents = false;
    private LivingEntity holder;

    public BaublesContainer(LivingEntity player) {
        super(BAUBLE_SLOTS);
        this.holder = player;
        Arrays.fill(previous, ItemStack.EMPTY);
    }

    @Override
    public void setSize(int size) {
        if (size != BAUBLE_SLOTS)
            System.out.println("Cannot resize baubles container");
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring
     * stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        LazyOptional<IBauble> opt = stack.getCapability(BaublesCapabilities.ITEM_BAUBLE);
        if (stack.isEmpty() || !opt.isPresent())
            return false;
        IBauble bauble = opt.orElseThrow(NullPointerException::new);
        return bauble.canEquip(holder) && bauble.getBaubleType(stack).hasSlot(slot);
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (stack.isEmpty() || this.isItemValidForSlot(slot, stack)) {
            super.setStackInSlot(slot, stack);
        }
    }

    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (!this.isItemValidForSlot(slot, stack)) return stack;
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public boolean isEventBlocked() {
        return blockEvents;
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
            ItemStack stack = getStackInSlot(i);
            stack.getCapability(BaublesCapabilities.ITEM_BAUBLE)
                    .ifPresent(b -> b.onWornTick(holder, stack));
        }
        sync();
    }

    private void sync() {
        if (!(holder instanceof ServerPlayer)) {
            return;
        }

        List<Player> receivers = null;
        for (byte i = 0; i < getSlots(); i++) {
            ItemStack stack = getStackInSlot(i);
            boolean autosync = stack.getCapability(BaublesCapabilities.ITEM_BAUBLE).map(b -> b.willAutoSync(holder)).orElse(false);
            if (changed[i] || autosync && !ItemStack.isSame(stack, previous[i])) {
                if (receivers == null) {
                    receivers = new ArrayList<>(((ServerLevel) holder.level).getPlayers((serverPlayerEntity)-> true));
                    receivers.add((ServerPlayer) holder);
                }
                EventHandlerEntity.syncSlot((ServerPlayer) holder, i, stack, receivers);
                this.changed[i] = false;
                previous[i] = stack.copy();
            }
        }
    }

    public LivingEntity getHolder() {
        return this.holder;
    }
}
