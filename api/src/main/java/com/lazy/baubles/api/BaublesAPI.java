package com.lazy.baubles.api;

import com.lazy.baubles.api.bauble.BaubleType;
import com.lazy.baubles.api.cap.BaublesCapabilities;
import com.lazy.baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.LazyOptional;

/**
 * @author Azanor
 * @author lazynessmind - porter
 */
public class BaublesAPI {

    //Retrieves the baubles inventory capability handler for the supplied player
    public static LazyOptional<IBaublesItemHandler> getBaublesHandler(PlayerEntity player) {
        return player.getCapability(BaublesCapabilities.BAUBLES);
    }

    //Returns if the passed in item is equipped in a bauble slot. Will return the first slot found
    //@return -1 if not found and slot number if it is found
    public static int isBaubleEquipped(PlayerEntity player, Item bauble) {
        return getBaublesHandler(player).map(handler -> {
            for (int a = 0; a < handler.getSlots(); a++) {
                if (!handler.getStackInSlot(a).isEmpty() && handler.getStackInSlot(a).getItem() == bauble)
                    return a;
            }
            return -1;
        }).orElse(-1);
    }

    public static int getEmptySlotForBaubleType(PlayerEntity playerEntity, BaubleType type){
        IBaublesItemHandler itemHandler = getBaublesHandler(playerEntity).orElseThrow(NullPointerException::new);

        for(int i : type.getValidSlots()){
            if(itemHandler.getStackInSlot(i).isEmpty()){
                return i;
            }
        }

        return -1;
    }
}