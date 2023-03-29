package net.darmo_creations.n_gameplay_base.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Blocks implementing this interface are rendered sensitive to light.
 *
 * @param <T> Concrete block class.
 */
public interface LightSensitiveBlock<T extends LightSensitiveBlock<T>> extends ModBlock {
  /**
   * Adds specific settings related to the solidity of the block.
   *
   * @param settings Base block settings.
   * @param passable Whether the block should be passable.
   * @return The new settings.
   */
  static AbstractBlock.Settings getSettings(AbstractBlock.Settings settings, final boolean passable) {
    return ModBlock.getSettings(passable ? settings.nonOpaque() : settings);
  }

  /**
   * Toggles the state of this block between passable and solid.
   *
   * @param state Current block state.
   * @param world World the block is in.
   * @param pos   Block’s position.
   */
  void toggleState(BlockState state, World world, BlockPos pos);

  /**
   * Returns whether entities can pass through this block.
   */
  boolean isPassable();

  /**
   * Set the state of this block.
   *
   * @param state    Current block state.
   * @param world    World the block is in.
   * @param pos      Block’s position.
   * @param passable True to make the block passable, false to make it solid.
   * @return True if the block state changed, false otherwise.
   */
  default boolean setPassable(BlockState state, World world, BlockPos pos, boolean passable) {
    if (passable != this.isPassable()) {
      this.toggleState(state, world, pos);
      return true;
    }
    return false;
  }

  /**
   * Return the block instance corresponding to the opposite state of this block.
   */
  T getCounterpartBlock();

  /**
   * Sets the block instance corresponding to the opposite state of this block.
   * Sub-classes should also set the reverse relation as well in this method.
   */
  void setCounterpartBlock(T block);
}
