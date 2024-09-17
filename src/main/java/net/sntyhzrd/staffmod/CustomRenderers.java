package net.sntyhzrd.staffmod;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.sntyhzrd.staffmod.init.BlocksMI;
import net.sntyhzrd.staffmod.init.ProjectilesMI;
import net.sntyhzrd.staffmod.projectile.BallLightningProjectileRenderer;
import org.zeith.hammerlib.annotations.client.TileRenderer;

public class CustomRenderers {
    public static void renderEntity() {
        EntityRenderers.register(ProjectilesMI.BALL_LIGHTNING, BallLightningProjectileRenderer::new);
    }
}
