package net.darmo_creations.n_gameplay_base.items;

import net.darmo_creations.n_gameplay_base.blocks.LightSensitiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Tool used to toggle the state of light-sensitive blocks.
 * <p>
 * Right-click on a compatible block to toggle its state.
 */
public class PassableStateTogglerItem extends Item {
  public PassableStateTogglerItem(Settings settings) {
    super(settings.maxCount(1));
  }

  @Override
  public ActionResult useOnBlock(ItemUsageContext context) {
    World world = context.getWorld();
    BlockPos pos = context.getBlockPos();
    BlockState blockState = world.getBlockState(pos);
    if (blockState.getBlock() instanceof LightSensitiveBlock<?> b) {
      b.toggleState(blockState, world, pos);
      return ActionResult.SUCCESS;
    }
    return ActionResult.FAIL;
  }
}
