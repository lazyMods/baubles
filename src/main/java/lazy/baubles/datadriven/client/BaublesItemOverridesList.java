package lazy.baubles.datadriven.client;

import lazy.baubles.api.bauble.IBauble;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class BaublesItemOverridesList extends ItemOverrideList {

    @Override
    public IBakedModel resolve(IBakedModel model, ItemStack stack, ClientWorld world, LivingEntity livingEntity) {
        if (stack.getItem() instanceof IBauble)
            return new BaublesItemFinalModel(model, ((IBauble) stack.getItem()).getBaubleType(stack));
        else return model;
    }
}
