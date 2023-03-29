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
 * This class represents a vertical quarter of a block.
 */
public class VerticalQuarterBlock extends Block implements Waterloggable, ModBlock {
  public static final EnumProperty<Position> POSITION = EnumProperty.of("position", Position.class);
  public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

  private static final VoxelShape NORTH_WEST = createCuboidShape(0, 0, 0, 8, 16, 8);
  private static final VoxelShape NORTH = createCuboidShape(4, 0, 0, 12, 16, 8);
  private static final VoxelShape NORTH_EAST = createCuboidShape(8, 0, 0, 16, 16, 8);

  private static final VoxelShape SOUTH_EAST = createCuboidShape(8, 0, 8, 16, 16, 16);
  private static final VoxelShape SOUTH = createCuboidShape(4, 0, 8, 12, 16, 16);
  private static final VoxelShape SOUTH_WEST = createCuboidShape(0, 0, 8, 8, 16, 16);

  private static final VoxelShape WEST = createCuboidShape(0, 0, 4, 8, 16, 12);

  private static final VoxelShape EAST = createCuboidShape(8, 0, 4, 16, 16, 12);

  public VerticalQuarterBlock(Settings settings) {
    super(ModBlock.getSettings(settings));
    this.setDefaultState(this.getStateManager().getDefaultState()
        .with(POSITION, Position.NORTH)
        .with(WATERLOGGED, false));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder.add(POSITION, WATERLOGGED));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return switch (state.get(POSITION)) {
      case NORTH_WEST -> NORTH_WEST;
      case NORTH -> NORTH;
      case NORTH_EAST -> NORTH_EAST;
      case EAST -> EAST;
      case SOUTH_EAST -> SOUTH_EAST;
      case SOUTH -> SOUTH;
      case SOUTH_WEST -> SOUTH_WEST;
      case WEST -> WEST;
    };
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    double hitX = ctx.getHitPos().x - ctx.getBlockPos().getX();
    double hitZ = ctx.getHitPos().z - ctx.getBlockPos().getZ();
    Position position;
    //noinspection ConstantConditions
    switch (ctx.getPlayer().getHorizontalFacing()) {
      case NORTH -> {
        if (hitX < 0.33) {
          position = Position.NORTH_WEST;
        } else if (hitX > 0.67) {
          position = Position.NORTH_EAST;
        } else {
          position = Position.NORTH;
        }
      }
      case SOUTH -> {
        if (hitX < 0.33) {
          position = Position.SOUTH_WEST;
        } else if (hitX > 0.67) {
          position = Position.SOUTH_EAST;
        } else {
          position = Position.SOUTH;
        }
      }
      case WEST -> {
        if (hitZ < 0.33) {
          position = Position.NORTH_WEST;
        } else if (hitZ > 0.67) {
          position = Position.SOUTH_WEST;
        } else {
          position = Position.WEST;
        }
      }
      case EAST -> {
        if (hitZ < 0.33) {
          position = Position.NORTH_EAST;
        } else if (hitZ > 0.67) {
          position = Position.SOUTH_EAST;
        } else {
          position = Position.EAST;
        }
      }
      default -> throw new IllegalArgumentException("invalid player horizontal facing");
    }
    FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
    return this.getDefaultState()
        .with(POSITION, position)
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
    NORTH_WEST("north_west"),
    NORTH("north"),
    NORTH_EAST("north_east"),
    EAST("east"),
    SOUTH_EAST("south_east"),
    SOUTH("south"),
    SOUTH_WEST("south_west"),
    WEST("west");

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
          case NORTH_WEST -> NORTH_EAST;
          case NORTH -> EAST;
          case NORTH_EAST -> SOUTH_EAST;
          case EAST -> SOUTH;
          case SOUTH_EAST -> SOUTH_WEST;
          case SOUTH -> WEST;
          case SOUTH_WEST -> NORTH_WEST;
          case WEST -> NORTH;
        };
        case CLOCKWISE_180 -> switch (this) {
          case NORTH_WEST -> SOUTH_EAST;
          case NORTH -> SOUTH;
          case NORTH_EAST -> SOUTH_WEST;
          case EAST -> WEST;
          case SOUTH_EAST -> NORTH_WEST;
          case SOUTH -> NORTH;
          case SOUTH_WEST -> NORTH_EAST;
          case WEST -> EAST;
        };
        case COUNTERCLOCKWISE_90 -> switch (this) {
          case NORTH_WEST -> SOUTH_WEST;
          case NORTH -> WEST;
          case NORTH_EAST -> NORTH_WEST;
          case EAST -> NORTH;
          case SOUTH_EAST -> NORTH_EAST;
          case SOUTH -> EAST;
          case SOUTH_WEST -> SOUTH_EAST;
          case WEST -> SOUTH;
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
          case NORTH_WEST -> SOUTH_WEST;
          case NORTH -> SOUTH;
          case NORTH_EAST -> SOUTH_EAST;
          case EAST -> EAST;
          case SOUTH_EAST -> NORTH_EAST;
          case SOUTH -> NORTH;
          case SOUTH_WEST -> NORTH_WEST;
          case WEST -> WEST;
        };
        case FRONT_BACK -> switch (this) {
          case NORTH_WEST -> NORTH_EAST;
          case NORTH -> NORTH;
          case NORTH_EAST -> NORTH_WEST;
          case EAST -> WEST;
          case SOUTH_EAST -> SOUTH_WEST;
          case SOUTH -> SOUTH;
          case SOUTH_WEST -> SOUTH_EAST;
          case WEST -> EAST;
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
