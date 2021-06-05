package lazy.baubles.container.slots;

import lazy.baubles.container.PlayerExpandedContainer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ArmorSlot extends Slot {

    private EquipmentSlotType slotType;
    private PlayerEntity playerEntity;

    public ArmorSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, EquipmentSlotType slotType, PlayerEntity playerEntity) {
        super(inventoryIn, index, xPosition, yPosition);
        this.slotType = slotType;
        this.playerEntity = playerEntity;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.canEquip(this.slotType, this.playerEntity);
    }

    @Override
    public boolean mayPickup(PlayerEntity playerIn) {
        ItemStack itemstack = this.getItem();
        return (itemstack.isEmpty() || playerIn.isCreative() || !EnchantmentHelper.hasBindingCurse(itemstack)) && super.mayPickup(playerIn);
    }

    @Nullable
    @Override
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() { //getSlotTexture
        return Pair.of(PlayerContainer.BLOCK_ATLAS, PlayerExpandedContainer.ARMOR_SLOT_TEXTURES[slotType.getIndex()]);
    }
}
