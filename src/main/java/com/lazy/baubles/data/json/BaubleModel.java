package com.lazy.baubles.data.json;

import java.util.List;

public class BaubleModel {

    public String type;
    public String registryName;
    public boolean glint;
    public String displayName;
    public List<String> tooltips;
    public List<EffectModel> potionIds;

    public BaubleModel(String type, String registryName, boolean glint, String displayName, List<String> tooltips, List<EffectModel> potionIds) {
        this.type = type;
        this.registryName = registryName;
        this.glint = glint;
        this.displayName = displayName;
        this.tooltips = tooltips;
        this.potionIds = potionIds;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isGlint() {
        return this.glint;
    }

    public void setGlint(boolean glint) {
        this.glint = glint;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getTooltips() {
        return this.tooltips;
    }

    public void setTooltips(List<String> tooltips) {
        this.tooltips = tooltips;
    }

    public List<EffectModel> getPotionIds() {
        return this.potionIds;
    }

    public void setPotionIds(List<EffectModel> potionIds) {
        this.potionIds = potionIds;
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
                ", potionIds=" + potionIds +
                '}';
    }
}
