package net.darmo_creations.n_gameplay_base.blocks;

import net.darmo_creations.n_gameplay_base.block_entities.ControllerBlockEntityWithArea;
import net.darmo_creations.n_gameplay_base.items.ControllerAreaTweakerItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Base class for controller blocks that apply effects in a certain area.
 *
 * @see ControllerAreaTweakerItem
 * @see ControllerBlockEntityWithArea
 */
public abstract class ControllerBlockWithArea<T extends ControllerBlockEntityWithArea> extends ControllerBlock<T> {
  protected ControllerBlockWithArea(MapColor mapColor, Class<T> blockEntityClass) {
    super(mapColor, blockEntityClass);
  }

  @Override
  protected void onPlaced(T be, World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
    be.init(state);
  }
}
