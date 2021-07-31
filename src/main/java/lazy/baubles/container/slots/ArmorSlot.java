package lazy.baubles.container.slots;

import lazy.baubles.container.PlayerExpandedContainer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ArmorSlot extends Slot {

    private final EquipmentSlot slotType;
    private final Player playerEntity;

    public ArmorSlot(Container inventoryIn, int index, int xPosition, int yPosition, EquipmentSlot slotType, Player playerEntity) {
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
    public boolean mayPickup(@Nonnull Player playerIn) {
        var itemStack = this.getItem();
        return (itemStack.isEmpty() || playerIn.isCreative() || !EnchantmentHelper.hasBindingCurse(itemStack)) && super.mayPickup(playerIn);
    }

    @Nullable
    @Override
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(InventoryMenu.BLOCK_ATLAS, PlayerExpandedContainer.ARMOR_SLOT_TEXTURES[slotType.getIndex()]);
    }
}
