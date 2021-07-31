package lazy.baubles.datadriven.model;

import java.util.List;

public record BaubleModel(String type, String registryName, boolean glint, boolean showEffectsTooltip,
                          String displayName, List<String> tooltips, List<String> requireMods,
                          List<EffectModel> effects) {

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
        return this.requireMods;
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
