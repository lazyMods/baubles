package lazy.baubles;

import lazy.baubles.api.BaublesAPI;
import lazy.baubles.container.PlayerExpandedContainer;
import lazy.baubles.proxy.ClientProxy;
import lazy.baubles.proxy.CommonProxy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.network.IContainerFactory;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;

@Mod(BaublesAPI.MOD_ID)
public class Baubles {

    public static CommonProxy proxy;

    public Baubles() {
        proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
        proxy.init();
    }

    @Mod.EventBusSubscriber(modid = BaublesAPI.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {

        public static List<MenuType<?>> CONTAINERS = new ArrayList<>();

        @ObjectHolder("baubles:player_baubles")
        public static MenuType<PlayerExpandedContainer> PLAYER_BAUBLES = createContainer("player_baubles", (id, inv, data) -> new PlayerExpandedContainer(id, inv, !inv.player.level.isClientSide));

        private static <T extends AbstractContainerMenu> MenuType<T> createContainer(String name, IContainerFactory<T> factory) {
            MenuType<T> containerType = IForgeContainerType.create(factory);
            containerType.setRegistryName(new ResourceLocation(BaublesAPI.MOD_ID, name));
            CONTAINERS.add(containerType);
            return containerType;
        }

        @SubscribeEvent
        public static void onContainerRegister(final RegistryEvent.Register<MenuType<?>> event) {
            event.getRegistry().registerAll(CONTAINERS.toArray(new MenuType[0]));
        }
    }
}
