package lazy.baubles.event;

import lazy.baubles.api.BaublesAPI;
import lazy.baubles.api.bauble.IBauble;
import lazy.baubles.api.cap.CapabilityBaubles;
import lazy.baubles.api.bauble.IBaublesItemHandler;
import lazy.baubles.capability.BaublesContainer;
import lazy.baubles.capability.BaublesContainerProvider;
import lazy.baubles.network.PacketHandler;
import lazy.baubles.network.SyncPacket;
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
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import java.util.Collection;
import java.util.Collections;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandlerEntity {

    @SubscribeEvent
    public static void cloneCapabilitiesEvent(PlayerEvent.Clone event) {
        try {
            event.getOriginal().getCapability(CapabilityBaubles.BAUBLES).ifPresent(bco -> {
                CompoundTag nbt = ((BaublesContainer) bco).serializeNBT();
                event.getOriginal().getCapability(CapabilityBaubles.BAUBLES).ifPresent(bcn -> {
                    ((BaublesContainer) bcn).deserializeNBT(nbt);
                });
            });
        } catch (Exception e) {
            System.out.println("Could not clone player [" + event.getOriginal().getName() + "] baubles when changing dimensions");
        }
    }

    @SubscribeEvent
    public static void attachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation(BaublesAPI.MOD_ID, "container"), new BaublesContainerProvider((Player) event.getObject()));
        }
    }

    @SubscribeEvent
    public static void playerJoin(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer) entity;
            syncSlots(player, Collections.singletonList(player));
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        if (target instanceof ServerPlayer) {
            syncSlots((ServerPlayer) target, Collections.singletonList(event.getPlayer()));
        }
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        // player events
        if (event.phase == TickEvent.Phase.END) {
            Player player = event.player;
            player.getCapability(CapabilityBaubles.BAUBLES).ifPresent(IBaublesItemHandler::tick);
        }
    }

    @SubscribeEvent
    public static void rightClickItem(PlayerInteractEvent.RightClickItem event) {
        ItemStack itemstack = event.getPlayer().getItemInHand(event.getHand());

        if (itemstack.getItem() instanceof IBauble) {
            IBauble bauble = (IBauble) itemstack.getItem();

            IBaublesItemHandler itemHandler = BaublesAPI.getBaublesHandler(event.getPlayer()).orElseThrow(NullPointerException::new);
            int emptySlot = BaublesAPI.getEmptySlotForBaubleType(event.getPlayer(), bauble.getBaubleType(itemstack));

            if (emptySlot != -1) {
                itemHandler.setStackInSlot(emptySlot, itemstack.copy());
                itemstack.setCount(0);
            }
        } else if (itemstack.getCapability(CapabilityBaubles.ITEM_BAUBLE).isPresent()) {
            IBauble bauble = itemstack.getCapability(CapabilityBaubles.ITEM_BAUBLE).orElseThrow(NullPointerException::new);

            IBaublesItemHandler itemHandler = BaublesAPI.getBaublesHandler(event.getPlayer()).orElseThrow(NullPointerException::new);
            int emptySlot = BaublesAPI.getEmptySlotForBaubleType(event.getPlayer(), bauble.getBaubleType(itemstack));

            if (emptySlot != -1) {
                itemHandler.setStackInSlot(emptySlot, itemstack.copy());
                itemstack.setCount(0);
            }
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
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) receiver), pkt);
        }
    }

    @SubscribeEvent
    public static void playerDeath(LivingDropsEvent event) {
        if (event.getEntity() instanceof Player && !event.getEntity().level.isClientSide && !event.getEntity().level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
            dropItemsAt((Player) event.getEntity(), event.getDrops());
        }
    }

    private static void dropItemsAt(Player player, Collection<ItemEntity> drops) {
        player.getCapability(CapabilityBaubles.BAUBLES).ifPresent(baubles -> {
            for (int i = 0; i < baubles.getSlots(); ++i) {
                if (!baubles.getStackInSlot(i).isEmpty()) {
                    ItemEntity ei = new ItemEntity(player.level, player.getX(), player.getY() + player.getEyeHeight(), player.getZ(), baubles.getStackInSlot(i).copy());
                    ei.setPickUpDelay(40);
                    drops.add(ei);
                    baubles.setStackInSlot(i, ItemStack.EMPTY);
                }
            }
        });
    }
}