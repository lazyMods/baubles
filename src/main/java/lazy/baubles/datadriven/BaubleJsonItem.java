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
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class BaubleJsonItem extends Item implements IBauble {

    public List<BaubleModel> model;

    public BaubleJsonItem(List<BaubleModel> model) {
        super(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS));
        this.setRegistryName("bauble");
        this.model = model;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        super.fillItemCategory(group, items);
        if (group != getItemCategory()) return;
        model.forEach(model -> {
            for (String modId : model.getRequiredMods()) {
                if (!ModList.get().isLoaded(modId)) return;
            }
            ItemStack stack = new ItemStack(this);
            stack.setTag(new CompoundTag());
            if (stack.getTag() != null) stack.getTag().putString("registryName", model.getRegistryName());
            items.add(stack);
        });
    }


    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (stack.getTag() != null) {
            model.forEach(model -> {
                if (this.isRing(stack, model.getRegistryName())) {
                    model.getTooltips().forEach(s -> tooltip.add(new TextComponent(s)));
                    if (model.canShowEffectsTooltip()) {
                        model.getEffects().forEach(effectModel -> {
                            if (!ForgeRegistries.POTIONS.containsKey(new ResourceLocation(effectModel.getEffectRegistryName())))
                                return;
                            MobEffect effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(effectModel.getEffectRegistryName()));
                            if (effect != null) {
                                TextComponent effectName = new TextComponent("");
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
        AtomicBoolean has = new AtomicBoolean(false);
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
    public Component getName(ItemStack stack) {
        TextComponent textComponent = new TextComponent("");
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
        StringBuilder type = new StringBuilder();
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
                        if (ForgeRegistries.POTIONS.containsKey(new ResourceLocation(effectModel.getEffectRegistryName()))) {
                            MobEffect effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(effectModel.getEffectRegistryName()));
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
                        MobEffect effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(effectModel.getEffectRegistryName()));
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
        switch (integer) {
            case 0:
                return "I";
            case 1:
                return "II";
            case 2:
                return "III";
            case 3:
                return "IV";
            case 4:
                return "V";
            case 5:
                return "VI";
            case 6:
                return "VII";
            case 7:
                return "VIII";
            case 8:
                return "IX";
            case 9:
                return "X";
        }
        return String.valueOf(integer);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Reg {
        @SubscribeEvent
        public static void onItemRegister(RegistryEvent.Register<Item> e) {
            if (!BaubleJson.loadBaubles().isEmpty()) {
                e.getRegistry().register(new BaubleJsonItem(BaubleJson.loadBaubles()));
            }
        }
    }
}
