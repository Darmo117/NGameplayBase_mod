package net.darmo_creations.n_gameplay_base.block_entities.renderers;

import net.darmo_creations.n_gameplay_base.block_entities.WindControllerBlockEntity;
import net.darmo_creations.n_gameplay_base.blocks.ModBlocks;
import net.darmo_creations.n_gameplay_base.blocks.WindControllerBlock;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.tuple.Triple;

/**
 * Renderer for the block entity associated to wind controllers.
 * <p>
 * Renders the region as a light gray wireframe box.
 *
 * @see WindControllerBlockEntity
 * @see WindControllerBlock
 * @see ModBlocks#WIND_CONTROLLER
 */
public class WindControllerBlockEntityRenderer
    extends ControllerBlockEntityWithAreaRenderer<WindControllerBlockEntity> {
  private static final Triple<Float, Float, Float> COLOR = Triple.of(0.7f, 0.7f, 0.7f);

  public WindControllerBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    super(context);
  }

  @Override
  protected Class<WindControllerBlockEntity> getBlockEntityClass() {
    return WindControllerBlockEntity.class;
  }

  @Override
  protected Triple<Float, Float, Float> getColor() {
    return COLOR;
  }

  @Override
  protected void renderAdditional(WindControllerBlockEntity be, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
    // Direction arrow
    Vec3d arrowPos = this.getBoxCenter(be);
    Vec3d arrowHeadPos = arrowPos.add(be.getWindDirection().normalize());
    var color = this.getColor();
    float r = color.getLeft();
    float g = color.getMiddle();
    float b = color.getRight();
    RenderUtils.renderLineInWorld(
        arrowPos,
        arrowHeadPos,
        r, g, b,
        matrices, vertexConsumers
    );
  }
}
