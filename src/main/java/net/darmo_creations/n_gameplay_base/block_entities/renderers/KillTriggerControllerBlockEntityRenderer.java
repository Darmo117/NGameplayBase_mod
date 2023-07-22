package net.darmo_creations.n_gameplay_base.block_entities.renderers;

import net.darmo_creations.n_gameplay_base.block_entities.KillTriggerControllerBlockEntity;
import net.darmo_creations.n_gameplay_base.blocks.KillTriggerControllerBlock;
import net.darmo_creations.n_gameplay_base.blocks.ModBlocks;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import org.apache.commons.lang3.tuple.Triple;

/**
 * Renderer for the block entity associated to kill trigger controllers.
 * <p>
 * Renders the region as a red wireframe box.
 *
 * @see KillTriggerControllerBlockEntity
 * @see KillTriggerControllerBlock
 * @see ModBlocks#KILL_TRIGGER_CONTROLLER
 */
public class KillTriggerControllerBlockEntityRenderer
    extends ControllerBlockEntityWithAreaRenderer<KillTriggerControllerBlockEntity> {
  private static final Triple<Float, Float, Float> COLOR = Triple.of(1f, 0f, 0f);

  public KillTriggerControllerBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    super(context);
  }

  @Override
  protected Class<KillTriggerControllerBlockEntity> getBlockEntityClass() {
    return KillTriggerControllerBlockEntity.class;
  }

  @Override
  protected Triple<Float, Float, Float> getColor() {
    return COLOR;
  }
}
