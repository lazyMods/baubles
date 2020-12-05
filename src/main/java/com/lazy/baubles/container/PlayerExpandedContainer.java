package com.lazy.baubles.container;

import com.lazy.baubles.Baubles;
import com.lazy.baubles.api.bauble.IBauble;
import com.lazy.baubles.api.cap.BaublesCapabilities;
import com.lazy.baubles.api.cap.IBaublesItemHandler;
import com.lazy.baubles.container.slots.ArmorSlot;
import com.lazy.baubles.container.slots.OffHandSlot;
import com.lazy.baubles.container.slots.SlotBauble;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PlayerExpandedContainer extends Container {

    public static final ResourceLocation[] ARMOR_SLOT_TEXTURES = new ResourceLocation[]{PlayerContainer.EMPTY_ARMOR_SLOT_BOOTS, PlayerContainer.EMPTY_ARMOR_SLOT_LEGGINGS, PlayerContainer.EMPTY_ARMOR_SLOT_CHESTPLATE, PlayerContainer.EMPTY_ARMOR_SLOT_HELMET};
    private static final EquipmentSlotType[] VALID_EQUIPMENT_SLOTS = new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};
    private final CraftingInventory craftMatrix = new CraftingInventory(this, 2, 2);
    private final CraftResultInventory craftResult = new CraftResultInventory();
    public final boolean isLocalWorld;
    private final PlayerEntity player;

    public IBaublesItemHandler baubles;

    public PlayerExpandedContainer(int id, PlayerInventory playerInventory, boolean localWorld) {
        super(Baubles.Registration.PLAYER_BAUBLES, id);
        this.isLocalWorld = localWorld;
        this.player = playerInventory.player;

        this.baubles = this.player.getCapability(BaublesCapabilities.BAUBLES).orElseThrow(NullPointerException::new);

        this.addSlot(new CraftingResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 154, 28));

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                this.addSlot(new Slot(this.craftMatrix, j + i * 2, 116 + j * 18, 18 + i * 18));
            }
        }

        for (int k = 0; k < 4; ++k) {
            final EquipmentSlotType equipmentslottype = VALID_EQUIPMENT_SLOTS[k];
            this.addSlot(new ArmorSlot(playerInventory, 36 + (3 - k), 8, 8 + k * 18, equipmentslottype, this.player));
        }

        this.addSlot(new SlotBauble(player, baubles, 0, 77, 8));
        this.addSlot(new SlotBauble(player, baubles, 1, 77, 8 + 1 * 18));
        this.addSlot(new SlotBauble(player, baubles, 2, 77, 8 + 2 * 18));
        this.addSlot(new SlotBauble(player, baubles, 3, 77, 8 + 3 * 18));
        this.addSlot(new SlotBauble(player, baubles, 4, 96, 8));
        this.addSlot(new SlotBauble(player, baubles, 5, 96, 8 + 1 * 18));
        this.addSlot(new SlotBauble(player, baubles, 6, 96, 8 + 2 * 18));

        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventory, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 142));
        }

        this.addSlot(new OffHandSlot(playerInventory, 40, 96, 62));
    }

    @Override
    public void onCraftMatrixChanged(IInventory par1IInventory) {
        try {
            Method onCraftChange = ObfuscationReflectionHelper.findMethod(WorkbenchContainer.class, "func_217066_a", int.class, World.class, PlayerEntity.class, CraftingInventory.class, CraftResultInventory.class);
            onCraftChange.invoke(null, this.windowId, this.player.world, this.player, this.craftMatrix, this.craftResult);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onContainerClosed(PlayerEntity player) {
        super.onContainerClosed(player);
        this.craftResult.clear();

        if (!player.world.isRemote) {
            this.clearContainer(player, player.world, this.craftMatrix);
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity par1PlayerEntity) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            EquipmentSlotType entityequipmentslot = MobEntity.getSlotForItemStack(itemstack);

            int slotShift = baubles.getSlots();

            if (index == 0) {
                if (!this.mergeItemStack(itemstack1, 9 + slotShift, 45 + slotShift, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index >= 1 && index < 5) {
                if (!this.mergeItemStack(itemstack1, 9 + slotShift, 45 + slotShift, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 5 && index < 9) {
                if (!this.mergeItemStack(itemstack1, 9 + slotShift, 45 + slotShift, false)) {
                    return ItemStack.EMPTY;
                }
            }

            // baubles -> inv
            else if (index >= 9 && index < 9 + slotShift) {
                if (!this.mergeItemStack(itemstack1, 9 + slotShift, 45 + slotShift, false)) {
                    return ItemStack.EMPTY;
                }
            }

            // inv -> armor
            else if (entityequipmentslot.getSlotType() == EquipmentSlotType.Group.ARMOR && !(this.inventorySlots.get(8 - entityequipmentslot.getIndex())).getHasStack()) {
                int i = 8 - entityequipmentslot.getIndex();

                if (!this.mergeItemStack(itemstack1, i, i + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            // inv -> offhand
            else if (entityequipmentslot == EquipmentSlotType.OFFHAND && !(this.inventorySlots.get(45 + slotShift)).getHasStack()) {
                if (!this.mergeItemStack(itemstack1, 45 + slotShift, 46 + slotShift, false)) {
                    return ItemStack.EMPTY;
                }
            }
            // inv -> bauble
            else if (itemstack.getCapability(BaublesCapabilities.ITEM_BAUBLE, null).isPresent()) {
                IBauble bauble = itemstack.getCapability(BaublesCapabilities.ITEM_BAUBLE, null).orElseThrow(NullPointerException::new);
                for (int baubleSlot : bauble.getBaubleType(itemstack).getValidSlots()) {
                    if (bauble.canEquip(this.player) && !(this.inventorySlots.get(baubleSlot + 9)).getHasStack() &&
                            !this.mergeItemStack(itemstack1, baubleSlot + 9, baubleSlot + 10, false)) {
                        return ItemStack.EMPTY;
                    }
                    if (itemstack1.getCount() == 0) break;
                }
            } else if (index >= 9 + slotShift && index < 36 + slotShift) {
                if (!this.mergeItemStack(itemstack1, 36 + slotShift, 45 + slotShift, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 36 + slotShift && index < 45 + slotShift) {
                if (!this.mergeItemStack(itemstack1, 9 + slotShift, 36 + slotShift, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 9 + slotShift, 45 + slotShift, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty() && !baubles.isEventBlocked() && slot instanceof SlotBauble && itemstack.getCapability(BaublesCapabilities.ITEM_BAUBLE, null).isPresent()) {
                ItemStack finalItemstack = itemstack;
                itemstack.getCapability(BaublesCapabilities.ITEM_BAUBLE, null).ifPresent((iBauble -> iBauble.onEquipped(playerIn, finalItemstack)));
            }

            ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);

            if (index == 0) {
                playerIn.dropItem(itemstack2, false);
            }
        }

        return itemstack;
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.craftResult && super.canMergeSlot(stack, slot);
    }

    private void addBaubleSlots() {

    }
}