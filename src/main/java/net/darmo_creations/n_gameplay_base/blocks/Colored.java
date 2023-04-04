package net.darmo_creations.n_gameplay_base.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

  /**
   * Replace this block by a similar one with the same properties but the given color.
   *
   * @param world The world.
   * @param color The new color.
   * @return True if the color changed, false otherwise.
   */
  default boolean setColor(BlockState state, World world, BlockPos pos, BlockColor color) {
    if (this.getColor() != color) {
      world.setBlockState(pos, this.getBlockStateForColor(state, color));
      return true;
    }
    return false;
  }

  BlockState getBlockStateForColor(BlockState state, BlockColor color);
}
