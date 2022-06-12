package lazy.baubles.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import lazy.baubles.Baubles;
import lazy.baubles.container.PlayerExpandedContainer;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;

public class PlayerExpandedScreen extends EffectRenderingInventoryScreen<PlayerExpandedContainer> {

    public static final ResourceLocation BACKGROUND = new ResourceLocation("baubles", "textures/gui/expanded_inventory.png");

    private float oldMouseX;
    private float oldMouseY;

    public PlayerExpandedScreen(PlayerExpandedContainer container, Inventory inventory, Component name) {
        super(container, inventory, name);
    }

    @Override
    public void containerTick() {
        this.menu.baubles.setEventBlock(false);
        this.canSeeEffects();
        this.resetGuiLeft();
    }

    @Override
    protected void init() {
        this.renderables.clear();
        super.init();
        this.resetGuiLeft();
    }


    @Override
    protected void renderLabels(@Nonnull PoseStack matrixStack, int mouseX, int mouseY) {
        if (this.minecraft != null) {
            this.minecraft.font.draw(matrixStack, Component.translatable("container.crafting"), 115, 8, 4210752);
        }
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        this.oldMouseX = (float) mouseX;
        this.oldMouseY = (float) mouseY;
    }

    @Override
    protected void renderBg(@Nonnull PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        if (this.minecraft != null && this.minecraft.player != null) {
            int k = this.leftPos;
            int l = this.topPos;
            this.blit(matrixStack, k, l, 0, 0, this.imageWidth, this.imageHeight);
            for (int i1 = 0; i1 < this.menu.slots.size(); ++i1) {
                var slot = this.menu.slots.get(i1);
                if (slot.hasItem() && slot.getMaxStackSize() == 1) {
                    this.blit(matrixStack, k + slot.x, l + slot.y, 200, 0, 16, 16);
                }
            }
            InventoryScreen.renderEntityInInventory(k + 51, l + 75, 30, (float) (k + 51) - this.oldMouseX, (float) (l + 75 - 50) - this.oldMouseY, this.minecraft.player);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int what) {
        if (Baubles.KEY_BAUBLES.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode))) {
            if (this.minecraft != null && this.minecraft.player != null) {
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