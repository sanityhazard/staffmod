package net.sntyhzrd.staffmod.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sntyhzrd.staffmod.init.ProjectilesMI;
import net.sntyhzrd.staffmod.projectile.BallLightningProjectile;

import java.util.function.Predicate;

public class ElectricStaffItem extends Item {

    public ElectricStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (!level.isClientSide()) {
            player.getMainHandItem().hurtAndBreak(1, player, e -> e.broadcastBreakEvent(e.getUsedItemHand()));
            player.getCooldowns().addCooldown(this, 200);
            Vec3 lookAngle = player.getViewVector(1f);
            Vec3 eyePos = player.position().add(lookAngle.scale(2.0)); // 2 blocks in front of the player
            Vec3 motion = lookAngle.scale(1.5); // Adjust speed of the fireball
            BallLightningProjectile fireball = new BallLightningProjectile(ProjectilesMI.BALL_LIGHTNING, level);
            fireball.setPos(eyePos.x, eyePos.y + 1, eyePos.z);
            fireball.setDeltaMovement(motion.x, motion.y, motion.z);
            level.addFreshEntity(fireball);
        }

        return super.use(level, player, interactionHand);
    }
}
