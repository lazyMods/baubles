package lazy.baubles;

import lazy.baubles.api.BaublesAPI;
import lazy.baubles.container.PlayerExpandedContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModMenus {

    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS, BaublesAPI.MOD_ID);

    public static final RegistryObject<MenuType<PlayerExpandedContainer>> PLAYER_BAUBLES = MENUS.register("player_baubles",
            () -> IForgeContainerType.create((windowId, inv, data) -> new PlayerExpandedContainer(windowId, inv, !inv.player.level.isClientSide)));

    public static void init() {
        MENUS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
