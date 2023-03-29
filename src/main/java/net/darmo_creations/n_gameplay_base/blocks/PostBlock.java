package net.darmo_creations.n_gameplay_base.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

/**
 * This class represents a single fence post.
 */
public class PostBlock extends AbstractPostBlock implements Colored {
  private static final VoxelShape SHAPE_X = createCuboidShape(0, 6, 6, 16, 10, 10);
  private static final VoxelShape SHAPE_Y = createCuboidShape(6, 0, 6, 10, 16, 10);
  private static final VoxelShape SHAPE_Z = createCuboidShape(6, 6, 0, 10, 10, 16);

  public PostBlock(final BlockColor color) {
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
