package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

/**
 * This class reprensents a part of the striped black-and-white blocks that are afraid of light in the original game.
 * <p>
 * Its texture can connect to other adjacent blocks of the same class.
 */
public class LivingBlock extends Block {
  public static final BooleanProperty NORTH = BooleanProperty.of("north");
  public static final BooleanProperty SOUTH = BooleanProperty.of("south");
  public static final BooleanProperty WEST = BooleanProperty.of("west");
  public static final BooleanProperty EAST = BooleanProperty.of("east");

  public LivingBlock() {
    super(ModBlock.getSettings(FabricBlockSettings.of(Material.METAL, MapColor.BLACK).sounds(BlockSoundGroup.METAL)));
    this.setDefaultState(this.getStateManager().getDefaultState()
        .with(NORTH, false)
        .with(SOUTH, false)
        .with(WEST, false)
        .with(EAST, false));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder.add(NORTH, SOUTH, WEST, EAST));
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext context) {
    World world = context.getWorld();
    BlockPos pos = context.getBlockPos();
    return this.getDefaultState()
        .with(NORTH, this.canConnectTo(world, pos, Direction.NORTH))
        .with(EAST, this.canConnectTo(world, pos, Direction.EAST))
        .with(SOUTH, this.canConnectTo(world, pos, Direction.SOUTH))
        .with(WEST, this.canConnectTo(world, pos, Direction.WEST));
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    if (direction.getAxis().isHorizontal()) {
      boolean canConnect = this.canConnectTo(world, pos, direction);
      BooleanProperty property = switch (direction) {
        case NORTH -> NORTH;
        case SOUTH -> SOUTH;
        case EAST -> EAST;
        case WEST -> WEST;
        default -> throw new IllegalArgumentException("invalid direction");
      };
      return state.with(property, canConnect);
    }
    return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
  }

  /**
   * Whether a block of this class should connect its textures to the block type at the given position.
   */
  public boolean canConnectTo(WorldAccess world, BlockPos pos, Direction facing) {
    return world.getBlockState(pos.offset(facing)).getBlock() instanceof LivingBlock;
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return switch (rotation) {
      case COUNTERCLOCKWISE_90 -> state
          .with(NORTH, state.get(EAST))
          .with(EAST, state.get(SOUTH))
          .with(SOUTH, state.get(WEST))
          .with(WEST, state.get(NORTH));
      case CLOCKWISE_90 -> state
          .with(NORTH, state.get(WEST))
          .with(EAST, state.get(NORTH))
          .with(SOUTH, state.get(EAST))
          .with(WEST, state.get(SOUTH));
      case CLOCKWISE_180 -> state
          .with(NORTH, state.get(SOUTH))
          .with(EAST, state.get(WEST))
          .with(SOUTH, state.get(NORTH))
          .with(WEST, state.get(EAST));
      default -> state;
    };
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return switch (mirror) {
      case LEFT_RIGHT -> state
          .with(NORTH, state.get(SOUTH))
          .with(SOUTH, state.get(NORTH));
      case FRONT_BACK -> state
          .with(EAST, state.get(WEST))
          .with(WEST, state.get(EAST));
      default -> state;
    };
  }
}
