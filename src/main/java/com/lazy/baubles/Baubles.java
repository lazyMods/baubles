package com.lazy.baubles;

import com.lazy.baubles.data.BaubleJsonItem;
import com.lazy.baubles.data.json.BaubleJson;
import com.lazy.baubles.container.PlayerExpandedContainer;
import com.lazy.baubles.proxy.ClientProxy;
import com.lazy.baubles.proxy.CommonProxy;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(Baubles.MODID)
public class Baubles {

    public static final String MODID = "baubles";

    public static final Logger LOGGER = LogManager.getLogger();
    public static CommonProxy proxy;

    public Baubles() {
        proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
        proxy.init();
    }

    @Mod.EventBusSubscriber(modid = Baubles.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {

        public static List<ContainerType<?>> CONTAINERS = new ArrayList<>();

        @ObjectHolder("baubles:player_baubles")
        public static ContainerType<PlayerExpandedContainer> PLAYER_BAUBLES = createContainer("player_baubles", (id, inv, data) -> new PlayerExpandedContainer(id, inv, !inv.player.world.isRemote));

        private static <T extends Container> ContainerType<T> createContainer(String name, IContainerFactory<T> factory) {
            ContainerType<T> containerType = IForgeContainerType.create(factory);
            containerType.setRegistryName(new ResourceLocation(Baubles.MODID, name));
            CONTAINERS.add(containerType);
            return containerType;
        }

        @SubscribeEvent
        public static void onContainerRegister(final RegistryEvent.Register<ContainerType<?>> event) {
            event.getRegistry().registerAll(CONTAINERS.toArray(new ContainerType[0]));
        }

        @SubscribeEvent
        public static void onItemRegister(RegistryEvent.Register<Item> e){
            e.getRegistry().register(new BaubleJsonItem(BaubleJson.loadBaubles()));
        }
    }
}
