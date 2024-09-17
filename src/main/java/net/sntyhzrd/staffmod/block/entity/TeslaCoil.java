package net.sntyhzrd.staffmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.sntyhzrd.staffmod.block.EnergyStorageBlock;
import net.sntyhzrd.staffmod.block.TeslaCoilBlock;
import net.sntyhzrd.staffmod.init.ProjectilesMI;
import net.sntyhzrd.staffmod.projectile.BallLightningProjectile;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.api.forge.BlockAPI;
import org.zeith.hammerlib.tiles.TileSyncableTickable;

import java.util.List;


@SimplyRegister
public class TeslaCoil extends TileSyncableTickable implements IEnergyStorage {
    @RegistryName("tesla_coil")
    public static final BlockEntityType<TeslaCoil> TESLA_COIL;

    protected int energy = 0;
    protected int capacity = 16000;
    protected int maxReceive = 16000;
    protected int maxExtract = 16000;
    private static final Direction[] DIRECTIONS_NO_UP = Direction.stream().filter(f -> f != Direction.UP).toArray(Direction[]::new);
//    private IntegerProperty transfer = IntegerProperty.create("transfer", 0, this.maxExtract);
    protected int transfer = 0;

    public TeslaCoil(BlockPos pos, BlockState state) {
        super(TESLA_COIL, pos, state);
    }

    private double getDistanceToEntity(Entity entity) {
        double deltaX = entity.getX() - this.getBlockPos().getX();
        double deltaY = entity.getY() - this.getBlockPos().getY();
        double deltaZ = entity.getZ() - this.getBlockPos().getZ();

        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
    }

    @Override
    public void update() {
        if (!level.isClientSide()) {
            double x = this.getBlockPos().getX();
            double y = this.getBlockPos().getY();
            double z = this.getBlockPos().getZ();

            AABB scanAbove = new AABB(x, y, z, x + 8, y + 8, z + 8);
            List<Entity> entities = level.getEntities(new BallLightningProjectile(ProjectilesMI.BALL_LIGHTNING, level), scanAbove);

            if (!entities.isEmpty()) {
                for (int i = 0; i < entities.toArray().length; i++) {
                    Entity entity = entities.get(i);

                    // For some reason ServerPlayer can be in the entity list
                    if (entity.getType() == ProjectilesMI.BALL_LIGHTNING) {
                        double dist = getDistanceToEntity(entity);
                        // dist (1, 0] -> 16k FE (0, 1, 0.7)
                        // dist (2, 1] -> 14k FE (1, 2, 1.5)
                        // ...

                        int floor = (int) Math.floor(dist);
                        int generatedEnergy = (16 - (2 * (floor - 1))) * 1000;

                        System.out.println("DISTANCE: " + dist + ". CLOSEST FLOOR INT:" + floor + ". ENERGY OUT: " + generatedEnergy);

                        receiveEnergy(generatedEnergy, false);

                        for(Direction hor : DIRECTIONS_NO_UP)
                        {
                            BlockEntity tile = level.getBlockEntity(worldPosition.relative(hor));
                            if(tile == null) continue;

                            tile.getCapability(ForgeCapabilities.ENERGY, hor.getOpposite()).ifPresent(storage ->
                            {
                                if(storage.canReceive())
                                    energy -= storage.receiveEnergy(Math.min(getEnergyStored(), maxExtract), false);
                            });
                        }
                    }
                }
            }
        }
    }

    static {
        TESLA_COIL = BlockAPI.createBlockEntityType(TeslaCoil::new, new Block[]{TeslaCoilBlock.TESLA_COIL});
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!this.canReceive()) {
            return 0;
        } else {
            int energyReceived = Math.min(this.capacity - this.energy, Math.min(this.maxReceive, maxReceive));
            if (!simulate) {
                this.energy += energyReceived;
            }

            return energyReceived;
        }
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!this.canExtract()) {
            return 0;
        } else {
            int energyExtracted = Math.min(this.energy, Math.min(this.maxExtract, maxExtract));
            if (!simulate) {
                this.energy -= energyExtracted;
            }

            return energyExtracted;
        }
    }

    @Override
    public int getEnergyStored() {
        return this.energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return this.capacity;
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0;
    }
}
