package lazy.baubles.event;

import lazy.baubles.api.BaublesAPI;
import lazy.baubles.api.bauble.IBauble;
import lazy.baubles.api.cap.CapabilityBaubles;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = BaublesAPI.MOD_ID)
public class AttachCapability {

    private static final ResourceLocation CAP = new ResourceLocation(BaublesAPI.MOD_ID, "bauble_cap");

    @SubscribeEvent
    public static void attachCaps(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        if (stack.getItem() instanceof IBauble bauble) {
            event.addCapability(CAP, new ICapabilityProvider() {
                private final LazyOptional<IBauble> opt = LazyOptional.of(() -> bauble);

                @SuppressWarnings("ConstantConditions")
                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                    return CapabilityBaubles.ITEM_BAUBLE.orEmpty(cap, opt);
                }
            });
        }
    }
}
