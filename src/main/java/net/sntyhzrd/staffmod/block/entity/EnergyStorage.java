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
import net.sntyhzrd.staffmod.init.ProjectilesMI;
import net.sntyhzrd.staffmod.projectile.BallLightningProjectile;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.api.forge.BlockAPI;
import org.zeith.hammerlib.tiles.TileSyncableTickable;

import java.util.List;


@SimplyRegister
public class EnergyStorage extends TileSyncableTickable implements IEnergyStorage {
    @RegistryName("energy_storage")
    public static final BlockEntityType<EnergyStorage> ENERGY_STORAGE;

    protected int energy = 0;
    protected int capacity = 320000;
    protected int maxReceive = 16000;
    protected int maxExtract = 16000;

    public EnergyStorage(BlockPos pos, BlockState state) {
        super(ENERGY_STORAGE, pos, state);
    }

    static {
        ENERGY_STORAGE = BlockAPI.createBlockEntityType(EnergyStorage::new, new Block[]{EnergyStorageBlock.ENERGY_STORAGE});
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
