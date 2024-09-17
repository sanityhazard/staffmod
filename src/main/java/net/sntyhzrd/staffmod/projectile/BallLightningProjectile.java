package net.sntyhzrd.staffmod.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.api.tile.IAnimatedEntity;
import org.zeith.hammerlib.api.fluid.FluidFactory;

public class BallLightningProjectile extends LargeFireball implements IAnimatedEntity {
    protected final AnimationSystem ANIMATIONS = AnimationSystem.create(this);

    public BallLightningProjectile(EntityType<? extends LargeFireball> p_37199_, Level p_37200_) {
        super(p_37199_, p_37200_);
    }

    @Override
    protected void onHit(HitResult p_37218_) {
        super.onHit(p_37218_);
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            super.tick();
            ANIMATIONS.tick();

            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                if (!this.level().isClientSide()) {
                    EntityType.LIGHTNING_BOLT.spawn((ServerLevel) this.level(), this.blockPosition(), MobSpawnType.COMMAND);
                }
                this.onHit(hitresult);
            }

            this.checkInsideBlocks();
            Vec3 vec3 = this.getDeltaMovement();
            double speed = vec3.length();

            ProjectileUtil.rotateTowardsMovement(this, 0.2F);

            if (speed < 0.5) {
                this.setDeltaMovement(vec3.normalize().scale(0.5));
            }

        } else {
            this.discard();
        }

    }

    @Override
    public void setupSystem(AnimationSystem.Builder builder) {

    }

    @Override
    public AnimationSystem getAnimationSystem() {
        return ANIMATIONS;
    }
}
