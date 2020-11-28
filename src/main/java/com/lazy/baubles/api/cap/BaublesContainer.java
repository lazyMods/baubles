package com.lazy.baubles.api.cap;

import com.lazy.baubles.api.bauble.IBauble;
import com.lazy.baubles.event.EventHandlerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

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
        return bauble.canEquip(holder) && bauble.getBaubleType().hasSlot(slot);
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
        if (!(holder instanceof ServerPlayerEntity)) {
            return;
        }

        List<PlayerEntity> receivers = null;
        for (byte i = 0; i < getSlots(); i++) {
            ItemStack stack = getStackInSlot(i);
            boolean autosync = stack.getCapability(BaublesCapabilities.ITEM_BAUBLE).map(b -> b.willAutoSync(holder)).orElse(false);
            if (changed[i] || autosync && !ItemStack.areItemStacksEqual(stack, previous[i])) {
                if (receivers == null) {
                    receivers = new ArrayList<>(((ServerWorld) holder.world).getPlayers());
                    receivers.add((ServerPlayerEntity) holder);
                }
                EventHandlerEntity.syncSlot((ServerPlayerEntity) holder, i, stack, receivers);
                this.changed[i] = false;
                previous[i] = stack.copy();
            }
        }
    }

    public LivingEntity getHolder() {
        return this.holder;
    }
}
