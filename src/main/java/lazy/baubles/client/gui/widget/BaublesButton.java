package lazy.baubles.client.gui.widget;

import lazy.baubles.client.gui.PlayerExpandedScreen;
import lazy.baubles.network.OpenBaublesInvPacket;
import lazy.baubles.network.OpenNormalInvPacket;
import lazy.baubles.network.PacketHandler;
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

    public BaublesButton(ContainerScreen parentGui, int x, int y, int width, int height) {
        super(x + parentGui.getGuiLeft(), parentGui.getGuiTop() + y, width, height, new StringTextComponent(""));
        this.parentGui = parentGui;
    }

    @Override
    public void onPress() { //onPress
        if (parentGui instanceof InventoryScreen) {
            PacketHandler.INSTANCE.sendToServer(new OpenBaublesInvPacket());
        } else {
            PacketHandler.INSTANCE.sendToServer(new OpenNormalInvPacket());
            this.displayNormalInventory();
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) { //render
        if(!Minecraft.getInstance().player.isCreative()){
            if (this.visible) {
                FontRenderer fontrenderer = Minecraft.getInstance().fontRenderer;
                Minecraft.getInstance().getTextureManager().bindTexture(PlayerExpandedScreen.background);
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                Minecraft.getInstance().getTextureManager().bindTexture(PlayerExpandedScreen.background);
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.isHovered = mouseX >= x && mouseY >= this.y && mouseX < x + this.width && mouseY < this.y + this.height;
                GlStateManager.enableBlend();
                GlStateManager.blendFuncSeparate(770, 771, 1, 0);
                GlStateManager.blendFuncSeparate(770, 771, 1, 0);
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                GlStateManager.pushMatrix();
                GlStateManager.translatef(0, 0, 200);
                GlStateManager.translatef(0, 0, 200);
                if (!isHovered) {
                    this.blit(matrixStack, x, this.y, 200, 48, 10, 10); //blit
                } else {
                    this.blit(matrixStack, x, this.y, 210, 48, 10, 10); //blit
                    fontrenderer.drawString(matrixStack, new TranslationTextComponent("button.displayText").getString(), x + 5, this.y + this.height, 0xffffff); //drawCenteredString
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