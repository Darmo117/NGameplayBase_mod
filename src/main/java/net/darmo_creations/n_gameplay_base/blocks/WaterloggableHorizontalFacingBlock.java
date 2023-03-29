package net.darmo_creations.n_gameplay_base.blocks;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * A decorative block representing a sort of groove oriented along the y axis.
 */
public class WaterloggableHorizontalFacingBlock extends HorizontalFacingBlock implements ModBlock, Waterloggable {
  public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

  private final Map<Direction, Function<BlockState, VoxelShape>> shapes;

  public WaterloggableHorizontalFacingBlock(Settings settings, final VoxelShape northShape, final VoxelShape eastShape,
                                            final VoxelShape southShape, final VoxelShape westShape) {
    this(settings, state -> northShape, state -> eastShape, state -> southShape, state -> westShape);
  }

  public WaterloggableHorizontalFacingBlock(
      Settings settings,
      final Function<BlockState, VoxelShape> northShape,
      final Function<BlockState, VoxelShape> eastShape,
      final Function<BlockState, VoxelShape> southShape,
      final Function<BlockState, VoxelShape> westShape
  ) {
    super(ModBlock.getSettings(settings));
    this.shapes = new EnumMap<>(Direction.class);
    this.shapes.put(Direction.NORTH, Objects.requireNonNull(northShape));
    this.shapes.put(Direction.EAST, Objects.requireNonNull(eastShape));
    this.shapes.put(Direction.SOUTH, Objects.requireNonNull(southShape));
    this.shapes.put(Direction.WEST, Objects.requireNonNull(westShape));
    this.setDefaultState(this.getStateManager().getDefaultState()
        .with(FACING, Direction.NORTH)
        .with(WATERLOGGED, false));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder.add(FACING, WATERLOGGED));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    Direction facing = state.get(FACING);
    if (facing.getAxis().isHorizontal()) {
      return this.shapes.get(facing).apply(state);
    }
    return VoxelShapes.empty();
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
    return this.getDefaultState()
        .with(FACING, ctx.getPlayerFacing())
        .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
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
