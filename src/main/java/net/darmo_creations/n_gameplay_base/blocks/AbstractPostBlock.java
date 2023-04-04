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
import net.minecraft.world.BlockView;

/**
 * Base class for posts.
 */
public abstract class AbstractPostBlock extends PillarBlock implements Colored, Waterloggable {
  public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

  private final BlockColor color;

  public AbstractPostBlock(final BlockColor color) {
    super(ModBlock.getSettings(FabricBlockSettings.of(Material.STONE, color.getMapColor()).sounds(BlockSoundGroup.STONE)));
    this.color = color;
    this.setDefaultState(this.getStateManager().getDefaultState()
        .with(AXIS, Direction.Axis.Y)
        .with(WATERLOGGED, false));
  }

  @Override
  public BlockColor getColor() {
    return this.color;
  }

  @Override
  public BlockState getBlockStateForColor(BlockState state, BlockColor color) {
    return this.getVariants().get(color).getDefaultState()
        .with(AXIS, state.get(AXIS))
        .with(WATERLOGGED, state.get(WATERLOGGED));
  }

  protected abstract ColoredBlockMap<? extends AbstractPostBlock> getVariants();

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder.add(WATERLOGGED));
  }

  @SuppressWarnings("deprecation")
  @Override
  public abstract VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context);

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
    return this.getDefaultState()
        .with(AXIS, ctx.getSide().getAxis())
        .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    if (state.get(WATERLOGGED)) {
      return Fluids.WATER.getStill(false);
    }
    return super.getFluidState(state);
  }
}
