package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.sound.BlockSoundGroup;

/**
 * A simple colored stairs block.
 */
public class ColoredStairsBlock extends StairsBlock implements Colored {
  private final BlockColor color;

  /**
   * Creates stairs block for the given colored block.
   *
   * @param baseBlock Colored block to use a base.
   */
  public ColoredStairsBlock(final ColoredBlock baseBlock) {
    super(baseBlock.getDefaultState(), ModBlock.getSettings(
        FabricBlockSettings.of(baseBlock.getDefaultState().getMaterial(), baseBlock.getColor().getMapColor())
            .sounds(BlockSoundGroup.STONE)));
    this.color = baseBlock.getColor();
  }

  @Override
  public BlockColor getColor() {
    return this.color;
  }

  @Override
  public BlockState getBlockStateForColor(BlockState state, BlockColor color) {
    return ModBlocks.COLORED_STAIRS.get(color).getDefaultState()
        .with(FACING, state.get(FACING))
        .with(HALF, state.get(HALF))
        .with(SHAPE, state.get(SHAPE));
  }
}
