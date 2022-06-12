package lazy.baubles.client.util;

import lazy.baubles.container.PlayerExpandedContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GuiProvider implements MenuProvider {

    @Override
    @Nonnull
    public Component getDisplayName() {
        return Component.literal("PlayerBaublesInv");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @Nonnull Inventory playerInventory, Player playerEntity) {
        return new PlayerExpandedContainer(id, playerInventory, !playerEntity.level.isClientSide);
    }
}
