package com.lazy.baubles.client.renderer;

import com.lazy.baubles.api.cap.BaublesCapabilities;
import com.lazy.baubles.api.cap.IBaublesItemHandler;
import com.lazy.baubles.api.render.IRenderBauble;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class BaublesRenderLayer<T extends PlayerEntity, M extends PlayerModel<T>> extends LayerRenderer<T, M> {

    public BaublesRenderLayer(IEntityRenderer<T, M> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int i, PlayerEntity player, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        //TODO
		/*if(!Config.renderBaubles || player.getActivePotionEffect(MobEffects.INVISIBILITY) != null)
			return;
*/
        player.getCapability(BaublesCapabilities.BAUBLES).ifPresent(inv -> {
            dispatchRenders(inv, player, IRenderBauble.RenderType.BODY, partialTicks);
            float yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * partialTicks;
            float yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
            float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;

            GlStateManager.pushMatrix();
            GlStateManager.rotatef(yawOffset, 0, -1, 0);
            GlStateManager.rotatef(yaw - 270, 0, 1, 0);
            GlStateManager.rotatef(pitch, 0, 0, 1);
            dispatchRenders(inv, player, IRenderBauble.RenderType.HEAD, partialTicks);
            GlStateManager.popMatrix();
        });
    }

    private void dispatchRenders(IBaublesItemHandler inv, PlayerEntity player, IRenderBauble.RenderType type, float partialTicks) {
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                stack.getCapability(BaublesCapabilities.ITEM_BAUBLE).ifPresent(bauble -> {
                    if (bauble instanceof IRenderBauble) {
                        GlStateManager.pushMatrix();
                        GL11.glColor3ub((byte) 255, (byte) 255, (byte) 255);
                        GlStateManager.color4f(1F, 1F, 1F, 1F);
                        ((IRenderBauble) bauble).onPlayerBaubleRender(player, type, partialTicks);
                        GlStateManager.popMatrix();
                    }
                });
            }
        }
    }
}