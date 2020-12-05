package com.lazy.baubles.capability;

import com.lazy.baubles.api.cap.BaublesCapabilities;
import com.lazy.baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class BaublesContainerProvider implements INBTSerializable<CompoundNBT>, ICapabilityProvider {

    private BaublesContainer inner;
    private LazyOptional<IBaublesItemHandler> opt;

    public BaublesContainerProvider(PlayerEntity player) {
        this.inner = new BaublesContainer(player);
        this.opt = LazyOptional.of(() -> inner);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, Direction facing) {
        return BaublesCapabilities.BAUBLES.orEmpty(capability, opt);
    }

    @Override
    public CompoundNBT serializeNBT() {
        return this.inner.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.inner.deserializeNBT(nbt);
    }
}