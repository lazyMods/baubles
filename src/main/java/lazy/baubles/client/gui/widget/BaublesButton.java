package lazy.baubles.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import lazy.baubles.client.gui.PlayerExpandedScreen;
import lazy.baubles.network.msg.OpenBaublesInvPacket;
import lazy.baubles.network.msg.OpenNormalInvPacket;
import lazy.baubles.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

@SuppressWarnings("rawtypes")
public class BaublesButton extends AbstractButton {

    private final AbstractContainerScreen parentGui;
    private final Minecraft minecraft;

    public BaublesButton(AbstractContainerScreen parentGui, int x, int y, int width, int height) {
        super(x + parentGui.getGuiLeft(), parentGui.getGuiTop() + y, width, height, new TextComponent(""));
        this.parentGui = parentGui;
        this.minecraft = Minecraft.getInstance();
    }

    @Override
    public void onPress() { //onPress
        if (parentGui instanceof InventoryScreen) {
            PacketHandler.INSTANCE.sendToServer(new OpenBaublesInvPacket());
        } else {
            if (this.minecraft.player != null) {
                PacketHandler.INSTANCE.sendToServer(new OpenNormalInvPacket());
                this.displayNormalInventory(this.minecraft.player);
            }
        }
    }

    @Override
    public void render(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (this.minecraft == null) return;
        if (this.minecraft.player != null && !this.minecraft.player.isCreative()) {
            if (this.visible) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                RenderSystem.setShaderTexture(0, PlayerExpandedScreen.BACKGROUND);

                this.isHovered = mouseX >= x && mouseY >= this.y && mouseX < x + this.width && mouseY < this.y + this.height;

                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(770, 771, 1, 0);
                RenderSystem.blendFuncSeparate(770, 771, 1, 0);
                RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                poseStack.pushPose();
                poseStack.translate(0, 0, 200);
                poseStack.translate(0, 0, 200);
                if (!isHovered) {
                    this.blit(poseStack, x, this.y, 200, 48, 10, 10);
                } else {
                    this.blit(poseStack, x, this.y, 210, 48, 10, 10);
                    this.minecraft.font.draw(poseStack, new TranslatableComponent("button.displayText").getString(), x + 5, this.y + this.height, 0xffffff);
                }
                poseStack.popPose();
            }
        }
    }

    public void displayNormalInventory(Player player) {
        Minecraft.getInstance().setScreen(new InventoryScreen(player));
    }

    @Override
    public void updateNarration(@Nonnull NarrationElementOutput output) {
        this.defaultButtonNarrationText(output);
    }
}