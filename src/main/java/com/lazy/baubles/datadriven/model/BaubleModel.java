package com.lazy.baubles.datadriven.model;

import java.util.List;

public class BaubleModel {

    private final String type;
    private final String registryName;
    private final boolean glint;
    private final boolean showEffectsTooltip;
    private final String displayName;
    private final List<String> tooltips;
    private final List<String> requireMod;
    private final List<EffectModel> effects;

    public BaubleModel(String type, String registryName, boolean glint, boolean showEffectsTooltip, String displayName, List<String> tooltips, List<String> requireMods, List<EffectModel> effects) {
        this.type = type;
        this.registryName = registryName;
        this.glint = glint;
        this.displayName = displayName;
        this.tooltips = tooltips;
        this.effects = effects;
        this.showEffectsTooltip = showEffectsTooltip;
        this.requireMod = requireMods;
    }

    public String getType() {
        return this.type;
    }

    public boolean isGlint() {
        return this.glint;
    }

    public boolean canShowEffectsTooltip() {
        return this.showEffectsTooltip;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public List<String> getTooltips() {
        return this.tooltips;
    }

    public List<EffectModel> getEffects() {
        return this.effects;
    }

    public List<String> getRequiredMods() {
        return this.requireMod;
    }

    public String getRegistryName() {
        return this.registryName;
    }

    @Override
    public String toString() {
        return "BaubleModel{" +
                "type='" + type + '\'' +
                ", registryName='" + registryName + '\'' +
                ", glint=" + glint +
                ", displayName='" + displayName + '\'' +
                ", tooltips=" + tooltips +
                ", potionIds=" + effects +
                '}';
    }
}
