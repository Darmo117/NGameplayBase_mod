package net.darmo_creations.n_gameplay_base.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

/**
 * A small 8x8x8 corner block.
 */
public class CornerBlock extends Block implements Waterloggable, ModBlock {
  public static final EnumProperty<VerticalPosition> VERTICAL_POSITION = EnumProperty.of("vertical_position", VerticalPosition.class);
  public static final EnumProperty<Position> POSITION = EnumProperty.of("position", Position.class);
  public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

  private static final VoxelShape NORTH_WEST_TOP_SHAPE = createCuboidShape(0, 8, 0, 8, 16, 8);
  private static final VoxelShape NORTH_WEST_MIDDLE_SHAPE = createCuboidShape(0, 4, 0, 8, 12, 8);
  private static final VoxelShape NORTH_WEST_BOTTOM_SHAPE = createCuboidShape(0, 0, 0, 8, 8, 8);

  private static final VoxelShape NORTH_TOP_SHAPE = createCuboidShape(4, 8, 0, 12, 16, 8);
  private static final VoxelShape NORTH_MIDDLE_SHAPE = createCuboidShape(4, 4, 0, 12, 12, 8);
  private static final VoxelShape NORTH_BOTTOM_SHAPE = createCuboidShape(4, 0, 0, 12, 8, 8);

  private static final VoxelShape NORTH_EAST_TOP_SHAPE = createCuboidShape(8, 8, 0, 16, 16, 8);
  private static final VoxelShape NORTH_EAST_MIDDLE_SHAPE = createCuboidShape(8, 4, 0, 16, 12, 8);
  private static final VoxelShape NORTH_EAST_BOTTOM_SHAPE = createCuboidShape(8, 0, 0, 16, 8, 8);

  private static final VoxelShape EAST_TOP_SHAPE = createCuboidShape(8, 8, 4, 16, 16, 12);
  private static final VoxelShape EAST_MIDDLE_SHAPE = createCuboidShape(8, 4, 4, 16, 12, 12);
  private static final VoxelShape EAST_BOTTOM_SHAPE = createCuboidShape(8, 0, 4, 16, 8, 12);

  private static final VoxelShape SOUTH_EAST_TOP_SHAPE = createCuboidShape(8, 8, 8, 16, 16, 16);
  private static final VoxelShape SOUTH_EAST_MIDDLE_SHAPE = createCuboidShape(8, 4, 8, 16, 12, 16);
  private static final VoxelShape SOUTH_EAST_BOTTOM_SHAPE = createCuboidShape(8, 0, 8, 16, 8, 16);

  private static final VoxelShape SOUTH_TOP_SHAPE = createCuboidShape(4, 8, 8, 12, 16, 16);
  private static final VoxelShape SOUTH_MIDDLE_SHAPE = createCuboidShape(4, 4, 8, 12, 12, 16);
  private static final VoxelShape SOUTH_BOTTOM_SHAPE = createCuboidShape(4, 0, 8, 12, 8, 16);

  private static final VoxelShape SOUTH_WEST_TOP_SHAPE = createCuboidShape(0, 8, 8, 8, 16, 16);
  private static final VoxelShape SOUTH_WEST_MIDDLE_SHAPE = createCuboidShape(0, 4, 8, 8, 12, 16);
  private static final VoxelShape SOUTH_WEST_BOTTOM_SHAPE = createCuboidShape(0, 0, 8, 8, 8, 16);

  private static final VoxelShape WEST_TOP_SHAPE = createCuboidShape(0, 8, 4, 8, 16, 12);
  private static final VoxelShape WEST_MIDDLE_SHAPE = createCuboidShape(0, 4, 4, 8, 12, 12);
  private static final VoxelShape WEST_BOTTOM_SHAPE = createCuboidShape(0, 0, 4, 8, 8, 12);

  private static final VoxelShape CENTER_TOP_SHAPE = createCuboidShape(4, 8, 4, 12, 16, 12);
  private static final VoxelShape CENTER_MIDDLE_SHAPE = createCuboidShape(4, 4, 4, 12, 12, 12);
  private static final VoxelShape CENTER_BOTTOM_SHAPE = createCuboidShape(4, 0, 4, 12, 8, 12);

  /**
   * Creates a corner block for the given color.
   *
   * @param settings Block’s settings.
   */
  public CornerBlock(Settings settings) {
    super(ModBlock.getSettings(settings));
    this.setDefaultState(this.getStateManager().getDefaultState()
        .with(VERTICAL_POSITION, VerticalPosition.BOTTOM)
        .with(POSITION, Position.NORTH)
        .with(WATERLOGGED, false));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder.add(VERTICAL_POSITION, POSITION, WATERLOGGED));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    final Position position = state.get(POSITION);
    return switch (state.get(VERTICAL_POSITION)) {
      case TOP -> switch (position) {
        case NORTH -> NORTH_TOP_SHAPE;
        case NORTH_WEST -> NORTH_WEST_TOP_SHAPE;
        case NORTH_EAST -> NORTH_EAST_TOP_SHAPE;
        case SOUTH -> SOUTH_TOP_SHAPE;
        case SOUTH_WEST -> SOUTH_WEST_TOP_SHAPE;
        case SOUTH_EAST -> SOUTH_EAST_TOP_SHAPE;
        case WEST -> WEST_TOP_SHAPE;
        case EAST -> EAST_TOP_SHAPE;
        case CENTER -> CENTER_TOP_SHAPE;
      };
      case MIDDLE -> switch (position) {
        case NORTH -> NORTH_MIDDLE_SHAPE;
        case NORTH_WEST -> NORTH_WEST_MIDDLE_SHAPE;
        case NORTH_EAST -> NORTH_EAST_MIDDLE_SHAPE;
        case SOUTH -> SOUTH_MIDDLE_SHAPE;
        case SOUTH_WEST -> SOUTH_WEST_MIDDLE_SHAPE;
        case SOUTH_EAST -> SOUTH_EAST_MIDDLE_SHAPE;
        case WEST -> WEST_MIDDLE_SHAPE;
        case EAST -> EAST_MIDDLE_SHAPE;
        case CENTER -> CENTER_MIDDLE_SHAPE;
      };
      case BOTTOM -> switch (position) {
        case NORTH -> NORTH_BOTTOM_SHAPE;
        case NORTH_WEST -> NORTH_WEST_BOTTOM_SHAPE;
        case NORTH_EAST -> NORTH_EAST_BOTTOM_SHAPE;
        case SOUTH -> SOUTH_BOTTOM_SHAPE;
        case SOUTH_WEST -> SOUTH_WEST_BOTTOM_SHAPE;
        case SOUTH_EAST -> SOUTH_EAST_BOTTOM_SHAPE;
        case WEST -> WEST_BOTTOM_SHAPE;
        case EAST -> EAST_BOTTOM_SHAPE;
        case CENTER -> CENTER_BOTTOM_SHAPE;
      };
    };
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    BlockPos pos = ctx.getBlockPos();
    double xHit = ctx.getHitPos().getX() - pos.getX();
    double yHit = ctx.getHitPos().getY() - pos.getY();
    double zHit = ctx.getHitPos().getZ() - pos.getZ();
    BlockState state = this.getDefaultState();
    if (yHit > 0.67) {
      state = state.with(VERTICAL_POSITION, VerticalPosition.TOP);
    } else if (yHit < 0.33) {
      state = state.with(VERTICAL_POSITION, VerticalPosition.BOTTOM);
    } else {
      state = state.with(VERTICAL_POSITION, VerticalPosition.MIDDLE);
    }
    if (zHit < 0.33) {
      if (xHit < 0.33) {
        state = state.with(POSITION, Position.NORTH_WEST);
      } else if (xHit > 0.67) {
        state = state.with(POSITION, Position.NORTH_EAST);
      } else {
        state = state.with(POSITION, Position.NORTH);
      }
    } else if (zHit > 0.67) {
      if (xHit < 0.33) {
        state = state.with(POSITION, Position.SOUTH_WEST);
      } else if (xHit > 0.67) {
        state = state.with(POSITION, Position.SOUTH_EAST);
      } else {
        state = state.with(POSITION, Position.SOUTH);
      }
    } else {
      if (xHit < 0.33) {
        state = state.with(POSITION, Position.WEST);
      } else if (xHit > 0.67) {
        state = state.with(POSITION, Position.EAST);
      } else {
        state = state.with(POSITION, Position.CENTER);
      }
    }
    FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
    return state.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    if (state.get(WATERLOGGED)) {
      return Fluids.WATER.getStill(false);
    }
    return super.getFluidState(state);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return state.with(POSITION, state.get(POSITION).rotate(rotation));
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return state.with(POSITION, state.get(POSITION).mirror(mirror));
  }

  public enum Position implements StringIdentifiable {
    NORTH("north"),
    NORTH_WEST("north_west"),
    NORTH_EAST("north_east"),
    SOUTH("south"),
    SOUTH_WEST("south_west"),
    SOUTH_EAST("south_east"),
    WEST("west"),
    EAST("east"),
    CENTER("center");

    private final String name;

    Position(String name) {
      this.name = name;
    }

    /**
     * Applies the given rotation to this position.
     *
     * @param rotation Rotation to apply.
     * @return Resulting position.
     */
    public Position rotate(BlockRotation rotation) {
      return switch (rotation) {
        case NONE -> this;
        case CLOCKWISE_90 -> switch (this) {
          case NORTH -> EAST;
          case NORTH_EAST -> SOUTH_EAST;
          case NORTH_WEST -> NORTH_EAST;
          case SOUTH -> WEST;
          case SOUTH_EAST -> SOUTH_WEST;
          case SOUTH_WEST -> NORTH_WEST;
          case WEST -> NORTH;
          case EAST -> SOUTH;
          default -> this;
        };
        case CLOCKWISE_180 -> switch (this) {
          case NORTH -> SOUTH;
          case NORTH_EAST -> SOUTH_WEST;
          case NORTH_WEST -> SOUTH_EAST;
          case SOUTH -> NORTH;
          case SOUTH_EAST -> NORTH_WEST;
          case SOUTH_WEST -> NORTH_EAST;
          case WEST -> EAST;
          case EAST -> WEST;
          default -> this;
        };
        case COUNTERCLOCKWISE_90 -> switch (this) {
          case NORTH -> WEST;
          case NORTH_EAST -> NORTH_WEST;
          case NORTH_WEST -> SOUTH_WEST;
          case SOUTH -> EAST;
          case SOUTH_EAST -> NORTH_EAST;
          case SOUTH_WEST -> SOUTH_EAST;
          case WEST -> SOUTH;
          case EAST -> NORTH;
          default -> this;
        };
      };
    }

    /**
     * Applies the given mirror transformation to this position.
     *
     * @param mirror Mirror transformation to apply.
     * @return Resulting position.
     */
    public Position mirror(BlockMirror mirror) {
      return switch (mirror) {
        case NONE -> this;
        case LEFT_RIGHT -> switch (this) {
          case NORTH -> SOUTH;
          case NORTH_EAST -> SOUTH_EAST;
          case NORTH_WEST -> SOUTH_WEST;
          case SOUTH -> NORTH;
          case SOUTH_EAST -> NORTH_EAST;
          case SOUTH_WEST -> NORTH_WEST;
          default -> this;
        };
        case FRONT_BACK -> switch (this) {
          case WEST -> EAST;
          case NORTH_WEST -> NORTH_EAST;
          case SOUTH_WEST -> SOUTH_EAST;
          case EAST -> WEST;
          case NORTH_EAST -> NORTH_WEST;
          case SOUTH_EAST -> SOUTH_WEST;
          default -> this;
        };
      };
    }

    @Override
    public String asString() {
      return this.name;
    }

    @Override
    public String toString() {
      return this.asString();
    }
  }
}
