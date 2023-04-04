package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;

/**
 * A simple colored slab block.
 */
public class ColoredVerticalSlabBlock extends VerticalSlabBlock implements Colored {
  private final BlockColor color;

  /**
   * Creates a vertical slab for the given color.
   *
   * @param color Slab’s color.
   */
  public ColoredVerticalSlabBlock(final BlockColor color) {
    super(FabricBlockSettings.of(Material.STONE, color.getMapColor()));
    this.color = color;
  }

  @Override
  public BlockColor getColor() {
    return this.color;
  }

  @Override
  public BlockState getBlockStateForColor(BlockState state, BlockColor color) {
    return ModBlocks.COLORED_VERTICAL_SLABS.get(color).getDefaultState()
        .with(TYPE, state.get(TYPE))
        .with(WATERLOGGED, state.get(WATERLOGGED));
  }
}
