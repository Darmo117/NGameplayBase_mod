package net.darmo_creations.n_gameplay_base.blocks;

/**
 * Blocks implementing this interface indicate that they are colored.
 *
 * @see BlockColor
 */
public interface Colored extends ModBlock {
  /**
   * Returns the color of this block.
   */
  BlockColor getColor();
}
