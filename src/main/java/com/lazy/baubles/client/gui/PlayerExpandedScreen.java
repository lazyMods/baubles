package com.lazy.baubles.client.gui;

import com.lazy.baubles.container.PlayerExpandedContainer;
import com.lazy.baubles.proxy.ClientProxy;
import com.mojang.blaze3d.platform.GlStateManager;
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
    public void tick() {
        this.container.baubles.setEventBlock(false);
        updateActivePotionEffects();
        resetGuiLeft();
    }

    @Override
    protected void init() {
        this.buttons.clear();
        super.init();
        this.resetGuiLeft();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        if (this.minecraft != null) {
            this.minecraft.fontRenderer.drawString(new TranslationTextComponent("container.crafting").getFormattedText(), 115, 8, 4210752);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        this.oldMouseX = (float) mouseX;
        this.oldMouseY = (float) mouseY;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bindTexture(background);
            int k = this.guiLeft;
            int l = this.guiTop;
            this.blit(k, l, 0, 0, this.xSize, this.ySize);

            for (int i1 = 0; i1 < this.container.inventorySlots.size(); ++i1) {
                Slot slot = this.container.inventorySlots.get(i1);
                if (slot.getHasStack() && slot.getSlotStackLimit() == 1) {
                    this.blit(k + slot.xPos, l + slot.yPos, 200, 0, 16, 16);
                }
            }
            InventoryScreen.drawEntityOnScreen(k + 51, l + 75, 30, (float) (k + 51) - this.oldMouseX, (float) (l + 75 - 50) - this.oldMouseY, this.minecraft.player);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int what) {
        if (ClientProxy.KEY_BAUBLES.isActiveAndMatches(InputMappings.getInputByCode(keyCode, scanCode))) {
            if (this.minecraft != null) {
                this.minecraft.player.closeScreen();
            }
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, what);
        }
    }

    private void resetGuiLeft() {
        this.guiLeft = (this.width - this.xSize) / 2;
    }
}