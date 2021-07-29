package lazy.baubles.client.renderer;

import lazy.baubles.api.cap.BaublesCapabilities;
import lazy.baubles.api.cap.IBaublesItemHandler;
import lazy.baubles.api.render.IRenderBauble;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.opengl.GL11;

@SuppressWarnings("ConstantConditions")
public class BaublesRenderLayer<T extends Player, M extends PlayerModel<T>> extends RenderLayer<T, M> {

    public BaublesRenderLayer(RenderLayerParent<T, M> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource iRenderTypeBuffer, int i, Player player, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        //TODO
		/*if(!Config.renderBaubles || player.getActivePotionEffect(MobEffects.INVISIBILITY) != null)
			return;
*/
        player.getCapability(BaublesCapabilities.BAUBLES).ifPresent(inv -> {
            dispatchRenders(inv, player, IRenderBauble.RenderType.BODY, partialTicks);

            //TODO

            float yaw = player.yHeadRotO + (player.yHeadRot - player.yHeadRotO) * partialTicks;
            float yawOffset = player.yBodyRotO + (player.yBodyRot - player.yBodyRotO) * partialTicks;
            //float pitch = player.xRotO + (player.xRot - player.xRotO) * partialTicks;

            //GlStateManager._pushMatrix();
            //GlStateManager._rotatef(yawOffset, 0, -1, 0);
            //GlStateManager._rotatef(yaw - 270, 0, 1, 0);
            //GlStateManager._rotatef(pitch, 0, 0, 1);
            dispatchRenders(inv, player, IRenderBauble.RenderType.HEAD, partialTicks);
            //GlStateManager._popMatrix();
        });
    }
//TODO
    private void dispatchRenders(IBaublesItemHandler inv, Player player, IRenderBauble.RenderType type, float partialTicks) {
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                stack.getCapability(BaublesCapabilities.ITEM_BAUBLE).ifPresent(bauble -> {
                    if (bauble instanceof IRenderBauble) {
                        //GlStateManager._pushMatrix();
                        GL11.glColor3ub((byte) 255, (byte) 255, (byte) 255);
                        //GlStateManager._color4f(1F, 1F, 1F, 1F);
                        ((IRenderBauble) bauble).onPlayerBaubleRender(player, type, partialTicks);
                        //GlStateManager._popMatrix();
                    }
                });
            }
        }
    }
}