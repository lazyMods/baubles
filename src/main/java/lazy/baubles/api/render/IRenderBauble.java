package lazy.baubles.api.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import lazy.baubles.api.bauble.IBauble;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.opengl.GL11;

/**
 * A Bauble Item that implements this will be have hooks to render something on
 * the player while its equipped.
 * This class doesn't extend IBauble to make the API not depend on the Baubles
 * API, but the item in question still needs to implement IBauble.
 */
public interface IRenderBauble extends IBauble {

    /**
     * Called for the rendering of the bauble on the player. The player instance can be
     * acquired through the event parameter. Transformations are already applied for
     * the RenderType passed in. Make sure to check against the type parameter for
     * rendering.
     */
    void onPlayerBaubleRender(PoseStack stack, Player player, RenderType type, float partialTicks);


    /**
     * A few helper methods for the render.
     */
    final class Helper {

        /**
         * Rotates the render for a bauble correctly if the player is sneaking.
         * Use for renders under {@link RenderType#BODY}.
         */
        public static void rotateIfSneaking(PoseStack stack, Player player) {
            if (player.isCrouching())
                applySneakingRotation(stack);
        }

        /**
         * Rotates the render for a bauble correctly for a sneaking player.
         * Use for renders under {@link RenderType#BODY}.
         */
        public static void applySneakingRotation(PoseStack stack) {
            stack.translate(0f, .2f, 0f);
            stack.mulPose(Vector3f.XP.rotationDegrees(90f / (float)Math.PI));
        }

        /**
         * Shifts the render for a bauble correctly to the head, including sneaking rotation.
         * Use for renders under {@link RenderType#HEAD}.
         */
        public static void translateToHeadLevel(PoseStack stack, Player player) {
            stack.translate(0, -player.getEyeHeight(), 0);
            if (player.isCrouching())
                stack.translate(0.25F * Mth.sin(player.yHeadRot * (float) Math.PI / 180), 0.25F * Mth.cos(player.yHeadRot * (float) Math.PI / 180), 0F);
        }

        /**
         * Shifts the render for a bauble correctly to the face.
         * Use for renders under {@link RenderType#HEAD}, and usually after calling {@link Helper#translateToHeadLevel(PoseStack, Player)}.
         */
        public static void translateToFace(PoseStack stack) {
            stack.mulPose(Vector3f.YP.rotationDegrees(90f));
            stack.mulPose(Vector3f.XP.rotationDegrees(180f));
            stack.translate(0f, -4.35f, -1.27f);
        }

        /**
         * Scales down the render to a correct size.
         * Use for any render.
         */
        public static void defaultTransforms(PoseStack stack) {
            stack.translate(0.0f, 3.0f, 1.0f);
            stack.scale(0.55f, 0.55f, 0.55f);
        }

        /**
         * Shifts the render for a bauble correctly to the chest.
         * Use for renders under {@link RenderType#BODY}, and usually after calling {@link Helper#rotateIfSneaking(PoseStack, Player)}.
         */
        public static void translateToChest(PoseStack stack) {
            stack.mulPose(Vector3f.XP.rotationDegrees(180f));
            stack.translate(0F, -3.2F, -0.85F);
        }
    }

    enum RenderType {
        /**
         * Render Type for the player's body, translations apply on the player's rotation.
         * Sneaking is not handled and should be done during the render.
         *
         * @see IRenderBauble.Helper
         */
        BODY,

        /**
         * Render Type for the player's body, translations apply on the player's head rotations.
         * Sneaking is not handled and should be done during the render.
         *
         * @see IRenderBauble.Helper
         */
        HEAD;
    }
}