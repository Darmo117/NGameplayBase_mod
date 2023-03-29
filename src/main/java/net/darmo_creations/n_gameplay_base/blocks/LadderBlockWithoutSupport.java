package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

/**
 * A ladder block that can be placed anywhere and does not break when its supporting block is removed.
 */
public class LadderBlockWithoutSupport extends LadderBlock implements Colored {
  private final BlockColor color;

  /**
   * Creates a ladder with the given color.
   *
   * @param color Ladder’s color.
   */
  public LadderBlockWithoutSupport(final BlockColor color) {
    super(ModBlock.getSettings(FabricBlockSettings.of(Material.STONE, color.getMapColor()).sounds(BlockSoundGroup.STONE).nonOpaque()));
    this.color = color;
  }

  @Override
  public BlockColor getColor() {
    return this.color;
  }

  @Override
  public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    return true;
  }
}
