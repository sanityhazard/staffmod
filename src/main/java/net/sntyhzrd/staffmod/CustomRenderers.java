package net.sntyhzrd.staffmod;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.sntyhzrd.staffmod.init.ProjectilesMI;
import net.sntyhzrd.staffmod.projectile.BallLightningProjectileRenderer;

public class CustomRenderers {
    public static void renderEntity() {
        EntityRenderers.register(ProjectilesMI.BALL_LIGHTNING, BallLightningProjectileRenderer::new);
    }
}
