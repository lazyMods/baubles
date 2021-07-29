package lazy.baubles.api.bauble;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IBaublesItemHandler extends IItemHandlerModifiable {

    boolean isItemValidForSlot(int slot, ItemStack stack);

    boolean isEventBlocked();

    void setEventBlock(boolean blockEvents);

    void tick();
}