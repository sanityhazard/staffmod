package net.sntyhzrd.staffmod.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.renderer.entity.*;
import net.sntyhzrd.staffmod.StaffMod;
import net.sntyhzrd.staffmod.init.GeometriesMI;
import net.sntyhzrd.staffmod.init.ProjectilesMI;
import org.zeith.hammeranims.api.HammerAnimationsApi;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.api.geometry.event.RefreshStaleModelsEvent;
import org.zeith.hammeranims.api.geometry.model.IGeometricModel;
import org.zeith.hammeranims.api.geometry.model.RenderData;
import org.zeith.hammeranims.core.client.render.entity.BedrockEntityRenderer;
import org.zeith.hammeranims.core.client.render.entity.RenderEntityBilly;

public class BallLightningProjectileRenderer extends EntityRenderer<BallLightningProjectile> {
    IGeometricModel ballLightningProjectileModel;

    final ResourceLocation texture = new ResourceLocation(StaffMod.MOD_ID, "textures/projectile/ball_lightning_texture.png");
    final RenderData data;

    public BallLightningProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        data = new RenderData();
        HammerAnimationsApi.EVENT_BUS.addListener(this::refreshModel);
    }

    public void refreshModel(RefreshStaleModelsEvent e)
    {
        ballLightningProjectileModel = GeometriesMI.BALL_LIGHTNING_PROJECTILE.createModel();
//        AnimationSystem system =
    }

    @Override
    public void render(BallLightningProjectile pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        ballLightningProjectileModel.resetPose();
        ballLightningProjectileModel.renderModel(data.apply(pPoseStack, pBuffer.getBuffer(RenderType.entityCutout(texture)), pPackedLight, OverlayTexture.NO_OVERLAY));
    }

    @Override
    public ResourceLocation getTextureLocation(BallLightningProjectile p_114482_) {
        return texture;
    }


}