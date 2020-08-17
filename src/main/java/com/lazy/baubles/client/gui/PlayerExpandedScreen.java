package com.lazy.baubles.client.gui;

import com.lazy.baubles.container.PlayerExpandedContainer;
import com.lazy.baubles.proxy.ClientProxy;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
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
    public void func_231023_e_() { //tick
        this.container.baubles.setEventBlock(false);
        this.updateActivePotionEffects();
        this.resetGuiLeft();
    }

    @Override
    protected void func_231160_c_() { //init
        this.field_230710_m_.clear(); //this.buttons
        super.func_231160_c_();
        this.resetGuiLeft();
    }

    @Override
    protected void func_230451_b_(MatrixStack matrixStack, int p_146979_1_, int p_146979_2_) { //drawGuiContainerForegroundLayer
        if (this.field_230706_i_ != null) { //this.minecraft
            this.field_230706_i_.fontRenderer.func_243248_b(matrixStack, new TranslationTextComponent("container.crafting"), 115, 8, 4210752);
        }
    }

    @Override
    public void func_230430_a_(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) { //render
        this.func_230446_a_(matrixStack); //renderBackground
        super.func_230430_a_(matrixStack, mouseX, mouseY, partialTicks);
        this.func_230459_a_(matrixStack, mouseX, mouseY); //renderHoveredToolTip
        this.oldMouseX = (float) mouseX;
        this.oldMouseY = (float) mouseY;
    }

    @Override
    protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) { //drawGuiContainerBackgroundLayer
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.field_230706_i_ != null) {
            this.field_230706_i_.getTextureManager().bindTexture(background);
            int k = this.guiLeft;
            int l = this.guiTop;
            this.func_238474_b_(matrixStack, k, l, 0, 0, this.xSize, this.ySize);  //blit
            for (int i1 = 0; i1 < this.container.inventorySlots.size(); ++i1) {
                Slot slot = this.container.inventorySlots.get(i1);
                if (slot.getHasStack() && slot.getSlotStackLimit() == 1) {
                    this.func_238474_b_(matrixStack, k + slot.xPos, l + slot.yPos, 200, 0, 16, 16);
                }
            }
            InventoryScreen.drawEntityOnScreen(k + 51, l + 75, 30, (float) (k + 51) - this.oldMouseX, (float) (l + 75 - 50) - this.oldMouseY, this.field_230706_i_.player);
        }
    }

    @Override
    public boolean func_231046_a_(int keyCode, int scanCode, int what) { //keyPressed
        if (ClientProxy.KEY_BAUBLES.isActiveAndMatches(InputMappings.getInputByCode(keyCode, scanCode))) {
            if (this.field_230706_i_ != null) {
                this.field_230706_i_.player.closeScreen();
            }
            return true;
        } else {
            return super.func_231046_a_(keyCode, scanCode, what);
        }
    }

    private void resetGuiLeft() {
        this.guiLeft = (this.field_230708_k_ - this.xSize) / 2; //width
    }
}