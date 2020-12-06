package com.lazy.baubles.event;

import com.lazy.baubles.api.BaublesAPI;
import com.lazy.baubles.api.bauble.IBauble;
import com.lazy.baubles.api.cap.BaublesCapabilities;
import com.lazy.baubles.api.cap.IBaublesItemHandler;
import com.lazy.baubles.capability.BaublesContainer;
import com.lazy.baubles.capability.BaublesContainerProvider;
import com.lazy.baubles.network.PacketHandler;
import com.lazy.baubles.network.SyncPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameRules;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Collection;
import java.util.Collections;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandlerEntity {

    @SubscribeEvent
    public static void cloneCapabilitiesEvent(PlayerEvent.Clone event) {
        try {
            event.getOriginal().getCapability(BaublesCapabilities.BAUBLES).ifPresent(bco -> {
                CompoundNBT nbt = ((BaublesContainer) bco).serializeNBT();
                event.getOriginal().getCapability(BaublesCapabilities.BAUBLES).ifPresent(bcn -> {
                    ((BaublesContainer) bcn).deserializeNBT(nbt);
                });
            });
        } catch (Exception e) {
            System.out.println("Could not clone player [" + event.getOriginal().getName() + "] baubles when changing dimensions");
        }
    }

    @SubscribeEvent
    public static void attachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(BaublesAPI.MOD_ID, "container"), new BaublesContainerProvider((PlayerEntity) event.getObject()));
        }
    }

    @SubscribeEvent
    public static void playerJoin(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            syncSlots(player, Collections.singletonList(player));
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        if (target instanceof ServerPlayerEntity) {
            syncSlots((ServerPlayerEntity) target, Collections.singletonList(event.getPlayer()));
        }
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        // player events
        if (event.phase == TickEvent.Phase.END) {
            PlayerEntity player = event.player;
            player.getCapability(BaublesCapabilities.BAUBLES).ifPresent(IBaublesItemHandler::tick);
        }
    }

    @SubscribeEvent
    public static void rightClickItem(PlayerInteractEvent.RightClickItem event) {
        ItemStack itemstack = event.getPlayer().getHeldItem(event.getHand());

        if (itemstack.getItem() instanceof IBauble) {
            IBauble bauble = (IBauble) itemstack.getItem();

            IBaublesItemHandler itemHandler = BaublesAPI.getBaublesHandler(event.getPlayer()).orElseThrow(NullPointerException::new);
            int emptySlot = BaublesAPI.getEmptySlotForBaubleType(event.getPlayer(), bauble.getBaubleType(itemstack));

            if (emptySlot != -1) {
                itemHandler.setStackInSlot(emptySlot, itemstack.copy());
                itemstack.setCount(0);
            }
        } else if (itemstack.getCapability(BaublesCapabilities.ITEM_BAUBLE).isPresent()) {
            IBauble bauble = itemstack.getCapability(BaublesCapabilities.ITEM_BAUBLE).orElseThrow(NullPointerException::new);

            IBaublesItemHandler itemHandler = BaublesAPI.getBaublesHandler(event.getPlayer()).orElseThrow(NullPointerException::new);
            int emptySlot = BaublesAPI.getEmptySlotForBaubleType(event.getPlayer(), bauble.getBaubleType(itemstack));

            if (emptySlot != -1) {
                itemHandler.setStackInSlot(emptySlot, itemstack.copy());
                itemstack.setCount(0);
            }
        }
    }

    private static void syncSlots(PlayerEntity player, Collection<? extends PlayerEntity> receivers) {
        player.getCapability(BaublesCapabilities.BAUBLES).ifPresent(baubles -> {
            for (byte i = 0; i < baubles.getSlots(); i++) {
                syncSlot(player, i, baubles.getStackInSlot(i), receivers);
            }
        });
    }

    public static void syncSlot(PlayerEntity player, byte slot, ItemStack stack, Collection<? extends PlayerEntity> receivers) {
        SyncPacket pkt = new SyncPacket(player.getEntityId(), slot, stack);
        for (PlayerEntity receiver : receivers) {
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) receiver), pkt);
        }
    }

    @SubscribeEvent
    public static void playerDeath(LivingDropsEvent event) {
        if (event.getEntity() instanceof PlayerEntity && !event.getEntity().world.isRemote && !event.getEntity().world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
            dropItemsAt((PlayerEntity) event.getEntity(), event.getDrops());
        }
    }

    private static void dropItemsAt(PlayerEntity player, Collection<ItemEntity> drops) {
        player.getCapability(BaublesCapabilities.BAUBLES).ifPresent(baubles -> {
            for (int i = 0; i < baubles.getSlots(); ++i) {
                if (!baubles.getStackInSlot(i).isEmpty()) {
                    ItemEntity ei = new ItemEntity(player.world, player.getPosX(), player.getPosY() + player.getEyeHeight(), player.getPosZ(), baubles.getStackInSlot(i).copy());
                    ei.setPickupDelay(40);
                    drops.add(ei);
                    baubles.setStackInSlot(i, ItemStack.EMPTY);
                }
            }
        });
    }
}