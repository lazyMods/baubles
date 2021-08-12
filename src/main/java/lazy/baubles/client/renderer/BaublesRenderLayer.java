package lazy.baubles.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import lazy.baubles.api.bauble.IBaublesItemHandler;
import lazy.baubles.api.cap.CapabilityBaubles;
import lazy.baubles.api.render.IRenderBauble;
import lazy.baubles.setup.ModConfigs;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

@SuppressWarnings("ConstantConditions")
public class BaublesRenderLayer<T extends Player, M extends PlayerModel<T>> extends RenderLayer<T, M> {

    public BaublesRenderLayer(RenderLayerParent<T, M> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource iRenderTypeBuffer, int i, Player player, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

        if (!ModConfigs.RENDER_BAUBLE.get() || player.getEffect(MobEffects.INVISIBILITY) != null)
            return;

        player.getCapability(CapabilityBaubles.BAUBLES).ifPresent(inv -> {

            this.dispatchRenders(matrixStack, inv, player, IRenderBauble.RenderType.BODY, partialTicks);

            float yaw = player.yHeadRotO + (player.yHeadRot - player.yHeadRotO) * partialTicks;
            float yawOffset = player.yBodyRotO + (player.yBodyRot - player.yBodyRotO) * partialTicks;
            float xRot = ObfuscationReflectionHelper.getPrivateValue(Entity.class, player, "f_19858_");
            float pitch = player.xRotO + (xRot - player.xRotO) * partialTicks;

            matrixStack.pushPose();
            matrixStack.mulPose(Vector3f.YN.rotationDegrees(yawOffset));
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(yaw - 270));
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(pitch));
            this.dispatchRenders(matrixStack, inv, player, IRenderBauble.RenderType.HEAD, partialTicks);
            matrixStack.popPose();
        });
    }

    private void dispatchRenders(PoseStack poseStack, IBaublesItemHandler inv, Player player, IRenderBauble.RenderType type, float partialTicks) {
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                stack.getCapability(CapabilityBaubles.ITEM_BAUBLE).ifPresent(bauble -> {
                    if (bauble instanceof IRenderBauble rb) {
                        poseStack.pushPose();
                        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                        rb.onPlayerBaubleRender(poseStack, player, type, partialTicks);
                        poseStack.popPose();
                    }
                });
            }
        }
    }
}