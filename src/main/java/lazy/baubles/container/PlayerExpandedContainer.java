package lazy.baubles.container;

import lazy.baubles.Baubles;
import lazy.baubles.api.bauble.IBauble;
import lazy.baubles.api.cap.CapabilityBaubles;
import lazy.baubles.api.bauble.IBaublesItemHandler;
import lazy.baubles.container.slots.ArmorSlot;
import lazy.baubles.container.slots.OffHandSlot;
import lazy.baubles.container.slots.SlotBauble;
import lazy.baubles.setup.ModMenus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings("ConstantConditions")
public class PlayerExpandedContainer extends AbstractContainerMenu {

    public static final ResourceLocation[] ARMOR_SLOT_TEXTURES = new ResourceLocation[]{InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS, InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE, InventoryMenu.EMPTY_ARMOR_SLOT_HELMET};
    private static final EquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    private final CraftingContainer craftMatrix = new CraftingContainer(this, 2, 2);
    private final ResultContainer craftResult = new ResultContainer();
    public final boolean isLocalWorld;
    private final Player player;

    public IBaublesItemHandler baubles;

    public PlayerExpandedContainer(int id, Inventory playerInventory, boolean localWorld) {
        super(ModMenus.PLAYER_BAUBLES.get(), id);
        this.isLocalWorld = localWorld;
        this.player = playerInventory.player;

        this.baubles = this.player.getCapability(CapabilityBaubles.BAUBLES).orElseThrow(NullPointerException::new);

        this.addSlot(new ResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 154, 28));

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                this.addSlot(new Slot(this.craftMatrix, j + i * 2, 116 + j * 18, 18 + i * 18));
            }
        }

        for (int k = 0; k < 4; ++k) {
            var equipmentSlotType = VALID_EQUIPMENT_SLOTS[k];
            this.addSlot(new ArmorSlot(playerInventory, 36 + (3 - k), 8, 8 + k * 18, equipmentSlotType, this.player));
        }

        this.addBaubleSlots();

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
    public void slotsChanged(@Nonnull Container container) {
        try {
            Method onCraftChange = ObfuscationReflectionHelper.findMethod(CraftingMenu.class, "func_217066_a", int.class, Level.class, Player.class, CraftingContainer.class, ResultContainer.class);
            onCraftChange.invoke(null, this.containerId, this.player.level, this.player, this.craftMatrix, this.craftResult);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void removed(@Nonnull Player player) {
        super.removed(player);
        this.craftResult.clearContent();

        if (!player.level.isClientSide) {
            this.clearContainer(player, this.craftMatrix);
        }
    }


    @Override
    public boolean stillValid(@Nonnull Player par1PlayerEntity) {
        return true;
    }

    @Override
    @Nonnull
    public ItemStack quickMoveStack(@Nonnull Player playerIn, int index) {
        var itemStack = ItemStack.EMPTY;
        var slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            var stackInSlot = slot.getItem();
            itemStack = stackInSlot.copy();

            var entityEquipmentSlot = Mob.getEquipmentSlotForItem(itemStack);

            int slotShift = baubles.getSlots();

            if (index == 0) {
                if (!this.moveItemStackTo(stackInSlot, 9 + slotShift, 45 + slotShift, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(stackInSlot, itemStack);
            } else if (index >= 1 && index < 5) {
                if (!this.moveItemStackTo(stackInSlot, 9 + slotShift, 45 + slotShift, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 5 && index < 9) {
                if (!this.moveItemStackTo(stackInSlot, 9 + slotShift, 45 + slotShift, false)) {
                    return ItemStack.EMPTY;
                }
            }

            // baubles -> inv
            else if (index >= 9 && index < 9 + slotShift) {
                if (!this.moveItemStackTo(stackInSlot, 9 + slotShift, 45 + slotShift, false)) {
                    return ItemStack.EMPTY;
                }
            }

            // inv -> armor
            else if (entityEquipmentSlot.getType() == EquipmentSlot.Type.ARMOR && !(this.slots.get(8 - entityEquipmentSlot.getIndex())).hasItem()) {
                int i = 8 - entityEquipmentSlot.getIndex();

                if (!this.moveItemStackTo(stackInSlot, i, i + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            // inv -> offhand
            else if (entityEquipmentSlot == EquipmentSlot.OFFHAND && !(this.slots.get(45 + slotShift)).hasItem()) {
                if (!this.moveItemStackTo(stackInSlot, 45 + slotShift, 46 + slotShift, false)) {
                    return ItemStack.EMPTY;
                }
            }
            // inv -> bauble
            else if (itemStack.getCapability(CapabilityBaubles.ITEM_BAUBLE, null).isPresent()) {
                var bauble = itemStack.getCapability(CapabilityBaubles.ITEM_BAUBLE, null).orElseThrow(NullPointerException::new);
                for (int baubleSlot : bauble.getBaubleType(itemStack).getValidSlots()) {
                    if (bauble.canEquip(this.player) && !(this.slots.get(baubleSlot + 9)).hasItem() &&
                            !this.moveItemStackTo(stackInSlot, baubleSlot + 9, baubleSlot + 10, false)) {
                        return ItemStack.EMPTY;
                    }
                    if (stackInSlot.getCount() == 0) break;
                }
            } else if (index >= 9 + slotShift && index < 36 + slotShift) {
                if (!this.moveItemStackTo(stackInSlot, 36 + slotShift, 45 + slotShift, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 36 + slotShift && index < 45 + slotShift) {
                if (!this.moveItemStackTo(stackInSlot, 9 + slotShift, 36 + slotShift, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stackInSlot, 9 + slotShift, 45 + slotShift, false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackInSlot.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty() && !baubles.isEventBlocked() && slot instanceof SlotBauble && itemStack.getCapability(CapabilityBaubles.ITEM_BAUBLE, null).isPresent()) {
                var finalItemStack = itemStack;
                itemStack.getCapability(CapabilityBaubles.ITEM_BAUBLE, null).ifPresent((iBauble -> iBauble.onEquipped(playerIn, finalItemStack)));
            }

            //TODO
            /*ItemStack itemstack2 = slot.onTake(playerIn, stackInSlot);

            if (index == 0) {
                playerIn.drop(itemstack2, false);
            }*/
        }

        return itemStack;
    }


    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.craftResult && super.canTakeItemForPickAll(stack, slot);
    }

    private void addBaubleSlots() {
        this.addSlot(new SlotBauble(player, baubles, 0, 77, 8));
        this.addSlot(new SlotBauble(player, baubles, 1, 77, 8 + 18));
        this.addSlot(new SlotBauble(player, baubles, 2, 77, 8 + 2 * 18));
        this.addSlot(new SlotBauble(player, baubles, 3, 77, 8 + 3 * 18));
        this.addSlot(new SlotBauble(player, baubles, 4, 96, 8));
        this.addSlot(new SlotBauble(player, baubles, 5, 96, 8 + 18));
        this.addSlot(new SlotBauble(player, baubles, 6, 96, 8 + 2 * 18));
    }
}