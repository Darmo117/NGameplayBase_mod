package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.SlabBlock;
import net.minecraft.sound.BlockSoundGroup;

/**
 * A simple colored slab block.
 */
public class ColoredSlabBlock extends SlabBlock implements Colored {
  private final BlockColor color;

  /**
   * Creates a slab for the given color.
   *
   * @param color Slab’s color.
   */
  public ColoredSlabBlock(final BlockColor color) {
    super(ModBlock.getSettings(FabricBlockSettings.of(Material.STONE, color.getMapColor()).sounds(BlockSoundGroup.STONE)));
    this.color = color;
  }

  @Override
  public BlockColor getColor() {
    return this.color;
  }
}
