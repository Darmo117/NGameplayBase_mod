package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

/**
 * A block representing a pair of “pipes“ oriented along the x or z axis.
 */
public class HorizontalPipesBlock extends WaterloggableWallMountedBlock implements Colored {
  private static final VoxelShape NORTH_WALL_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 2, 0, 16, 6, 8),
      createCuboidShape(0, 10, 0, 16, 14, 8));
  private static final VoxelShape SOUTH_WALL_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 2, 8, 16, 6, 16),
      createCuboidShape(0, 10, 8, 16, 14, 16));
  private static final VoxelShape WEST_WALL_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 2, 0, 8, 6, 16),
      createCuboidShape(0, 10, 0, 8, 14, 16));
  private static final VoxelShape EAST_WALL_SHAPE = VoxelShapes.union(
      createCuboidShape(8, 2, 0, 16, 6, 16),
      createCuboidShape(8, 10, 0, 16, 14, 16));

  private static final VoxelShape NS_FLOOR_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 0, 2, 16, 8, 6),
      createCuboidShape(0, 0, 10, 16, 8, 14));
  private static final VoxelShape EW_FLOOR_SHAPE = VoxelShapes.union(
      createCuboidShape(2, 0, 0, 6, 8, 16),
      createCuboidShape(10, 0, 0, 14, 8, 16));

  private static final VoxelShape NS_CEILING_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 8, 2, 16, 16, 6),
      createCuboidShape(0, 8, 10, 16, 16, 14));
  private static final VoxelShape EW_CEILING_SHAPE = VoxelShapes.union(
      createCuboidShape(2, 8, 0, 6, 16, 16),
      createCuboidShape(10, 8, 0, 14, 16, 16));

  private final BlockColor color;

  public HorizontalPipesBlock(final BlockColor color) {
    super(FabricBlockSettings.of(Material.STONE, color.getMapColor()).sounds(BlockSoundGroup.STONE));
    this.color = color;
  }

  @Override
  public BlockColor getColor() {
    return this.color;
  }

  @Override
  public BlockState getBlockStateForColor(BlockState state, BlockColor color) {
    return ModBlocks.HORIZONTAL_PIPES.get(color).getDefaultState()
        .with(FACE, state.get(FACE))
        .with(FACING, state.get(FACING))
        .with(WATERLOGGED, state.get(WATERLOGGED));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    Direction facing = state.get(FACING);
    return switch (state.get(FACE)) {
      case WALL -> switch (facing) {
        case NORTH -> NORTH_WALL_SHAPE;
        case SOUTH -> SOUTH_WALL_SHAPE;
        case WEST -> WEST_WALL_SHAPE;
        case EAST -> EAST_WALL_SHAPE;
        default -> VoxelShapes.empty();
      };
      case FLOOR -> {
        if (facing.getAxis() == Direction.Axis.Z) {
          yield NS_FLOOR_SHAPE;
        } else {
          yield EW_FLOOR_SHAPE;
        }
      }
      case CEILING -> {
        if (facing.getAxis() == Direction.Axis.Z) {
          yield NS_CEILING_SHAPE;
        } else {
          yield EW_CEILING_SHAPE;
        }
      }
    };
  }
}
