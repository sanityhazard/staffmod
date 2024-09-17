package net.sntyhzrd.staffmod.init;

import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammerlib.annotations.*;

@SimplyRegister
public interface GeometriesMI {
    @RegistryName("ball_lightning")
    IGeometryContainer BALL_LIGHTNING_PROJECTILE = IGeometryContainer.create();

    @RegistryName("tesla_coil")
    IGeometryContainer TESLA_COIL = IGeometryContainer.create();
}
