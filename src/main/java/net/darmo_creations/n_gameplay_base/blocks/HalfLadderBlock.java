package net.darmo_creations.n_gameplay_base.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

/**
 * One half of a ladder that does not disappear if the block it is placed against is removed.
 * Meant to be used as a way to center a ladder between two blocks.
 */
public class HalfLadderBlock extends LadderBlockWithoutSupport implements Colored {
  public static final EnumProperty<Side> SIDE = EnumProperty.of("side", Side.class);

  protected static final VoxelShape NORTH_LEFT_AABB = createCuboidShape(8, 0, 13, 16, 16, 16);
  protected static final VoxelShape SOUTH_LEFT_AABB = createCuboidShape(0, 0, 0, 8, 16, 3);
  protected static final VoxelShape WEST_LEFT_AABB = createCuboidShape(13, 0, 0, 16, 16, 8);
  protected static final VoxelShape EAST_LEFT_AABB = createCuboidShape(0, 0, 8, 3, 16, 16);

  protected static final VoxelShape NORTH_RIGHT_AABB = createCuboidShape(0, 0, 13, 8, 16, 16);
  protected static final VoxelShape SOUTH_RIGHT_AABB = createCuboidShape(8, 0, 0, 16, 16, 3);
  protected static final VoxelShape WEST_RIGHT_AABB = createCuboidShape(13, 0, 8, 16, 16, 16);
  protected static final VoxelShape EAST_RIGHT_AABB = createCuboidShape(0, 0, 0, 3, 16, 8);

  /**
   * Creates a half ladder with the given color.
   *
   * @param color Ladder’s color.
   */
  public HalfLadderBlock(final BlockColor color) {
    super(color);
    // Override default state defined in BlockLadder
    this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(SIDE, Side.LEFT));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder.add(SIDE));
  }

  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    boolean left = state.get(SIDE) == Side.LEFT;
    return switch (state.get(FACING)) {
      case NORTH -> left ? NORTH_LEFT_AABB : NORTH_RIGHT_AABB;
      case SOUTH -> left ? SOUTH_LEFT_AABB : SOUTH_RIGHT_AABB;
      case WEST -> left ? WEST_LEFT_AABB : WEST_RIGHT_AABB;
      case EAST -> left ? EAST_LEFT_AABB : EAST_RIGHT_AABB;
      default -> throw new IllegalStateException("invalid facing");
    };
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext context) {
    BlockState state = super.getPlacementState(context);
    if (state == null) {
      return null;
    }

    Direction face = state.get(FACING);
    Vec3d hitPos = context.getHitPos();
    BlockPos blockPos = context.getBlockPos();
    double xHit = hitPos.x - blockPos.getX();
    double zHit = hitPos.z - blockPos.getZ();
    Side side;

    if (face == Direction.NORTH && xHit < 0.5
        || face == Direction.SOUTH && xHit > 0.5
        || face == Direction.EAST && zHit < 0.5
        || face == Direction.WEST && zHit > 0.5) {
      side = Side.RIGHT;
    } else {
      side = Side.LEFT;
    }

    return state.with(SIDE, side);
  }

  /**
   * Enumeration defining the two possible sides a half ladder can be on a block’s side.
   */
  public enum Side implements StringIdentifiable {
    LEFT,
    RIGHT;

    @Override
    public String asString() {
      return this == LEFT ? "left" : "right";
    }

    @Override
    public String toString() {
      return this.asString();
    }
  }
}
