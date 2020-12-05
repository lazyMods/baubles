package com.lazy.baubles.event;

import com.lazy.baubles.Baubles;
import com.lazy.baubles.api.BaublesAPI;
import com.lazy.baubles.api.bauble.IBauble;
import com.lazy.baubles.api.cap.BaublesCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
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

    private static ResourceLocation cap = new ResourceLocation(BaublesAPI.MOD_ID, "bauble_cap");

    @SubscribeEvent
    public static void attachCaps(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        if (stack.getItem() instanceof IBauble) {
            event.addCapability(cap, new ICapabilityProvider() {
                private final LazyOptional<IBauble> opt = LazyOptional.of(() -> (IBauble) stack.getItem());

                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                    return BaublesCapabilities.ITEM_BAUBLE.orEmpty(cap, opt);
                }
            });
        }
    }
}
