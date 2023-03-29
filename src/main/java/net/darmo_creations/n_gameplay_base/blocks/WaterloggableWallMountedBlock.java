package net.darmo_creations.n_gameplay_base.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

/**
 * A waterloggable wall-mounted block.
 */
public class WaterloggableWallMountedBlock extends HorizontalFacingBlock implements ModBlock, Waterloggable {
  public static final EnumProperty<WallMountLocation> FACE = Properties.WALL_MOUNT_LOCATION;
  public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

  public WaterloggableWallMountedBlock(Settings settings) {
    super(ModBlock.getSettings(settings));
    this.setDefaultState(this.getStateManager().getDefaultState()
        .with(FACING, Direction.NORTH)
        .with(FACE, WallMountLocation.WALL)
        .with(WATERLOGGED, false));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder.add(FACING, FACE, WATERLOGGED));
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    Direction facing = ctx.getSide();
    BlockState state = this.getDefaultState();
    if (facing.getAxis() != Direction.Axis.Y) {
      state = state
          .with(FACING, facing.getOpposite())
          .with(FACE, WallMountLocation.WALL);
    } else {
      state = state
          .with(FACING, ctx.getPlayerFacing())
          .with(FACE, facing == Direction.DOWN ? WallMountLocation.CEILING : WallMountLocation.FLOOR);
    }
    FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
    return state.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    if (state.get(WATERLOGGED)) {
      world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }
    return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
  }
}
