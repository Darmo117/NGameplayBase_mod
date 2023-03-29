package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

/**
 * A small flat light block offset by 0.5 blocks.
 */
public class SmallOffsetLightBlock extends WaterloggableWallMountedBlock {
  public static final IntProperty LIGHT_LEVEL = IntProperty.of("light_level", 0, 15);

  public static final VoxelShape WALL_NORTH_SHAPE = createCuboidShape(-4, 4, 0, 4, 12, 2);
  public static final VoxelShape WALL_WEST_SHAPE = createCuboidShape(0, 4, 12, 2, 12, 20);
  public static final VoxelShape WALL_SOUTH_SHAPE = createCuboidShape(12, 4, 14, 20, 12, 16);
  public static final VoxelShape WALL_EAST_SHAPE = createCuboidShape(14, 4, -4, 16, 12, 4);

  public static final VoxelShape CEILING_NORTH_SHAPE = createCuboidShape(4, 14, -4, 12, 16, 4);
  public static final VoxelShape CEILING_SOUTH_SHAPE = createCuboidShape(4, 14, 12, 12, 16, 20);
  public static final VoxelShape CEILING_WEST_SHAPE = createCuboidShape(-4, 14, 4, 4, 16, 12);
  public static final VoxelShape CEILING_EAST_SHAPE = createCuboidShape(12, 14, 4, 20, 16, 12);

  public static final VoxelShape FLOOR_NORTH_SHAPE = createCuboidShape(4, 0, -4, 12, 2, 4);
  public static final VoxelShape FLOOR_SOUTH_SHAPE = createCuboidShape(4, 0, 12, 12, 2, 20);
  public static final VoxelShape FLOOR_WEST_SHAPE = createCuboidShape(-4, 0, 4, 4, 2, 12);
  public static final VoxelShape FLOOR_EAST_SHAPE = createCuboidShape(12, 0, 4, 20, 2, 12);

  public SmallOffsetLightBlock() {
    super(FabricBlockSettings.of(Material.STONE, MapColor.WHITE)
        .sounds(BlockSoundGroup.STONE)
        .luminance(state -> state.get(LIGHT_LEVEL)));
    this.setDefaultState(this.getDefaultState().with(LIGHT_LEVEL, 15));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder.add(LIGHT_LEVEL));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    final Direction facing = state.get(FACING);
    return switch (state.get(FACE)) {
      case WALL -> switch (facing) {
        case NORTH -> WALL_NORTH_SHAPE;
        case SOUTH -> WALL_SOUTH_SHAPE;
        case WEST -> WALL_WEST_SHAPE;
        case EAST -> WALL_EAST_SHAPE;
        default -> VoxelShapes.empty();
      };
      case FLOOR -> switch (facing) {
        case NORTH -> FLOOR_NORTH_SHAPE;
        case SOUTH -> FLOOR_SOUTH_SHAPE;
        case WEST -> FLOOR_WEST_SHAPE;
        case EAST -> FLOOR_EAST_SHAPE;
        default -> VoxelShapes.empty();
      };
      case CEILING -> switch (facing) {
        case NORTH -> CEILING_NORTH_SHAPE;
        case SOUTH -> CEILING_SOUTH_SHAPE;
        case WEST -> CEILING_WEST_SHAPE;
        case EAST -> CEILING_EAST_SHAPE;
        default -> VoxelShapes.empty();
      };
    };
  }
}
