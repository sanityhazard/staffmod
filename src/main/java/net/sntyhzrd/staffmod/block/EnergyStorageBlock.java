package net.sntyhzrd.staffmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.energy.EnergyStorage;
import net.sntyhzrd.staffmod.StaffMod;
import net.sntyhzrd.staffmod.block.entity.CustomEnergyStorage;
import net.sntyhzrd.staffmod.block.entity.TeslaCoil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.api.blocks.ICreativeTabBlock;
import org.zeith.hammerlib.api.forge.BlockAPI;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.core.adapter.BlockHarvestAdapter;

import java.util.List;

@SimplyRegister
public class EnergyStorageBlock extends BaseEntityBlock implements ICreativeTabBlock {
//    public static final IntegerProperty ENERGY = IntegerProperty.create("storage_energy", 0, 16000);

    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);

//    @Override
//    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
//        pBuilder.add(ENERGY);
//    }

    public EnergyStorageBlock() {
        super(Properties.of().requiresCorrectToolForDrops().sound(SoundType.METAL).strength(1.5F));
        BlockHarvestAdapter.bindTool(BlockHarvestAdapter.MineableType.PICKAXE, Tiers.IRON, new Block[]{this});
    }

    @RegistryName("energy_storage")
    public static final EnergyStorageBlock ENERGY_STORAGE = new EnergyStorageBlock();

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
        return new CustomEnergyStorage(blockPos, defaultBlockState());
    }

    @Override
    public @NotNull CreativeTab getCreativeTab() {
        return StaffMod.MOD_TAB;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        if(stack.hasTag())
        {
            CustomEnergyStorage ces = null;
            BlockEntity tile = level.getBlockEntity(pos);
            if(tile instanceof CustomEnergyStorage)
                ces = (CustomEnergyStorage) tile;
            else
            {
                ces = (CustomEnergyStorage) newBlockEntity(pos, state);
                level.setBlockEntity(ces);
            }
            ces.loadFromItem(stack);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder)
    {
        NonNullList<ItemStack> stacks = NonNullList.create();

        BlockEntity tileentity = builder.getParameter(LootContextParams.BLOCK_ENTITY);
        if(tileentity instanceof CustomEnergyStorage te)
            stacks.add(te.generateItem(this));
        else
            stacks.add(new ItemStack(this));

        return stacks;
    }
}
