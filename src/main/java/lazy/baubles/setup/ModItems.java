package lazy.baubles.setup;

import com.mojang.blaze3d.vertex.PoseStack;
import lazy.baubles.api.BaublesAPI;
import lazy.baubles.api.bauble.BaubleType;
import lazy.baubles.api.bauble.IBauble;
import lazy.baubles.api.render.IRenderBauble;
import lazy.baubles.datadriven.BaubleJson;
import lazy.baubles.datadriven.BaubleJsonItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BaublesAPI.MOD_ID);

    public static void init() {
        if (!BaubleJson.loadBaubles().isEmpty())
            ITEMS.register("bauble", () -> new BaubleJsonItem(BaubleJson.loadBaubles()));

        //ITEMS.register("test_bauble", TestItem::new);
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static class TestItem extends Item implements IBauble, IRenderBauble {

        public TestItem() {
            super(new Properties().tab(CreativeModeTab.TAB_FOOD));
        }

        @Override
        public BaubleType getBaubleType(ItemStack stack) {
            return BaubleType.TRINKET;
        }

        @Override
        public void onPlayerBaubleRender(PoseStack stack, Player player, RenderType type, float partialTicks) {
            Helper.applySneakingRotation(stack);
        }
    }
}
