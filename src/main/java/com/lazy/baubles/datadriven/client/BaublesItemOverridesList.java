package com.lazy.baubles.datadriven.client;

import com.lazy.baubles.api.bauble.IBauble;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class BaublesItemOverridesList extends ItemOverrideList {

    @Override
    public IBakedModel getOverrideModel(IBakedModel model, ItemStack stack, ClientWorld world, LivingEntity livingEntity) {
        return new BaublesItemFinalModel(model, ((IBauble) stack.getItem()).getBaubleType(stack));
    }
}
