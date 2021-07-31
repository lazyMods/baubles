package lazy.baubles.datadriven.client;

import lazy.baubles.api.bauble.IBauble;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class BaublesItemOverridesList extends ItemOverrides {

    @Override
    public BakedModel resolve(@Nonnull BakedModel model, ItemStack stack, ClientLevel world, LivingEntity livingEntity, int i) {
        if (stack.getItem() instanceof IBauble bauble)
            return new BaublesItemFinalModel(model, bauble.getBaubleType(stack));
        else return model;
    }

}
