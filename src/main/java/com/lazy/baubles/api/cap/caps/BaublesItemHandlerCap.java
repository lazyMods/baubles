package com.lazy.baubles.api.cap.caps;

import com.lazy.baubles.api.cap.IBaublesItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Callable;

public class BaublesItemHandlerCap {

    public static class IBaublesItemHandlerStorage implements Capability.IStorage<IBaublesItemHandler> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<IBaublesItemHandler> capability, IBaublesItemHandler instance, Direction side) {
            return null;
        }

        @Override
        public void readNBT(Capability<IBaublesItemHandler> capability, IBaublesItemHandler instance, Direction side, INBT nbt) {

        }
    }

    public static class IBaublesItemHandlerFactory implements Callable<IBaublesItemHandler> {

        @Override
        public IBaublesItemHandler call() {
            return new IBaublesItemHandler() {
                @Override
                public boolean isItemValidForSlot(int slot, ItemStack stack) {
                    return false;
                }

                @Override
                public boolean isEventBlocked() {
                    return false;
                }

                @Override
                public void setEventBlock(boolean blockEvents) {

                }

                @Override
                public void setStackInSlot(int slot, @Nonnull ItemStack stack) {

                }

                @Override
                public int getSlots() {
                    return 0;
                }

                @Nonnull
                @Override
                public ItemStack getStackInSlot(int slot) {
                    return null;
                }

                @Nonnull
                @Override
                public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                    return null;
                }

                @Nonnull
                @Override
                public ItemStack extractItem(int slot, int amount, boolean simulate) {
                    return null;
                }

                @Override
                public int getSlotLimit(int slot) {
                    return 0;
                }

                @Override
                public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                    return false;
                }

                @Override
                public void tick() {

                }
            };
        }
    }
}
