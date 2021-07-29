package lazy.baubles.client.gui.widget;

import lazy.baubles.client.gui.PlayerExpandedScreen;
import lazy.baubles.network.OpenBaublesInvPacket;
import lazy.baubles.network.OpenNormalInvPacket;
import lazy.baubles.network.PacketHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.lwjgl.opengl.GL11;

public class BaublesButton extends AbstractButton {

    private final AbstractContainerScreen parentGui;

    public BaublesButton(AbstractContainerScreen parentGui, int x, int y, int width, int height) {
        super(x + parentGui.getGuiLeft(), parentGui.getGuiTop() + y, width, height, new TextComponent(""));
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
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) { //render
        if(!Minecraft.getInstance().player.isCreative()){
            if (this.visible) {
                Font fontrenderer = Minecraft.getInstance().font;
                Minecraft.getInstance().getTextureManager().bindForSetup(PlayerExpandedScreen.background);
                //GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
                Minecraft.getInstance().getTextureManager().bindForSetup(PlayerExpandedScreen.background);
                //GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.isHovered = mouseX >= x && mouseY >= this.y && mouseX < x + this.width && mouseY < this.y + this.height;
                GlStateManager._enableBlend();
                GlStateManager._blendFuncSeparate(770, 771, 1, 0);
                GlStateManager._blendFuncSeparate(770, 771, 1, 0);
                GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                //GlStateManager._pushMatrix();
                //GlStateManager._translatef(0, 0, 200);
                //GlStateManager._translatef(0, 0, 200);
                if (!isHovered) {
                    this.blit(matrixStack, x, this.y, 200, 48, 10, 10); //blit
                } else {
                    this.blit(matrixStack, x, this.y, 210, 48, 10, 10); //blit
                    fontrenderer.draw(matrixStack, new TranslatableComponent("button.displayText").getString(), x + 5, this.y + this.height, 0xffffff); //drawCenteredString
                }
                //GlStateManager._popMatrix();
            }
        }
    }

    public void displayNormalInventory() {
        InventoryScreen gui = new InventoryScreen(Minecraft.getInstance().player);
        Minecraft.getInstance().setScreen(gui);
    }

    @Override
    public void updateNarration(NarrationElementOutput p_169152_) {

    }
}