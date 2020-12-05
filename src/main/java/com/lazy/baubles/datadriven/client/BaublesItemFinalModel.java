package com.lazy.baubles.datadriven.client;

import com.lazy.baubles.api.BaublesAPI;
import com.lazy.baubles.api.bauble.BaubleType;
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
        TextureAtlasSprite baubleSprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(this.getFromType(this.type));

        if (side != null) return this.givenModel.getQuads(state, side, rand);

        List<BakedQuad> quads = new ArrayList<>(this.givenModel.getQuads(state, side, rand));
        quads.addAll(ItemLayerModel.getQuadsForSprite(0, baubleSprite, TransformationMatrix.identity()));
        return quads;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return this.givenModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.givenModel.isGui3d();
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return this.givenModel.isBuiltInRenderer();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return this.givenModel.getItemCameraTransforms();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return ModelLoader.instance().getSpriteMap().getAtlasTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).getSprite(new ResourceLocation("minecraft:diamond_block"));
    }

    @Override
    public ItemOverrideList getOverrides() {
        throw new UnsupportedOperationException("OH NO  OH NO OH NO NONONONONOO");
    }
}
