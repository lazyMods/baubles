package lazy.baubles.capability;

import lazy.baubles.api.cap.BaublesCapabilities;
import lazy.baubles.api.cap.IBaublesItemHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class BaublesContainerProvider implements INBTSerializable<CompoundTag>, ICapabilityProvider {

    private BaublesContainer inner;
    private LazyOptional<IBaublesItemHandler> opt;

    public BaublesContainerProvider(Player player) {
        this.inner = new BaublesContainer(player);
        this.opt = LazyOptional.of(() -> inner);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, Direction facing) {
        return BaublesCapabilities.BAUBLES.orEmpty(capability, opt);
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.inner.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.inner.deserializeNBT(nbt);
    }
}