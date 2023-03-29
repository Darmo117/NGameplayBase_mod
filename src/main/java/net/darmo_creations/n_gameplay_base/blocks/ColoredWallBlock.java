package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.WallBlock;
import net.minecraft.sound.BlockSoundGroup;

/**
 * A wall with a specific color.
 */
public class ColoredWallBlock extends WallBlock implements Colored {
  private final BlockColor color;

  public ColoredWallBlock(final BlockColor color) {
    super(ModBlock.getSettings(FabricBlockSettings.of(Material.STONE, color.getMapColor()).sounds(BlockSoundGroup.STONE)));
    this.color = color;
  }

  @Override
  public BlockColor getColor() {
    return this.color;
  }
}
