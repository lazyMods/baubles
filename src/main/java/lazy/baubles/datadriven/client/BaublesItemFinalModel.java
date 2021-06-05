package lazy.baubles.datadriven.client;

import lazy.baubles.api.BaublesAPI;
import lazy.baubles.api.bauble.BaubleType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BaublesItemFinalModel implements IBakedModel {

    private final IBakedModel givenModel;
    private final BaubleType type;

    public BaublesItemFinalModel(IBakedModel givenModel, BaubleType type) {
        this.givenModel = givenModel;
        this.type = type;
    }

    public ResourceLocation getFromType(BaubleType type) {
        switch (type) {
            case RING:
                return new ResourceLocation(BaublesAPI.MOD_ID, "item/ring");
            case BELT:
                return new ResourceLocation(BaublesAPI.MOD_ID, "item/belt");
            case BODY:
                return new ResourceLocation(BaublesAPI.MOD_ID, "item/body");
            case HEAD:
                return new ResourceLocation(BaublesAPI.MOD_ID, "item/head");
            case CHARM:
                return new ResourceLocation(BaublesAPI.MOD_ID, "item/charm");
            case AMULET:
                return new ResourceLocation(BaublesAPI.MOD_ID, "item/amulet");
        }
        return new ResourceLocation(BaublesAPI.MOD_ID, "item/trinket");
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
        TextureAtlasSprite baubleSprite = Minecraft.getInstance().getTextureAtlas(AtlasTexture.LOCATION_BLOCKS).apply(this.getFromType(this.type));

        if (side != null) return this.givenModel.getQuads(state, side, rand);

        List<BakedQuad> quads = new ArrayList<>(this.givenModel.getQuads(state, side, rand));
        quads.addAll(ItemLayerModel.getQuadsForSprite(0, baubleSprite, TransformationMatrix.identity()));
        return quads;
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
        return false;
    }


    @Override
    public boolean isCustomRenderer() {
        return this.givenModel.isCustomRenderer();
    }


    @Override
    public ItemCameraTransforms getTransforms() {
        return this.givenModel.getTransforms();
    }


    @Override
    public TextureAtlasSprite getParticleIcon() {
        return ModelLoader.instance().getSpriteMap().getAtlas(AtlasTexture.LOCATION_BLOCKS).getSprite(new ResourceLocation("minecraft:diamond_block"));
    }

    @Override
    public ItemOverrideList getOverrides() {
        throw new UnsupportedOperationException("OH NO  OH NO OH NO NONONONONOO");
    }
}
