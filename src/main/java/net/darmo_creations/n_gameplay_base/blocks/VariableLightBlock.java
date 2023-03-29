package net.darmo_creations.n_gameplay_base.blocks;

import net.darmo_creations.n_gameplay_base.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Function;

/**
 * Base class for blocks with a light level that may vary.
 */
public abstract class VariableLightBlock extends Block implements ModBlock {
  public static final IntProperty LIGHT_LEVEL = IntProperty.of("light_level", 0, 15);

  public VariableLightBlock(Settings settings) {
    super(ModBlock.getSettings(settings.luminance(state -> state.get(LIGHT_LEVEL))));
  }

  /**
   * Increases the light level of the targetted block.
   *
   * @param world Word the block is in.
   * @param pos   Position of the block.
   */
  public void increaseLightLevel(World world, BlockPos pos) {
    this.setLightLevel(world, pos, l -> (l + 1) % 16);
  }

  /**
   * Decreases the light level of the targetted block.
   *
   * @param world Word the block is in.
   * @param pos   Position of the block.
   */
  public void decreaseLightLevel(World world, BlockPos pos) {
    this.setLightLevel(world, pos, l -> Utils.trueModulo(l - 1, 16));
  }

  /**
   * Sets the light level of the targetted block using the given function.
   *
   * @param world       Word the block is in.
   * @param pos         Position of the block.
   * @param transformer A function that takes in the current light level then returns its new value.
   */
  protected void setLightLevel(World world, BlockPos pos, Function<Integer, Integer> transformer) {
    BlockState blockState = world.getBlockState(pos);
    int lightLevel = blockState.get(LIGHT_LEVEL);
    world.setBlockState(pos, blockState.with(LIGHT_LEVEL, transformer.apply(lightLevel)));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder.add(LIGHT_LEVEL));
  }
}
