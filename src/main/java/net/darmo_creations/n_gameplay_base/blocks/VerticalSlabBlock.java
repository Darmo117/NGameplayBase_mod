package net.darmo_creations.n_gameplay_base.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

/**
 * Base class for vertical slabs.
 * Vertical slabs are waterloggable.
 */
public class VerticalSlabBlock extends Block implements Waterloggable, ModBlock {
  public static final EnumProperty<VerticalSlabType> TYPE = EnumProperty.of("type", VerticalSlabType.class);
  public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

  protected static final VoxelShape NORTH_SHAPE = createCuboidShape(0, 0, 0, 16, 16, 8);
  protected static final VoxelShape SOUTH_SHAPE = createCuboidShape(0, 0, 8, 16, 16, 16);
  protected static final VoxelShape WEST_SHAPE = createCuboidShape(0, 0, 0, 8, 16, 16);
  protected static final VoxelShape EAST_SHAPE = createCuboidShape(8, 0, 0, 16, 16, 16);

  public VerticalSlabBlock(Settings settings) {
    super(ModBlock.getSettings(settings));
    this.setDefaultState(this.getStateManager().getDefaultState()
        .with(TYPE, VerticalSlabType.NORTH)
        .with(WATERLOGGED, false));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder.add(TYPE, WATERLOGGED));
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean hasSidedTransparency(BlockState state) {
    return state.get(TYPE) != VerticalSlabType.DOUBLE;
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return switch (state.get(TYPE)) {
      case NORTH -> NORTH_SHAPE;
      case SOUTH -> SOUTH_SHAPE;
      case EAST -> EAST_SHAPE;
      case WEST -> WEST_SHAPE;
      case DOUBLE -> VoxelShapes.fullCube();
    };
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    BlockPos blockPos = ctx.getBlockPos();
    BlockState blockState = ctx.getWorld().getBlockState(blockPos);
    if (blockState.isOf(this)) {
      return blockState.with(TYPE, VerticalSlabType.DOUBLE).with(WATERLOGGED, false);
    }
    FluidState fluidState = ctx.getWorld().getFluidState(blockPos);
    return this.getDefaultState()
        .with(TYPE, VerticalSlabType.forDirection(ctx.getPlayerFacing()))
        .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean canReplace(BlockState state, ItemPlacementContext context) {
    ItemStack itemStack = context.getStack();
    VerticalSlabType slabType = state.get(TYPE);
    if (slabType == VerticalSlabType.DOUBLE || !itemStack.isOf(this.asItem())) {
      return false;
    }
    if (context.canReplaceExisting()) {
      Direction side = context.getSide();
      if (side.getAxis() == Direction.Axis.Y) {
        Vec3d hitPos = context.getHitPos();
        BlockPos blockPos = context.getBlockPos();
        double xHit = hitPos.x - blockPos.getX();
        double zHit = hitPos.z - blockPos.getZ();
        return switch (slabType) {
          case NORTH -> zHit > 0.5;
          case SOUTH -> zHit < 0.5;
          case WEST -> xHit > 0.5;
          case EAST -> xHit < 0.5;
          default -> false;
        };
      }
      return slabType.getDirection() == side.getOpposite();
    }
    return true;
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    if (state.get(WATERLOGGED)) {
      return Fluids.WATER.getStill(false);
    }
    return super.getFluidState(state);
  }

  @Override
  public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
    if (state.get(TYPE) != VerticalSlabType.DOUBLE) {
      return Waterloggable.super.tryFillWithFluid(world, pos, state, fluidState);
    }
    return false;
  }

  @Override
  public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
    if (state.get(TYPE) != VerticalSlabType.DOUBLE) {
      return Waterloggable.super.canFillWithFluid(world, pos, state, fluid);
    }
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    if (state.get(WATERLOGGED)) {
      world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }
    return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
    return type == NavigationType.WATER && world.getFluidState(pos).isIn(FluidTags.WATER);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return state.with(TYPE, state.get(TYPE).rotate(rotation));
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return state.with(TYPE, state.get(TYPE).mirror(mirror));
  }
}
