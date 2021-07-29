package lazy.baubles.container.slots;

import lazy.baubles.api.bauble.IBauble;
import lazy.baubles.api.cap.BaublesCapabilities;
import lazy.baubles.api.cap.IBaublesItemHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBauble extends SlotItemHandler {
    int baubleSlot;
    Player player;

    public SlotBauble(Player player, IBaublesItemHandler itemHandler, int slot, int par4, int par5) {
        super(itemHandler, slot, par4, par5);
        this.baubleSlot = slot;
        this.player = player;
    }

    /**
     * Check if the stack is a valid item for this slot.
     */
    @Override
    public boolean mayPlace(ItemStack stack) {
        return ((IBaublesItemHandler) getItemHandler()).isItemValidForSlot(baubleSlot, stack);
    }

    @Override
    public boolean mayPickup(Player player) {
        ItemStack stack = getItem();
        if (stack.isEmpty())
            return false;

        IBauble bauble = stack.getCapability(BaublesCapabilities.ITEM_BAUBLE).orElseThrow(NullPointerException::new);
        return bauble.canUnequip(player);
    }

    @Override
    public void onTake(Player playerIn, ItemStack stack) {
        if (!hasItem() && !((IBaublesItemHandler) getItemHandler()).isEventBlocked() && stack.getCapability(BaublesCapabilities.ITEM_BAUBLE).isPresent()) {
            stack.getCapability(BaublesCapabilities.ITEM_BAUBLE, null).ifPresent((iBauble) -> iBauble.onUnequipped(playerIn, stack));
        }
        super.onTake(playerIn, stack);
    }

    @Override
    public void set(ItemStack stack) {
        if (hasItem() && !ItemStack.isSame(stack, getItem()) && !((IBaublesItemHandler) getItemHandler()).isEventBlocked() && getItem().getCapability(BaublesCapabilities.ITEM_BAUBLE, null).isPresent()) {
            getItem().getCapability(BaublesCapabilities.ITEM_BAUBLE, null).ifPresent((iBauble) -> iBauble.onUnequipped(player, stack));
        }

        ItemStack oldstack = getItem().copy();
        super.set(stack);

        if (hasItem() && !ItemStack.isSame(oldstack, getItem()) && !((IBaublesItemHandler) getItemHandler()).isEventBlocked() && getItem().getCapability(BaublesCapabilities.ITEM_BAUBLE, null).isPresent()) {
            getItem().getCapability(BaublesCapabilities.ITEM_BAUBLE, null).ifPresent((iBauble) -> iBauble.onEquipped(player, stack));
        }
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}