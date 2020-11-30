package com.lazy.baubles.api.cap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IBaublesItemHandler extends IItemHandlerModifiable {

    boolean isItemValidForSlot(int slot, ItemStack stack);

    boolean isEventBlocked();

    void setEventBlock(boolean blockEvents);

    void tick();
}