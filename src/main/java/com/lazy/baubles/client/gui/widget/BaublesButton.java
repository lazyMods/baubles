package com.lazy.baubles.client.gui.widget;

import com.lazy.baubles.client.gui.PlayerExpandedScreen;
import com.lazy.baubles.network.OpenBaublesInvPacket;
import com.lazy.baubles.network.OpenNormalInvPacket;
import com.lazy.baubles.network.PacketHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.opengl.GL11;

public class BaublesButton extends AbstractButton {

    private final ContainerScreen parentGui;

    //temp
    public boolean _isHovered = this.field_230692_n_;
    public boolean _visible = this.field_230693_o_;
    public int _x = this.field_230690_l_;
    public int _y = this.field_230691_m_;
    public int _width = this.field_230688_j_;
    public int _height = this.field_230689_k_;


    public BaublesButton(ContainerScreen parentGui, int x, int y, int width, int height) {
        super(x + parentGui.getGuiLeft(), parentGui.getGuiTop() + y, width, height, new StringTextComponent(""));
        this.parentGui = parentGui;
    }

    @Override
    public void func_230930_b_() { //onPress
        if (parentGui instanceof InventoryScreen) {
            PacketHandler.INSTANCE.sendToServer(new OpenBaublesInvPacket());
        } else {
            PacketHandler.INSTANCE.sendToServer(new OpenNormalInvPacket());
            this.displayNormalInventory();
        }
    }

    @Override
    public void func_230430_a_(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) { //render
        if(!Minecraft.getInstance().player.isCreative()){
            if (this._visible) {
                FontRenderer fontrenderer = Minecraft.getInstance().fontRenderer;
                Minecraft.getInstance().getTextureManager().bindTexture(PlayerExpandedScreen.background);
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                Minecraft.getInstance().getTextureManager().bindTexture(PlayerExpandedScreen.background);
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                this._isHovered = mouseX >= _x && mouseY >= this._y && mouseX < _x + this._width && mouseY < this._y + this._height;
                GlStateManager.enableBlend();
                GlStateManager.blendFuncSeparate(770, 771, 1, 0);
                GlStateManager.blendFuncSeparate(770, 771, 1, 0);
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                GlStateManager.pushMatrix();
                GlStateManager.translatef(0, 0, 200);
                GlStateManager.translatef(0, 0, 200);
                if (!_isHovered) {
                    this.func_238474_b_(matrixStack, _x, this._y, 200, 48, 10, 10); //blit
                } else {
                    this.func_238474_b_(matrixStack, _x, this._y, 210, 48, 10, 10); //blit
                    this.func_238471_a_(matrixStack, fontrenderer, new TranslationTextComponent("button.displayText").getString(), _x + 5, this._y + this._height, 0xffffff); //drawCenteredString
                }
                GlStateManager.popMatrix();
            }
        }
    }

    public void displayNormalInventory() {
        InventoryScreen gui = new InventoryScreen(Minecraft.getInstance().player);
        Minecraft.getInstance().displayGuiScreen(gui);
    }
}