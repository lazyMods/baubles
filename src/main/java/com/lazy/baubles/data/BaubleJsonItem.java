package com.lazy.baubles.data;

import com.lazy.baubles.api.bauble.BaubleType;
import com.lazy.baubles.api.bauble.IBauble;
import com.lazy.baubles.data.json.BaubleModel;
import com.lazy.baubles.data.json.EffectModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class BaubleJsonItem extends Item implements IBauble {

    public List<BaubleModel> model;

    public BaubleJsonItem(List<BaubleModel> model) {
        super(new Item.Properties().group(ItemGroup.BREWING));
        this.setRegistryName("ring");
        this.model = model;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        super.fillItemGroup(group, items);
        model.forEach(model -> {
            ItemStack stack = new ItemStack(this);
            stack.setTag(new CompoundNBT());
            if(stack.getTag() != null) stack.getTag().putString("jsonName", model.getRegistryName());
            items.add(stack);
        });
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if(stack.getTag() != null){
            model.forEach(model->{
                if(this.isRing(stack, model.getRegistryName())){
                    model.getTooltips().forEach(s -> tooltip.add(new StringTextComponent(s)));
                }
            });
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        AtomicBoolean has = new AtomicBoolean(false);
        if(stack.getTag() != null){
            model.forEach(model -> {
                if(this.isRing(stack, model.getRegistryName())){
                    has.set(model.isGlint());
                }
            });
        }
        return has.get();
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        StringTextComponent textComponent = new StringTextComponent("");
        if(stack.getTag() != null){
            model.forEach(model->{
                if(this.isRing(stack, model.getRegistryName())){
                    textComponent.func_240702_b_(model.getDisplayName());
                }
            });
        }
        return textComponent.getString().equals("") ? super.getDisplayName(stack) : textComponent;
    }

    @Override
    public BaubleType getBaubleType() {
        return BaubleType.RING;
    }

    @Override
    public void onEquipped(LivingEntity player, ItemStack stack) {
        if(stack.getTag() != null){
            model.forEach(model->{
                if(this.isRing(stack, model.getRegistryName())){
                    for (EffectModel effectModel : model.getPotionIds()) {
                        if(ForgeRegistries.POTIONS.containsKey(new ResourceLocation(effectModel.getEffectRegistryName()))){
                            Effect effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(effectModel.getEffectRegistryName()));
                            if(effect != null) player.addPotionEffect(new EffectInstance(effect, 999999, effectModel.getEffectLevel()));
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onUnequipped(LivingEntity player, ItemStack stack) {
        if(stack.getTag() != null){
            model.forEach(model->{
                if(this.isRing(stack, model.getRegistryName())){
                    for(EffectModel effectModel : model.getPotionIds()){
                        System.out.println(effectModel.getEffectLevel());
                        Effect effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(effectModel.getEffectRegistryName()));
                        if(effect != null) player.removeActivePotionEffect(effect);
                    }
                }
            });
        }
    }

    private boolean isRing(ItemStack stack, String registryName){
        return stack.getTag() != null && stack.getTag().contains("jsonName") && stack.getTag().getString("jsonName").equals(registryName);
    }

    public void setModel(List<BaubleModel> loadBaubles) {
        this.model = loadBaubles;
    }
}
