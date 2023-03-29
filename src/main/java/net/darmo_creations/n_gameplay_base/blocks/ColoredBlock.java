package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

/**
 * A simple colored full block with no behavior.
 */
public class ColoredBlock extends Block implements Colored {
  private final BlockColor color;

  /**
   * Creates a plain block for the given color.
   *
   * @param color Block’s color.
   */
  public ColoredBlock(final BlockColor color) {
    super(ModBlock.getSettings(FabricBlockSettings.of(Material.STONE, color.getMapColor()).sounds(BlockSoundGroup.STONE)));
    this.color = color;
  }

  @Override
  public BlockColor getColor() {
    return this.color;
  }
}
