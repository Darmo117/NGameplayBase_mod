package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

/**
 * A small flat light block.
 */
public class SmallLightBlock extends FacingBlock implements ModBlock, Waterloggable {
  public static final IntProperty LIGHT_LEVEL = IntProperty.of("light_level", 0, 15);
  public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

  public static final VoxelShape NORTH_SHAPE = createCuboidShape(4, 4, 0, 12, 12, 2);
  public static final VoxelShape WEST_SHAPE = createCuboidShape(0, 4, 4, 2, 12, 12);
  public static final VoxelShape SOUTH_SHAPE = createCuboidShape(4, 4, 14, 12, 12, 16);
  public static final VoxelShape EAST_SHAPE = createCuboidShape(14, 4, 4, 16, 12, 12);
  public static final VoxelShape UP_SHAPE = createCuboidShape(4, 14, 4, 12, 16, 12);
  public static final VoxelShape DOWN_SHAPE = createCuboidShape(4, 0, 4, 12, 2, 12);

  public SmallLightBlock() {
    super(ModBlock.getSettings(FabricBlockSettings.of(Material.STONE, MapColor.WHITE)
        .sounds(BlockSoundGroup.STONE)
        .luminance(state -> state.get(LIGHT_LEVEL))));
    this.setDefaultState(this.getStateManager().getDefaultState()
        .with(FACING, Direction.NORTH)
        .with(LIGHT_LEVEL, 15)
        .with(WATERLOGGED, false));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder.add(FACING, LIGHT_LEVEL, WATERLOGGED));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return switch (state.get(FACING)) {
      case NORTH -> NORTH_SHAPE;
      case SOUTH -> SOUTH_SHAPE;
      case WEST -> WEST_SHAPE;
      case EAST -> EAST_SHAPE;
      case UP -> UP_SHAPE;
      case DOWN -> DOWN_SHAPE;
    };
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
    return this.getDefaultState()
        .with(FACING, ctx.getSide().getOpposite())
        .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
  }
}
