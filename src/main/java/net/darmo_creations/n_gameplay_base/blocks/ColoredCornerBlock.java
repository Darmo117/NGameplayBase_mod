package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class ColoredCornerBlock extends CornerBlock implements Colored {
  private final BlockColor color;

  /**
   * Creates a corner block for the given color.
   *
   * @param color Block’s color.
   */
  public ColoredCornerBlock(final BlockColor color) {
    super(FabricBlockSettings.of(Material.STONE, color.getMapColor()).sounds(BlockSoundGroup.STONE));
    this.color = color;
  }

  @Override
  public BlockColor getColor() {
    return this.color;
  }

  @Override
  public BlockState getBlockStateForColor(BlockState state, BlockColor color) {
    return ModBlocks.CORNER_BLOCKS.get(color).getDefaultState()
        .with(VERTICAL_POSITION, state.get(VERTICAL_POSITION))
        .with(POSITION, state.get(POSITION))
        .with(WATERLOGGED, state.get(WATERLOGGED));
  }
}
