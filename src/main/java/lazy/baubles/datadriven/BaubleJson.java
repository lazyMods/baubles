package lazy.baubles.datadriven;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lazy.baubles.datadriven.model.BaubleModel;
import lazy.baubles.datadriven.model.EffectModel;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BaubleJson {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final Path BAUBLES_PATH = Paths.get(FMLPaths.GAMEDIR.get().toString() + "/baubles/");

    public static List<BaubleModel> loadBaubles() {
        List<BaubleModel> baubles = new ArrayList<>();
        for (File file : getAllJsonBaubles()) {
            if (baubles.stream().noneMatch(baubleModel -> baubleModel.getRegistryName().equals(read(file).getRegistryName()))) {
                baubles.add(read(file));
            } else {
                System.out.println("Bauble with " + read(file).getRegistryName() + " already exists!");
            }
        }
        return baubles;
    }


    public static BaubleModel read(File file) {
        BaubleModel baubleModel = null;
        try {
            JsonObject json = JSONUtils.fromJson(new FileReader(file));
            String registryName = JSONUtils.getString(json, "registryName", "");
            String type = JSONUtils.getString(json, "type", "");
            boolean glint = JSONUtils.getBoolean(json, "glint", false);
            boolean showEffectTooltip = JSONUtils.getBoolean(json, "showEffectsTooltip", false);
            String displayName = JSONUtils.getString(json, "displayName", "Ring");
            List<String> tooltips = new ArrayList<>();
            List<String> requireMod = new ArrayList<>();
            List<EffectModel> effects = new ArrayList<>();

            if (JSONUtils.hasField(json, "tooltips")) {
                tooltips = Arrays.asList(GSON.fromJson(JSONUtils.getJsonArray(json, "tooltips"), String[].class).clone());
            }

            if (JSONUtils.hasField(json, "requireMod")) {
                requireMod = Arrays.asList(GSON.fromJson(JSONUtils.getJsonArray(json, "requireMod"), String[].class).clone());
            }

            if (JSONUtils.hasField(json, "effects")) {
                effects = Arrays.asList(GSON.fromJson(JSONUtils.getJsonArray(json, "effects"), EffectModel[].class).clone());
            }

            baubleModel = new BaubleModel(type, registryName, glint, showEffectTooltip, displayName, tooltips, requireMod, effects);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return baubleModel;
    }

    public static List<File> getAllJsonBaubles() {
        List<File> jsonBaubles = new ArrayList<>();
        try {
            Files.createDirectories(BAUBLES_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (Stream<Path> walk = Files.walk(BAUBLES_PATH)) {

            List<String> result = walk.map(x -> x.toString()).filter(f -> f.endsWith(".json")).collect(Collectors.toList());

            result.forEach(s -> jsonBaubles.add(new File(s)));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonBaubles;
    }
}
