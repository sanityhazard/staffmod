package net.sntyhzrd.staffmod.init;

import org.zeith.hammeranims.api.animation.IAnimationContainer;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface AnimationsMI {
    @RegistryName("your_animation")
    IAnimationContainer BALL_LIGHTNING_PROJECTILE_ANIMATION = IAnimationContainer.create();
}
