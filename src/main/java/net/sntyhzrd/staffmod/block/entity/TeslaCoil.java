package net.sntyhzrd.staffmod.block.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.animal.Pig;
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
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sntyhzrd.staffmod.StaffMod;
//import net.sntyhzrd.staffmod.block.EnergyStorageBlock;
import net.sntyhzrd.staffmod.block.TeslaCoilBlock;
import net.sntyhzrd.staffmod.init.ProjectilesMI;
import net.sntyhzrd.staffmod.projectile.BallLightningProjectile;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.api.forge.BlockAPI;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.core.test.machine.TileTestMachine;
import org.zeith.hammerlib.net.properties.PropertyInt;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.tiles.TileSyncableTickable;
import org.zeith.hammerlib.tiles.tooltip.EnumTooltipEngine;
import org.zeith.hammerlib.tiles.tooltip.ITooltipConsumer;
import org.zeith.hammerlib.tiles.tooltip.ITooltipTile;
import org.zeith.hammerlib.tiles.tooltip.own.ITooltip;
import org.zeith.hammerlib.tiles.tooltip.own.ITooltipProvider;
import org.zeith.hammerlib.tiles.tooltip.own.impl.WrappedTooltipEngine;
import org.zeith.hammerlib.util.java.DirectStorage;
import org.zeith.hammerlib.util.mcf.NormalizedTicker;

import java.util.List;

@SimplyRegister
public class TeslaCoil extends TileSyncableTickable implements IEnergyStorage, ITooltipProvider {
    @RegistryName("tesla_coil")
    public static final BlockEntityType<TeslaCoil> TESLA_COIL;
    @NBTSerializable
    private int _energy;
    public static int capacity = 256000;
    public static int maxReceive = Integer.MAX_VALUE;
    public static int maxExtract = 16000;
    public static final Direction[] DIRECTIONS_NO_UP = Direction.stream().filter(f -> f != Direction.UP).toArray(Direction[]::new);
    public final PropertyInt energy = new PropertyInt(DirectStorage.create((i) -> this._energy = i, () -> this._energy));

    public boolean dirty;

    public TeslaCoil(BlockPos pos, BlockState state) {
        super(TESLA_COIL, pos, state);
        this.dispatcher.registerProperty("energy", this.energy);
    }

    @Override
    public void clientTick() {
        this.setTooltipDirty(true);
    }

    @Override
    public void serverTick() {
        double x = this.getBlockPos().getX();
        double y = this.getBlockPos().getY();
        double z = this.getBlockPos().getZ();

        AABB scanAbove = new AABB(x - 8, y - 8, z - 8, x + 8, y + 8, z + 8);
        var entities = level.getEntitiesOfClass(LightningBolt.class, scanAbove);


        for (LightningBolt e : entities) {
            if (!e.getTags().contains("processed")) {
                var distance = e.position().distanceTo(getBlockPos().getCenter());
                int newEnergy = (int) (16000 * (1 - distance / 8));
                receiveEnergy(newEnergy, false);

                e.addTag("processed");
            }
        }

        for(Direction dir : Direction.values())
        {
            BlockEntity tile = level.getBlockEntity(worldPosition.relative(dir));
            if(tile == null) continue;

            tile.getCapability(ForgeCapabilities.ENERGY, dir.getOpposite()).ifPresent(storage ->
            {
                if(storage.canReceive()) {
                    int storageMaxEnergyStored = storage.getMaxEnergyStored(); // put them here for easy debug
                    int energyStored = getEnergyStored(); // зейт не ешь меня
                    setEnergyStored(
                            getEnergyStored() - storage.receiveEnergy(Math.min(Math.min(maxExtract, getEnergyStored()), storage.getMaxEnergyStored() - storage.getEnergyStored()), false)
                    );
                }
            });
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
        return energy.getInt();
    }

    public void setEnergyStored(int value) {
        energy.setInt(value);
        // это останется здесь на случай если мне снова надо будет переписывать взаимодействие с energy
        // я знаю что это убого
        // upd А Я ЗНАЛ ЧТО ПРИДЕТСЯ
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

    @Override
    public boolean isTooltipDirty() {
        return this.dirty;
    }

    @Override
    public void setTooltipDirty(boolean b) {
        this.dirty = b;
    }

    @Override
    public void addInformation(ITooltip iTooltip) {
        iTooltip.addText(Component.literal("Energy: " + getEnergyStored()));
    }
}
