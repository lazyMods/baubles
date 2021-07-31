package lazy.baubles.datadriven.model;

public record EffectModel(int effectLevel, String effectRegistryName) {

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
