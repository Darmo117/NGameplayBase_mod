package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class SmallLightsBlock extends FacingBlock implements ModBlock, Waterloggable {
  public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

  private static final VoxelShape AABB_NORTH = createCuboidShape(0, 0, 15, 16, 16, 16);
  private static final VoxelShape AABB_SOUTH = createCuboidShape(0, 0, 0, 16, 16, 1);
  private static final VoxelShape AABB_WEST = createCuboidShape(15, 0, 0, 16, 16, 16);
  private static final VoxelShape AABB_EAST = createCuboidShape(0, 0, 0, 1, 16, 16);
  private static final VoxelShape AABB_UP = createCuboidShape(0, 0, 0, 16, 1, 16);
  private static final VoxelShape AABB_DOWN = createCuboidShape(0, 15, 0, 16, 16, 16);

  public SmallLightsBlock() {
    super(ModBlock.getSettings(FabricBlockSettings.of(Material.STONE, MapColor.WHITE)
        .sounds(BlockSoundGroup.STONE)
        .luminance(5)
        .nonOpaque()));
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
  public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return VoxelShapes.empty();
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return switch (state.get(FACING)) {
      case NORTH -> AABB_NORTH;
      case SOUTH -> AABB_SOUTH;
      case WEST -> AABB_WEST;
      case EAST -> AABB_EAST;
      case UP -> AABB_UP;
      case DOWN -> AABB_DOWN;
    };
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
    return this.getDefaultState()
        .with(FACING, ctx.getSide())
        .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
  }
}
