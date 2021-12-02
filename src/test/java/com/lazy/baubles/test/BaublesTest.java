package com.lazy.baubles.test;

import lazy.baubles.api.bauble.BaubleType;
import lazy.baubles.api.bauble.IBauble;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;

@Mod("baubles_test")
public class BaublesTest {

    public static final CreativeModeTab TAB = new CreativeModeTab("baubles_test") {
        @Override
        @Nonnull
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.FLY_BAUBLE.get());
        }
    };

    public BaublesTest() {
        ModItems.init();
    }

    public static class ModItems {

        public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "baubles_test");

        public static final RegistryObject<Item> FLY_BAUBLE = ITEMS.register("fly_ring", FlyRing::new);

        public static void init() {
            ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }
    }

    private static class FlyRing extends Item implements IBauble {

        public FlyRing() {
            super(new Properties().stacksTo(1).tab(TAB));
        }

        @Override
        public BaubleType getBaubleType(ItemStack stack) {
            return BaubleType.RING;
        }

        @Override
        public void onEquipped(LivingEntity player, ItemStack stack) {
            if (player instanceof Player p) {
                p.getAbilities().mayfly = true;
            }
        }

        @Override
        public void onUnequipped(LivingEntity player, ItemStack stack) {
            if (player instanceof Player p) {
                p.getAbilities().mayfly = false;
            }
        }
    }
}
