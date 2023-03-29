package net.darmo_creations.n_gameplay_base.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

/**
 * This class represent a single wall post.
 */
public class ThickPostBlock extends AbstractPostBlock implements Colored {
  private static final VoxelShape SHAPE_X = createCuboidShape(0, 4, 4, 16, 12, 12);
  private static final VoxelShape SHAPE_Y = createCuboidShape(4, 0, 4, 12, 16, 12);
  private static final VoxelShape SHAPE_Z = createCuboidShape(4, 4, 0, 12, 12, 16);

  public ThickPostBlock(final BlockColor color) {
    super(color);
  }

  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return switch (state.get(AXIS)) {
      case X -> SHAPE_X;
      case Y -> SHAPE_Y;
      case Z -> SHAPE_Z;
    };
  }
}
