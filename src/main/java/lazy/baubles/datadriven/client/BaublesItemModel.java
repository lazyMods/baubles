package lazy.baubles.datadriven.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class BaublesItemModel implements IBakedModel {

    private final IBakedModel givenModel;
    private final BaublesItemOverridesList baublesItemOverridesList;
    public static final ModelResourceLocation INVENTORY_MODEL = new ModelResourceLocation("baubles:bauble", "inventory");

    public BaublesItemModel(IBakedModel givenModel) {
        this.givenModel = givenModel;
        this.baublesItemOverridesList = new BaublesItemOverridesList();
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
        return this.givenModel.getQuads(state, side, rand);
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData extraData) {
        throw new AssertionError("OH NO  OH NO OH NO NONONONONOO");
    }

    @Override
    public IModelData getModelData(IBlockDisplayReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData) {
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

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return null;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return this.baublesItemOverridesList;
    }
}
