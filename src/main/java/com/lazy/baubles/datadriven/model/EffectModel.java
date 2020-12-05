package com.lazy.baubles.datadriven.model;

public class EffectModel {

    private final int effectLevel;
    private final String effectRegistryName;

    public EffectModel(int effectLevel, String effectRegistryName) {
        this.effectLevel = effectLevel;
        this.effectRegistryName = effectRegistryName;
    }

    public int getEffectLevel() {
        return this.effectLevel;
    }

    public String getEffectRegistryName() {
        return this.effectRegistryName;
    }

    @Override
    public String toString() {
        return "EffectModel{" +
                "effectLevel=" + effectLevel +
                ", effectRegistryName='" + effectRegistryName + '\'' +
                '}';
    }
}
