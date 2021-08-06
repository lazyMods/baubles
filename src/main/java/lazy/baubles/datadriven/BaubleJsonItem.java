package lazy.baubles.datadriven;

import lazy.baubles.api.bauble.BaubleType;
import lazy.baubles.api.bauble.IBauble;
import lazy.baubles.datadriven.model.BaubleModel;
import lazy.baubles.datadriven.model.EffectModel;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class BaubleJsonItem extends Item implements IBauble {

    public List<BaubleModel> model;

    public BaubleJsonItem(List<BaubleModel> model) {
        super(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS));
        this.model = model;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        super.fillItemCategory(group, items);
        if (group != getItemCategory()) return;
        model.forEach(model -> {
            for (var modId : model.getRequiredMods()) {
                if (!ModList.get().isLoaded(modId)) return;
            }
            var stack = new ItemStack(this);
            stack.setTag(new CompoundTag());
            if (stack.getTag() != null) stack.getTag().putString("registryName", model.getRegistryName());
            items.add(stack);
        });
    }


    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (stack.getTag() != null) {
            model.forEach(model -> {
                if (this.isRing(stack, model.getRegistryName())) {
                    model.getTooltips().forEach(s -> tooltip.add(new TextComponent(s)));
                    if (model.canShowEffectsTooltip()) {
                        model.getEffects().forEach(effectModel -> {
                            if (!ForgeRegistries.MOB_EFFECTS.containsKey(new ResourceLocation(effectModel.getEffectRegistryName())))
                                return;
                            var effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effectModel.getEffectRegistryName()));
                            if (effect != null) {
                                var effectName = new TextComponent("");
                                effectName.append(effect.getDisplayName().getString() + " " + getFromInteger(Math.min(effectModel.getEffectLevel(), 99)));
                                tooltip.add(effectName);
                            }
                        });
                    }
                }
            });
        }
    }


    @Override
    public boolean isFoil(ItemStack stack) {
        var has = new AtomicBoolean(false);
        if (stack.getTag() != null) {
            model.forEach(model -> {
                if (this.isRing(stack, model.getRegistryName())) {
                    has.set(model.isGlint());
                }
            });
        }
        return has.get();
    }

    @Override
    @Nonnull
    public Component getName(ItemStack stack) {
        var textComponent = new TextComponent("");
        if (stack.getTag() != null) {
            model.forEach(model -> {
                if (this.isRing(stack, model.getRegistryName())) {
                    textComponent.append(model.getDisplayName());
                }
            });
        }
        return textComponent.getString().equals("") ? super.getName(stack) : textComponent;
    }

    @Override
    public BaubleType getBaubleType(ItemStack stack) {
        var type = new StringBuilder();
        if (stack.getTag() != null) {
            model.forEach(model -> {
                if (this.isRing(stack, model.getRegistryName())) {
                    type.append(model.getType());
                }
            });
        }
        return BaubleType.getFromString(type.toString());
    }

    @Override
    public void onEquipped(LivingEntity player, ItemStack stack) {
        if (stack.getTag() != null) {
            model.forEach(model -> {
                if (this.isRing(stack, model.getRegistryName())) {
                    for (EffectModel effectModel : model.getEffects()) {
                        if (ForgeRegistries.MOB_EFFECTS.containsKey(new ResourceLocation(effectModel.getEffectRegistryName()))) {
                            var effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effectModel.getEffectRegistryName()));
                            if (effect != null)
                                player.addEffect(new MobEffectInstance(effect, 999999, Math.min(effectModel.getEffectLevel(), 99)));
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onUnequipped(LivingEntity player, ItemStack stack) {
        if (stack.getTag() != null) {
            model.forEach(model -> {
                if (this.isRing(stack, model.getRegistryName())) {
                    for (EffectModel effectModel : model.getEffects()) {
                        var effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effectModel.getEffectRegistryName()));
                        if (effect != null) player.removeEffect(effect);
                    }
                }
            });
        }
    }

    private boolean isRing(ItemStack stack, String registryName) {
        return stack.getTag() != null && stack.getTag().contains("registryName") && stack.getTag().getString("registryName").equals(registryName);
    }

    public void setModel(List<BaubleModel> loadBaubles) {
        this.model = loadBaubles;
    }

    public String getFromInteger(int integer) {
        return switch (integer) {
            case 0 -> "I";
            case 1 -> "II";
            case 2 -> "III";
            case 3 -> "IV";
            case 4 -> "V";
            case 5 -> "VI";
            case 6 -> "VII";
            case 7 -> "VIII";
            case 8 -> "IX";
            case 9 -> "X";
            default -> String.valueOf(integer);
        };
    }
}
