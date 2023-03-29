package net.darmo_creations.n_gameplay_base.blocks;

import net.minecraft.block.AbstractBlock;

/**
 * Interface used to mark blocks added by this mod.
 */
public interface ModBlock {
  /**
   * Adds specific settings related to the hardness of the block.
   *
   * @param settings Base block settings.
   * @return The new settings.
   */
  static AbstractBlock.Settings getSettings(AbstractBlock.Settings settings) {
    return settings.strength(1_000_000, 3_600_000).dropsNothing();
  }
}
