package net.sntyhzrd.staffmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sntyhzrd.staffmod.StaffMod;
import net.sntyhzrd.staffmod.block.entity.TeslaCoil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.api.blocks.ICreativeTabBlock;
import org.zeith.hammerlib.api.forge.BlockAPI;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.core.adapter.BlockHarvestAdapter;

@SimplyRegister
public class TeslaCoilBlock extends BaseEntityBlock implements ICreativeTabBlock {
    @RegistryName("tesla_coil")
    public static final EnergyStorageBlock TESLA_COIL = new EnergyStorageBlock();

    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 32, 16);
    public TeslaCoilBlock() {
        super(Properties.of().requiresCorrectToolForDrops().sound(SoundType.METAL).strength(1.5F));
        BlockHarvestAdapter.bindTool(BlockHarvestAdapter.MineableType.PICKAXE, Tiers.IRON, new Block[]{this});
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return BlockAPI.ticker(level);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TeslaCoil(blockPos, blockState);
    }

    @Override
    public @NotNull CreativeTab getCreativeTab() {
        return StaffMod.MOD_TAB;
    }
}
