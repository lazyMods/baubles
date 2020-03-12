package com.lazy.baubles.api.cap;

import com.lazy.baubles.api.IBauble;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class BaublesCapabilities {

    @CapabilityInject(IBaublesItemHandler.class)
    public static final Capability<IBaublesItemHandler> BAUBLES = null;

    @CapabilityInject(IBauble.class)
    public static final Capability<IBauble> ITEM_BAUBLE = null;
}