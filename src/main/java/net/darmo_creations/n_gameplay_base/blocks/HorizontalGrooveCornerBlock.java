package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.function.Function;

/**
 * A decorative block representing a sort of groove oriented along the x or z axis.
 */
public class HorizontalGrooveCornerBlock extends WaterloggableHorizontalFacingBlock implements Colored {
  public static final EnumProperty<BlockHalf> HALF = Properties.BLOCK_HALF;

  private static final VoxelShape NORTH_WEST_BOTTOM_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 0, 0, 4, 8, 4),
      createCuboidShape(0, 1, 4, 5, 3, 8),
      createCuboidShape(4, 1, 0, 8, 3, 5),
      createCuboidShape(5, 1, 5, 7, 3, 7),
      createCuboidShape(0, 5, 4, 5, 7, 8),
      createCuboidShape(4, 5, 0, 8, 7, 5),
      createCuboidShape(5, 5, 5, 7, 7, 7));
  private static final VoxelShape NORTH_WEST_TOP_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 8, 0, 4, 16, 4),
      createCuboidShape(0, 9, 4, 5, 11, 8),
      createCuboidShape(4, 9, 0, 8, 11, 5),
      createCuboidShape(5, 9, 5, 7, 11, 7),
      createCuboidShape(0, 13, 4, 5, 15, 8),
      createCuboidShape(4, 13, 0, 8, 15, 5),
      createCuboidShape(5, 13, 5, 7, 15, 7));

  private static final VoxelShape NORTH_EAST_BOTTOM_SHAPE = VoxelShapes.union(
      createCuboidShape(12, 0, 0, 16, 8, 4),
      createCuboidShape(11, 1, 4, 16, 3, 8),
      createCuboidShape(8, 1, 0, 12, 3, 5),
      createCuboidShape(9, 1, 5, 11, 3, 7),
      createCuboidShape(11, 5, 4, 16, 7, 8),
      createCuboidShape(8, 5, 0, 12, 7, 5),
      createCuboidShape(9, 5, 5, 11, 7, 7));
  private static final VoxelShape NORTH_EAST_TOP_SHAPE = VoxelShapes.union(
      createCuboidShape(12, 8, 0, 16, 16, 4),
      createCuboidShape(11, 9, 4, 16, 11, 8),
      createCuboidShape(8, 9, 0, 12, 11, 5),
      createCuboidShape(9, 9, 5, 11, 11, 7),
      createCuboidShape(11, 13, 4, 16, 15, 8),
      createCuboidShape(8, 13, 0, 12, 15, 5),
      createCuboidShape(9, 13, 5, 11, 15, 7));

  private static final VoxelShape SOUTH_EAST_BOTTOM_SHAPE = VoxelShapes.union(
      createCuboidShape(12, 0, 12, 16, 8, 16),
      createCuboidShape(8, 1, 11, 12, 3, 16),
      createCuboidShape(11, 1, 8, 16, 3, 12),
      createCuboidShape(9, 1, 9, 11, 3, 11),
      createCuboidShape(8, 5, 11, 12, 7, 16),
      createCuboidShape(11, 5, 8, 16, 7, 12),
      createCuboidShape(9, 5, 9, 11, 7, 11));
  private static final VoxelShape SOUTH_EAST_TOP_SHAPE = VoxelShapes.union(
      createCuboidShape(12, 8, 12, 16, 16, 16),
      createCuboidShape(8, 9, 11, 12, 11, 16),
      createCuboidShape(11, 9, 8, 16, 11, 12),
      createCuboidShape(9, 9, 9, 11, 11, 11),
      createCuboidShape(8, 13, 11, 12, 15, 16),
      createCuboidShape(11, 13, 8, 16, 15, 12),
      createCuboidShape(9, 13, 9, 11, 15, 11));

  private static final VoxelShape SOUTH_WEST_BOTTOM_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 0, 12, 4, 8, 16),
      createCuboidShape(4, 1, 11, 8, 3, 16),
      createCuboidShape(0, 1, 8, 5, 3, 12),
      createCuboidShape(5, 1, 9, 7, 3, 11),
      createCuboidShape(4, 5, 11, 8, 7, 16),
      createCuboidShape(0, 5, 8, 5, 7, 12),
      createCuboidShape(5, 5, 9, 7, 7, 11));
  private static final VoxelShape SOUTH_WEST_TOP_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 8, 12, 4, 16, 16),
      createCuboidShape(4, 9, 11, 8, 11, 16),
      createCuboidShape(0, 9, 8, 5, 11, 12),
      createCuboidShape(5, 9, 9, 7, 11, 11),
      createCuboidShape(4, 13, 11, 8, 15, 16),
      createCuboidShape(0, 13, 8, 5, 15, 12),
      createCuboidShape(5, 13, 9, 7, 15, 11));

  private final BlockColor color;

  public HorizontalGrooveCornerBlock(final BlockColor color) {
    super(FabricBlockSettings.of(Material.STONE, color.getMapColor()).sounds(BlockSoundGroup.STONE),
        shapeProvider(NORTH_WEST_BOTTOM_SHAPE, NORTH_WEST_TOP_SHAPE),
        shapeProvider(NORTH_EAST_BOTTOM_SHAPE, NORTH_EAST_TOP_SHAPE),
        shapeProvider(SOUTH_EAST_BOTTOM_SHAPE, SOUTH_EAST_TOP_SHAPE),
        shapeProvider(SOUTH_WEST_BOTTOM_SHAPE, SOUTH_WEST_TOP_SHAPE));
    this.color = color;
    this.setDefaultState(this.getDefaultState().with(HALF, BlockHalf.BOTTOM));
  }

  private static Function<BlockState, VoxelShape> shapeProvider(final VoxelShape bottomShape, final VoxelShape topShape) {
    return state -> switch (state.get(HALF)) {
      case BOTTOM -> bottomShape;
      case TOP -> topShape;
    };
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder.add(HALF));
  }

  @Override
  public BlockColor getColor() {
    return this.color;
  }

  @Override
  public BlockState getBlockStateForColor(BlockState state, BlockColor color) {
    return ModBlocks.HORIZONTAL_GROOVE_CORNER_BLOCKS.get(color).getDefaultState()
        .with(HALF, state.get(HALF))
        .with(FACING, state.get(FACING))
        .with(WATERLOGGED, state.get(WATERLOGGED));
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    BlockState state = super.getPlacementState(ctx);
    double yHit = ctx.getHitPos().y - ctx.getBlockPos().getY();
    //noinspection ConstantConditions
    return state.with(HALF, yHit < 0.5 ? BlockHalf.BOTTOM : BlockHalf.TOP);
  }
}
