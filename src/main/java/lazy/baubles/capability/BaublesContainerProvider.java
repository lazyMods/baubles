package lazy.baubles.capability;

import lazy.baubles.api.cap.CapabilityBaubles;
import lazy.baubles.api.bauble.IBaublesItemHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

@SuppressWarnings("ConstantConditions")
public class BaublesContainerProvider implements INBTSerializable<CompoundTag>, ICapabilityProvider {

    private final BaublesContainer inner;
    private final LazyOptional<IBaublesItemHandler> baublesHandlerCap;

    public BaublesContainerProvider(Player player) {
        this.inner = new BaublesContainer(player);
        this.baublesHandlerCap = LazyOptional.of(() -> this.inner);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, Direction facing) {
        return CapabilityBaubles.BAUBLES.orEmpty(capability, this.baublesHandlerCap);
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