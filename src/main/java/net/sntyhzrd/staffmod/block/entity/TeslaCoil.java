package net.sntyhzrd.staffmod.block.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.sntyhzrd.staffmod.block.EnergyStorageBlock;
import net.sntyhzrd.staffmod.block.TeslaCoilBlock;
import net.sntyhzrd.staffmod.init.ProjectilesMI;
import net.sntyhzrd.staffmod.projectile.BallLightningProjectile;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.api.forge.BlockAPI;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.tiles.TileSyncableTickable;
import org.zeith.hammerlib.tiles.tooltip.EnumTooltipEngine;
import org.zeith.hammerlib.tiles.tooltip.ITooltipConsumer;
import org.zeith.hammerlib.tiles.tooltip.ITooltipTile;
import org.zeith.hammerlib.tiles.tooltip.own.ITooltip;
import org.zeith.hammerlib.tiles.tooltip.own.impl.WrappedTooltipEngine;
import org.zeith.hammerlib.util.mcf.NormalizedTicker;

import java.util.List;


@SimplyRegister
public class TeslaCoil extends TileSyncableTickable implements IEnergyStorage, ITooltipTile {
    @RegistryName("tesla_coil")
    public static final BlockEntityType<TeslaCoil> TESLA_COIL;

    public static int capacity = 256000;
    public static int maxReceive = 16000;
    public static int maxExtract = 16000;
    public static final Direction[] DIRECTIONS_NO_UP = Direction.stream().filter(f -> f != Direction.UP).toArray(Direction[]::new);

    public int energy;

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
    public void serverTick() {
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
                        // dist (1, 0] -> 16k FE
                        // dist (2, 1] -> 14k FE
                        // ...

                        int floor = (int) Math.floor(dist);
                        int generatedEnergy = ((16 - (2 * (floor - 1))) * 1000);

//                        System.out.println("DISTANCE: " + dist + ". CLOSEST FLOOR INT:" + floor + ". ENERGY OUT: " + generatedEnergy);
//                        Minecraft.getInstance().player.sendSystemMessage(Component.literal("DISTANCE: " + dist + ". CLOSEST FLOOR INT:" + floor + ". ENERGY OUT: " + generatedEnergy));
                        receiveEnergy(generatedEnergy, false);
                        System.out.println();

                        for(Direction hor : DIRECTIONS_NO_UP)
                        {
                            BlockEntity tile = level.getBlockEntity(worldPosition.relative(hor));
                            if(tile == null) continue;

                            tile.getCapability(ForgeCapabilities.ENERGY, hor.getOpposite()).ifPresent(storage ->
                            {
                                if(storage.canReceive())
                                    setEnergyStored(
                                            getEnergyStored() - storage.receiveEnergy(generatedEnergy, false)
                                    );
//                                    energy -= storage.receiveEnergy(Math.min(getEnergyStored(), maxExtract), false);
                            });
                        }
                    }
                }
            }
        }
    }

    static {
        TESLA_COIL = BlockAPI.createBlockEntityType(TeslaCoil::new, TeslaCoilBlock.TESLA_COIL);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!this.canReceive()) {
            return 0;
        } else {
            int energyReceived = Math.min(capacity - getEnergyStored(), Math.min(TeslaCoil.maxReceive, maxReceive));
            if (!simulate) {
                setEnergyStored(getEnergyStored() + energyReceived);
//                energy += energyReceived;
            }

            return energyReceived;
        }
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!this.canExtract()) {
            return 0;
        } else {
            int energyExtracted = Math.min(getEnergyStored(), Math.min(TeslaCoil.maxExtract, maxExtract));
            if (!simulate) {
                setEnergyStored(getEnergyStored() - energyExtracted);
//                energy -= energyExtracted;
            }

            return energyExtracted;
        }
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    public void setEnergyStored(int value) {
        energy = value;
        // это останется здесь на случай если мне снова надо будет переписывать взаимодействие с energy
        // я знаю что это убого
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return maxReceive > 0;
    }

    @Override
    public void addTooltip(ITooltipConsumer consumer, Player player) {
        consumer.addLine(Component.literal("Energy stored: " + getEnergyStored()));
    }

    @Override
    public boolean isEngineSupported(EnumTooltipEngine engine) {
        return ITooltipTile.super.isEngineSupported(engine);
    }

    LazyOptional<IEnergyStorage> energyStorageTile = LazyOptional.of(() -> TeslaCoil.this);

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, Direction side)
    {
        if(cap == ForgeCapabilities.ENERGY)
            return energyStorageTile.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
    }

    @Override
    public CompoundTag serializeNBT() {
        return super.serializeNBT();
    }
}
