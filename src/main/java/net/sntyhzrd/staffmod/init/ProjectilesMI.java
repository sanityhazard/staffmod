package net.sntyhzrd.staffmod.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.sntyhzrd.staffmod.projectile.BallLightningProjectile;
import org.zeith.hammeranims.core.client.render.entity.RenderEntityBilly;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface ProjectilesMI {
    @RegistryName("ball_lightning")
    EntityType<BallLightningProjectile> BALL_LIGHTNING =
            EntityType.Builder.of(BallLightningProjectile::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .updateInterval(1)
                    .fireImmune()
                    .build("ball_lightning");

//    @RegistryName("ball_lightning_type")
//    EntityType<BallLightningProjectile> BALL_LIGHTNING_TYPE = EntityType.Builder.of(BallLightningProjectile::new,
//            MobCategory.MISC).sized(1F, 1.0F).build("ball_lightning_type");
}
