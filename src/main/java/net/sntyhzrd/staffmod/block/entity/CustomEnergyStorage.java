//package net.sntyhzrd.staffmod.block.entity;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.ItemLike;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.properties.IntegerProperty;
//import net.minecraft.world.phys.AABB;
//import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.capabilities.ForgeCapabilities;
//import net.minecraftforge.common.util.LazyOptional;
//import net.minecraftforge.energy.IEnergyStorage;
//import net.sntyhzrd.staffmod.block.EnergyStorageBlock;
//import net.sntyhzrd.staffmod.block.TeslaCoilBlock;
//import net.sntyhzrd.staffmod.init.ProjectilesMI;
//import net.sntyhzrd.staffmod.projectile.BallLightningProjectile;
//import org.jetbrains.annotations.NotNull;
//import org.zeith.hammerlib.annotations.RegistryName;
//import org.zeith.hammerlib.annotations.SimplyRegister;
//import org.zeith.hammerlib.api.forge.BlockAPI;
//import org.zeith.hammerlib.api.io.NBTSerializable;
//import org.zeith.hammerlib.tiles.TileSyncableTickable;
//import org.zeith.hammerlib.tiles.tooltip.EnumTooltipEngine;
//import org.zeith.hammerlib.tiles.tooltip.ITooltipConsumer;
//import org.zeith.hammerlib.tiles.tooltip.ITooltipTile;
//import org.zeith.hammerlib.tiles.tooltip.own.ITooltip;
//import org.zeith.hammerlib.tiles.tooltip.own.ITooltipProvider;
//
//import java.util.List;
//
//
//@SimplyRegister
//public class CustomEnergyStorage extends TileSyncableTickable implements IEnergyStorage, ITooltipProvider {
//    @RegistryName("custom_energy_storage")
//    public static final BlockEntityType<CustomEnergyStorage> CUSTOM_ENERGY_STORAGE;
//
//    public static int capacity = 1024000;
//    public static int maxExtract = 16000;
//
//    public int energy;
//    public boolean dirty;
//
//    public CustomEnergyStorage(BlockPos pos, BlockState state) {
//        super(CUSTOM_ENERGY_STORAGE, pos, state);
//    }
//
//    @Override
//    public void update() {
//        super.update();
//    }
//
//    @Override
//    public void clientTick() {
//        this.setTooltipDirty(true);
//    }
//
//    static {
//        CUSTOM_ENERGY_STORAGE = BlockAPI.createBlockEntityType(CustomEnergyStorage::new, EnergyStorageBlock.ENERGY_STORAGE);
//    }
//
//    @Override
//    public int receiveEnergy(int maxReceive, boolean simulate) {
//        if (!this.canReceive()) {
//            return 0;
//        } else {
//            int energyReceived = Math.min(Math.min(capacity - energy, Integer.MAX_VALUE), maxReceive);
//            if (!simulate) {
////                int energyReceived = Math.min(Math.min(capacity - energy, Integer.MAX_VALUE), maxReceive);
//                setEnergyStored(getEnergyStored() + energyReceived);
////                return energyReceived;
//            }
//            return energyReceived;
//        }
//    }
//
//    @Override
//    public int extractEnergy(int maxExtract, boolean simulate) {
//        if (!this.canExtract()) {
//            return 0;
//        } else {
//            int energyExtracted = Math.min(getEnergyStored(), Math.min(CustomEnergyStorage.maxExtract, maxExtract));
//            if (!simulate) {
//                setEnergyStored(getEnergyStored() - energyExtracted);
//            }
//
//            return energyExtracted;
//        }
//    }
//
//    @Override
//    public int getEnergyStored() {
//        return energy;
//    }
//
//    public void setEnergyStored(int value) {
//        energy = value;
//        // это останется здесь на случай если мне снова надо будет переписывать взаимодействие с energy
//        // я знаю что это убого
//    }
//    @Override
//    public int getMaxEnergyStored() {
//        return capacity;
//    }
//
//    @Override
//    public boolean canExtract() {
//        return maxExtract > 0;
//    }
//
//    @Override
//    public boolean canReceive() {
//        return true;
//    }
//
//    @Override
//    public boolean isTooltipDirty() {
//        return this.dirty;
//    }
//
//    @Override
//    public void setTooltipDirty(boolean b) {
//        this.dirty = b;
//    }
//
//    @Override
//    public void addInformation(ITooltip iTooltip) {
//        iTooltip.addText(Component.literal("Energy: " + energy));
//    }
//
//    LazyOptional<IEnergyStorage> customEnergyStorageTile = LazyOptional.of(() -> CustomEnergyStorage.this);
//
//    @Override
//    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, Direction side)
//    {
//        if(cap == ForgeCapabilities.ENERGY)
//            return customEnergyStorageTile.cast();
//        return super.getCapability(cap, side);
//    }
//
//    @Override
//    public CompoundTag writeNBT(CompoundTag nbt)
//    {
//        nbt.putInt("Energy", energy);
//        return nbt;
//    }
//
//    @Override
//    public void readNBT(CompoundTag nbt)
//    {
//        energy = nbt.getInt("Energy");
//    }
//
//    public ItemStack generateItem(ItemLike item)
//    {
//        ItemStack stack = new ItemStack(item);
//        CompoundTag tag = new CompoundTag();
//        tag.putInt("Energy", energy);
//        stack.setTag(tag);
//        return stack;
//    }
//
//    public void loadFromItem(ItemStack stack)
//    {
//        if(stack.hasTag())
//        {
//            energy = stack.getTag().getInt("Energy");
//        }
//    }
//
//}
