package com.burntrouter.atr.fluid;

import com.burntrouter.atr.registry.ATRBlocks;
import com.burntrouter.atr.registry.ATRFluids;
import com.burntrouter.atr.registry.ATRItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public abstract class JuiceFluid extends FlowableFluid {
    public JuiceFluid() {
    }

    public Fluid getFlowing() {
        return ATRFluids.JUICE_FLOWING;
    }

    public Fluid getStill() {
        return ATRFluids.JUICE_STILL;
    }

    public Item getBucketItem() {
        return ATRItems.JUICE_BUCKET;
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
        if (!state.isStill() && !(Boolean) state.get(FALLING)) {
            if (random.nextInt(64) == 0) {
                world.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false);
            }
        } else if (random.nextInt(10) == 0) {
            world.addParticle(ParticleTypes.UNDERWATER, (double) pos.getX() + random.nextDouble(), (double) pos.getY() + random.nextDouble(), (double) pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
        }

    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public ParticleEffect getParticle() {
        return ParticleTypes.DRIPPING_WATER;
    }

    protected boolean isInfinite() {
        return true;
    }

    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.getBlock().hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropStacks(state, world, pos, blockEntity);
    }

    public int getFlowSpeed(WorldView world) {
        return 4;
    }

    public BlockState toBlockState(FluidState state) {
        return (BlockState) ATRBlocks.JUICE.getDefaultState().with(FluidBlock.LEVEL, method_15741(state));
    }

    public boolean matchesType(Fluid fluid) {
        return fluid == ATRFluids.JUICE_STILL || fluid == ATRFluids.JUICE_FLOWING;
    }

    public int getLevelDecreasePerBlock(WorldView world) {
        return 1;
    }

    public int getTickRate(WorldView world) {
        return 5;
    }

    public boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
        //return direction == Direction.DOWN && !fluid.isIn(FluidTags.WATER);
        return false;
    }

    protected float getBlastResistance() {
        return 100.0F;
    }

    public static class Flowing extends JuiceFluid {
        public Flowing() {
        }

        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(new Property[]{LEVEL});
        }

        public int getLevel(FluidState state) {
            return (Integer) state.get(LEVEL);
        }

        public boolean isStill(FluidState state) {
            return false;
        }
    }

    public static class Still extends JuiceFluid {
        public Still() {
        }

        public int getLevel(FluidState state) {
            return 8;
        }

        public boolean isStill(FluidState state) {
            return true;
        }
    }
}
