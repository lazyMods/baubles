package lazy.baubles.setup;

import lazy.baubles.api.BaublesAPI;
import lazy.baubles.datadriven.BaubleJson;
import lazy.baubles.datadriven.BaubleJsonItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BaublesAPI.MOD_ID);

    public static void init() {
        if (!BaubleJson.loadBaubles().isEmpty())
            ITEMS.register("bauble", () -> new BaubleJsonItem(BaubleJson.loadBaubles()));
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
