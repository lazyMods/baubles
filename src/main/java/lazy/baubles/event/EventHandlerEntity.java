package lazy.baubles.event;

import lazy.baubles.api.BaublesAPI;
import lazy.baubles.api.bauble.IBauble;
import lazy.baubles.api.bauble.IBaublesItemHandler;
import lazy.baubles.api.cap.CapabilityBaubles;
import lazy.baubles.capability.BaublesContainer;
import lazy.baubles.capability.BaublesContainerProvider;
import lazy.baubles.network.PacketHandler;
import lazy.baubles.network.msg.SyncPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.Collection;
import java.util.Collections;

@SuppressWarnings("ConstantConditions")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandlerEntity {

    @SubscribeEvent
    public static void cloneCapabilitiesEvent(PlayerEvent.Clone event) {
        try {
            event.getOriginal().getCapability(CapabilityBaubles.BAUBLES).ifPresent(bco -> {
                var nbt = ((BaublesContainer) bco).serializeNBT();
                event.getOriginal().getCapability(CapabilityBaubles.BAUBLES).ifPresent(bcn -> ((BaublesContainer) bcn).deserializeNBT(nbt));
            });
        } catch (Exception e) {
            System.out.println("Could not clone player [" + event.getOriginal().getName() + "] baubles when changing dimensions");
        }
    }

    @SubscribeEvent
    public static void attachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            event.addCapability(new ResourceLocation(BaublesAPI.MOD_ID, "container"), new BaublesContainerProvider(player));
        }
    }

    @SubscribeEvent
    public static void playerJoin(EntityJoinWorldEvent event) {
        var entity = event.getEntity();
        if (entity instanceof ServerPlayer serverPlayer) {
            syncSlots(serverPlayer, Collections.singletonList(serverPlayer));
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        var target = event.getTarget();
        if (target instanceof ServerPlayer serverPlayer) {
            syncSlots(serverPlayer, Collections.singletonList(event.getPlayer()));
        }
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            var player = event.player;
            player.getCapability(CapabilityBaubles.BAUBLES).ifPresent(IBaublesItemHandler::tick);
        }
    }

    @SubscribeEvent
    public static void rightClickItem(PlayerInteractEvent.RightClickItem event) {
        var itemStack = event.getPlayer().getItemInHand(event.getHand());

        if (itemStack.getItem() instanceof IBauble bauble) {
            var itemHandler = BaublesAPI.getBaublesHandler(event.getPlayer()).orElseThrow(NullPointerException::new);
            int emptySlot = BaublesAPI.getEmptySlotForBaubleType(event.getPlayer(), bauble.getBaubleType(itemStack));

            if (emptySlot != -1) {
                itemHandler.setStackInSlot(emptySlot, itemStack.copy());
                itemStack.setCount(0);
            }
        } else if (itemStack.getCapability(CapabilityBaubles.ITEM_BAUBLE).isPresent()) {
            var bauble = itemStack.getCapability(CapabilityBaubles.ITEM_BAUBLE).orElseThrow(NullPointerException::new);
            var itemHandler = BaublesAPI.getBaublesHandler(event.getPlayer()).orElseThrow(NullPointerException::new);
            int emptySlot = BaublesAPI.getEmptySlotForBaubleType(event.getPlayer(), bauble.getBaubleType(itemStack));

            if (emptySlot != -1) {
                itemHandler.setStackInSlot(emptySlot, itemStack.copy());
                itemStack.setCount(0);
            }
        }
    }

    @SubscribeEvent
    public static void playerDeath(LivingDropsEvent event) {
        var level = event.getEntity().level;
        if (event.getEntity() instanceof Player p && !level.isClientSide && !level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
            dropItemsAt(p, event.getDrops());
        }
    }

    private static void syncSlots(Player player, Collection<? extends Player> receivers) {
        player.getCapability(CapabilityBaubles.BAUBLES).ifPresent(baubles -> {
            for (byte i = 0; i < baubles.getSlots(); i++) {
                syncSlot(player, i, baubles.getStackInSlot(i), receivers);
            }
        });
    }

    public static void syncSlot(Player player, byte slot, ItemStack stack, Collection<? extends Player> receivers) {
        SyncPacket pkt = new SyncPacket(player.getId(), slot, stack);
        for (Player receiver : receivers) {
            if (receiver instanceof ServerPlayer s)
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> s), pkt);
        }
    }

    private static void dropItemsAt(Player player, Collection<ItemEntity> drops) {
        player.getCapability(CapabilityBaubles.BAUBLES).ifPresent(baubles -> {
            for (int i = 0; i < baubles.getSlots(); ++i) {
                if (!baubles.getStackInSlot(i).isEmpty()) {
                    var pos = player.position();
                    var bauble = new ItemEntity(player.level, pos.x, pos.y + player.getEyeHeight(), pos.z, baubles.getStackInSlot(i).copy());
                    bauble.setPickUpDelay(40);
                    drops.add(bauble);
                    baubles.setStackInSlot(i, ItemStack.EMPTY);
                }
            }
        });
    }
}