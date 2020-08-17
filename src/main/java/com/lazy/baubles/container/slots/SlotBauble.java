package com.lazy.baubles.container.slots;

import com.lazy.baubles.api.IBauble;
import com.lazy.baubles.api.cap.BaublesCapabilities;
import com.lazy.baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBauble extends SlotItemHandler {
    int baubleSlot;
    PlayerEntity player;

    public SlotBauble(PlayerEntity player, IBaublesItemHandler itemHandler, int slot, int par4, int par5) {
        super(itemHandler, slot, par4, par5);
        this.baubleSlot = slot;
        this.player = player;
    }

    /**
     * Check if the stack is a valid item for this slot.
     */
    @Override
    public boolean isItemValid(ItemStack stack) {
        return ((IBaublesItemHandler) getItemHandler()).isItemValidForSlot(baubleSlot, stack);
    }

    @Override
    public boolean canTakeStack(PlayerEntity player) {
        ItemStack stack = getStack();
        if (stack.isEmpty())
            return false;

        IBauble bauble = stack.getCapability(BaublesCapabilities.ITEM_BAUBLE).orElseThrow(NullPointerException::new);
        return bauble.canUnequip(player);
    }

    @Override
    public ItemStack onTake(PlayerEntity playerIn, ItemStack stack) {
        if (!getHasStack() && !((IBaublesItemHandler) getItemHandler()).isEventBlocked() && stack.getCapability(BaublesCapabilities.ITEM_BAUBLE).isPresent()) {
            stack.getCapability(BaublesCapabilities.ITEM_BAUBLE, null).ifPresent((iBauble) -> iBauble.onUnequipped(playerIn));
        }
        super.onTake(playerIn, stack);
        return stack;
    }

    @Override
    public void putStack(ItemStack stack) {
        if (getHasStack() && !ItemStack.areItemStacksEqual(stack, getStack()) && !((IBaublesItemHandler) getItemHandler()).isEventBlocked() && getStack().getCapability(BaublesCapabilities.ITEM_BAUBLE, null).isPresent()) {
            getStack().getCapability(BaublesCapabilities.ITEM_BAUBLE, null).ifPresent((iBauble) -> iBauble.onUnequipped(player));
        }

        ItemStack oldstack = getStack().copy();
        super.putStack(stack);

        if (getHasStack() && !ItemStack.areItemStacksEqual(oldstack, getStack()) && !((IBaublesItemHandler) getItemHandler()).isEventBlocked() && getStack().getCapability(BaublesCapabilities.ITEM_BAUBLE, null).isPresent()) {
            getStack().getCapability(BaublesCapabilities.ITEM_BAUBLE, null).ifPresent((iBauble) -> iBauble.onEquipped(player));
        }
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }
}