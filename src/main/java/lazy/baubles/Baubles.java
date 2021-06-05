package lazy.baubles;

import lazy.baubles.api.BaublesAPI;
import lazy.baubles.container.PlayerExpandedContainer;
import lazy.baubles.proxy.ClientProxy;
import lazy.baubles.proxy.CommonProxy;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.IContainerFactory;
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

        public static List<ContainerType<?>> CONTAINERS = new ArrayList<>();

        @ObjectHolder("baubles:player_baubles")
        public static ContainerType<PlayerExpandedContainer> PLAYER_BAUBLES = createContainer("player_baubles", (id, inv, data) -> new PlayerExpandedContainer(id, inv, !inv.player.world.isRemote));

        private static <T extends Container> ContainerType<T> createContainer(String name, IContainerFactory<T> factory) {
            ContainerType<T> containerType = IForgeContainerType.create(factory);
            containerType.setRegistryName(new ResourceLocation(BaublesAPI.MOD_ID, name));
            CONTAINERS.add(containerType);
            return containerType;
        }

        @SubscribeEvent
        public static void onContainerRegister(final RegistryEvent.Register<ContainerType<?>> event) {
            event.getRegistry().registerAll(CONTAINERS.toArray(new ContainerType[0]));
        }
    }
}
