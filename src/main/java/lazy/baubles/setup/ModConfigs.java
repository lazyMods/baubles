package lazy.baubles.setup;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import lazy.baubles.api.BaublesAPI;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

public class ModConfigs {

    private static final ForgeConfigSpec CLIENT;

    public static final ForgeConfigSpec.BooleanValue RENDER_BAUBLE;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push(BaublesAPI.MOD_ID);
        RENDER_BAUBLE = builder.comment("When enabled baubles can render on player.").define("render_baubles", true);
        builder.pop();

        CLIENT = builder.build();
    }

    public static void registerAndLoadConfig() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT);
        CommentedFileConfig config = CommentedFileConfig.builder(FMLPaths.CONFIGDIR.get().resolve(BaublesAPI.MOD_ID.concat("-client.toml"))).sync().writingMode(WritingMode.REPLACE).build();
        config.load();
        CLIENT.setConfig(config);
    }
}
