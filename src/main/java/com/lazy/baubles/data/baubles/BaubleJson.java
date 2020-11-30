package com.lazy.baubles.data.baubles;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.lazy.baubles.data.baubles.model.BaubleModel;
import com.lazy.baubles.data.baubles.model.EffectModel;
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
            baubles.add(read(file));
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
            String displayName = JSONUtils.getString(json, "displayName", "Ring");
            List<String> tooltips = new ArrayList<>();
            List<EffectModel> effects = Arrays.asList(GSON.fromJson(JSONUtils.getJsonArray(json, "potionIds"), EffectModel[].class));

            if (JSONUtils.hasField(json, "tooltips")) {
                tooltips = Arrays.asList(GSON.fromJson(JSONUtils.getJsonArray(json, "tooltips"), String[].class).clone());
            }

            if (JSONUtils.hasField(json, "potionIds")) {
                effects = Arrays.asList(GSON.fromJson(JSONUtils.getJsonArray(json, "potionIds"), EffectModel[].class).clone());
            }

            baubleModel = new BaubleModel(type, registryName, glint, displayName, tooltips, effects);
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
