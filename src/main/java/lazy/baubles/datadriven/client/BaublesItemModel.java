package lazy.baubles.datadriven.client;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class BaublesItemModel implements BakedModel {

    private final BakedModel givenModel;
    private final BaublesItemOverridesList baublesItemOverridesList;
    public static final ModelResourceLocation INVENTORY_MODEL = new ModelResourceLocation("baubles:bauble", "inventory");

    public BaublesItemModel(BakedModel givenModel) {
        this.givenModel = givenModel;
        this.baublesItemOverridesList = new BaublesItemOverridesList();
    }

    @Override
    @Nonnull
    public List<BakedQuad> getQuads(BlockState state, Direction side, @Nonnull Random rand) {
        return this.givenModel.getQuads(state, side, rand);
    }

    @Override
    @Nonnull
    public List<BakedQuad> getQuads(BlockState state, Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        throw new AssertionError("OH NO  OH NO OH NO NONONONONOO");
    }

    @Override
    @Nonnull
    public IModelData getModelData(@Nonnull BlockAndTintGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData) {
        throw new AssertionError("OH NO  OH NO OH NO NONONONONOO");
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.givenModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.givenModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return this.givenModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return this.givenModel.isCustomRenderer();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public TextureAtlasSprite getParticleIcon() {
        return null;
    }

    @Override
    @Nonnull
    public ItemOverrides getOverrides() {
        return this.baublesItemOverridesList;
    }
}
