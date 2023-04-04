package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class ColoredCompositeBlock extends CompositeBlock implements Colored {
  private final BlockColor color;

  /**
   * Creates a corner block for the given color.
   *
   * @param color Block’s color.
   */
  public ColoredCompositeBlock(final BlockColor color) {
    super(FabricBlockSettings.of(Material.STONE, color.getMapColor()).sounds(BlockSoundGroup.STONE));
    this.color = color;
  }

  @Override
  public BlockColor getColor() {
    return this.color;
  }

  @Override
  public BlockState getBlockStateForColor(BlockState state, BlockColor color) {
    return ModBlocks.COMPOSITE_BLOCKS.get(color).getDefaultState()
        .with(NORTH_EAST_TOP, state.get(NORTH_EAST_TOP))
        .with(NORTH_WEST_TOP, state.get(NORTH_WEST_TOP))
        .with(NORTH_EAST_BOTTOM, state.get(NORTH_EAST_BOTTOM))
        .with(NORTH_WEST_BOTTOM, state.get(NORTH_WEST_BOTTOM))
        .with(SOUTH_EAST_TOP, state.get(SOUTH_EAST_TOP))
        .with(SOUTH_WEST_TOP, state.get(SOUTH_WEST_TOP))
        .with(SOUTH_EAST_BOTTOM, state.get(SOUTH_EAST_BOTTOM))
        .with(SOUTH_WEST_BOTTOM, state.get(SOUTH_WEST_BOTTOM))
        .with(WATERLOGGED, state.get(WATERLOGGED));
  }
}
