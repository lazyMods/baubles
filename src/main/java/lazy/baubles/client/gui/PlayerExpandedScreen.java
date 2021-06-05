package lazy.baubles.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import lazy.baubles.container.PlayerExpandedContainer;
import lazy.baubles.proxy.ClientProxy;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class PlayerExpandedScreen extends DisplayEffectsScreen<PlayerExpandedContainer> {

    public static final ResourceLocation background = new ResourceLocation("baubles", "textures/gui/expanded_inventory.png");

    private float oldMouseX;
    private float oldMouseY;

    public PlayerExpandedScreen(PlayerExpandedContainer container, PlayerInventory inventory, ITextComponent name) {
        super(container, inventory, name);
    }

    @Override
    public void tick() { //tick
        this.menu.baubles.setEventBlock(false);
        this.checkEffectRendering();
        this.resetGuiLeft();
    }

    @Override
    protected void init() {
        this.buttons.clear();
        super.init();
        this.resetGuiLeft();
    }


    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        if (this.minecraft != null) {
            this.minecraft.font.draw(matrixStack, new TranslationTextComponent("container.crafting"), 115, 8, 4210752);
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) { //render
        this.renderBackground(matrixStack); //renderBackground
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY); //renderHoveredToolTip
        this.oldMouseX = (float) mouseX;
        this.oldMouseY = (float) mouseY;
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bind(background);
            int k = this.leftPos;
            int l = this.topPos;
            this.blit(matrixStack, k, l, 0, 0, this.imageWidth, this.imageHeight);
            for (int i1 = 0; i1 < this.menu.slots.size(); ++i1) {
                Slot slot = this.menu.slots.get(i1);
                if (slot.hasItem() && slot.getMaxStackSize() == 1) {
                    this.blit(matrixStack, k + slot.x, l + slot.y, 200, 0, 16, 16);
                }
            }
            InventoryScreen.renderEntityInInventory(k + 51, l + 75, 30, (float) (k + 51) - this.oldMouseX, (float) (l + 75 - 50) - this.oldMouseY, this.minecraft.player);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int what) {
        if (ClientProxy.KEY_BAUBLES.isActiveAndMatches(InputMappings.getKey(keyCode, scanCode))) {
            if (this.minecraft != null) {
                this.minecraft.player.closeContainer();
            }
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, what);
        }
    }

    private void resetGuiLeft() {
        this.leftPos = (this.width - this.imageWidth) / 2;
    }
}