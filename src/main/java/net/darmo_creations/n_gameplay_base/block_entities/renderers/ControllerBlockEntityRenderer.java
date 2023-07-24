package net.darmo_creations.n_gameplay_base.block_entities.renderers;

import net.darmo_creations.n_gameplay_base.block_entities.ControllerBlockEntity;
import net.darmo_creations.n_gameplay_base.items.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Optional;

/**
 * Base class for {@link ControllerBlockEntity} renderers.
 *
 * @param <T> Type of rendered block entity.
 */
public abstract class ControllerBlockEntityRenderer<T extends ControllerBlockEntity>
    implements BlockEntityRenderer<T> {
  /**
   * {@return whether anything should be rendered.}
   *
   * @param be The block entity to render.
   */
  protected boolean shouldRender(final T be) {
    ClientPlayerEntity player = MinecraftClient.getInstance().player;
    if (player == null || !player.isCreativeLevelTwoOp() && !player.isSpectator()) {
      return false;
    }
    ItemStack stack = player.getMainHandStack();
    return stack.getItem() == ModItems.CONTROLLER_STICK
        || stack.getItem() == this.getItem()
        && this.getBlockEntityFromStack(stack, be.getWorld()).map(t -> t.getPos().equals(be.getPos())).orElse(false);
  }

  /**
   * {@return the item players should be holding to render.}
   */
  protected abstract Item getItem();

  /**
   * {@return the block entity pointed to by the given stack.}
   *
   * @param stack Item stack to pull the block entity’s position from.
   * @param world The current world.
   */
  protected abstract Optional<T> getBlockEntityFromStack(ItemStack stack, World world);
}
