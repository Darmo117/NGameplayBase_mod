package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

/**
 * A block representing a pair of “pipes“ oriented along the y axis.
 */
public class VerticalPipesBlock extends WaterloggableHorizontalFacingBlock implements Colored {
  private static final VoxelShape NORTH_SHAPE = VoxelShapes.union(
      createCuboidShape(2, 0, 0, 6, 16, 8),
      createCuboidShape(10, 0, 0, 14, 16, 8));
  private static final VoxelShape SOUTH_SHAPE = VoxelShapes.union(
      createCuboidShape(2, 0, 8, 6, 16, 16),
      createCuboidShape(10, 0, 8, 14, 16, 16));
  private static final VoxelShape WEST_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 0, 2, 8, 16, 6),
      createCuboidShape(0, 0, 10, 8, 16, 14));
  private static final VoxelShape EAST_SHAPE = VoxelShapes.union(
      createCuboidShape(8, 0, 2, 16, 16, 6),
      createCuboidShape(8, 0, 10, 16, 16, 14));

  private final BlockColor color;

  public VerticalPipesBlock(final BlockColor color) {
    super(FabricBlockSettings.of(Material.STONE, color.getMapColor()).sounds(BlockSoundGroup.STONE),
        NORTH_SHAPE, EAST_SHAPE, SOUTH_SHAPE, WEST_SHAPE);
    this.color = color;
  }

  @Override
  public BlockColor getColor() {
    return this.color;
  }
}
