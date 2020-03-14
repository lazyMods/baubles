package com.lazy.baubles.container.slots;

import com.lazy.baubles.container.PlayerExpandedContainer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

public class ArmorSlot extends Slot {

    private EquipmentSlotType slotType;
    private PlayerEntity playerEntity;

    public ArmorSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, EquipmentSlotType slotType, PlayerEntity playerEntity) {
        super(inventoryIn, index, xPosition, yPosition);
        this.slotType = slotType;
        this.playerEntity = playerEntity;
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.canEquip(this.slotType, this.playerEntity);
    }

    @Override
    public boolean canTakeStack(PlayerEntity playerIn) {
        ItemStack itemstack = this.getStack();
        return (itemstack.isEmpty() || playerIn.isCreative() || !EnchantmentHelper.hasBindingCurse(itemstack)) && super.canTakeStack(playerIn);
    }

    @OnlyIn(Dist.CLIENT)
    public Pair<ResourceLocation, ResourceLocation> func_225517_c_() {
        return Pair.of(PlayerContainer.LOCATION_BLOCKS_TEXTURE, PlayerExpandedContainer.ARMOR_SLOT_TEXTURES[slotType.getIndex()]);
    }

}
