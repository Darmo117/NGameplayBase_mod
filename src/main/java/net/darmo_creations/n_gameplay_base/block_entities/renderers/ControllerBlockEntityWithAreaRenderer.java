package net.darmo_creations.n_gameplay_base.block_entities.renderers;

import net.darmo_creations.n_gameplay_base.block_entities.ControllerBlockEntityWithArea;
import net.darmo_creations.n_gameplay_base.blocks.ControllerBlockWithArea;
import net.darmo_creations.n_gameplay_base.items.ControllerAreaTweakerItem;
import net.darmo_creations.n_gameplay_base.items.ModItems;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Optional;

/**
 * Renderer for the block entity associated to {@link ControllerBlockWithArea} blocks.
 * <p>
 * Renders the region as a wireframe box.
 *
 * @see ControllerBlockEntityWithArea
 * @see ControllerBlockWithArea
 */
public abstract class ControllerBlockEntityWithAreaRenderer<T extends ControllerBlockEntityWithArea>
    extends ControllerBlockEntityRenderer<T> {
  /**
   * Constructor required for registration.
   */
  protected ControllerBlockEntityWithAreaRenderer(BlockEntityRendererFactory.Context ignored) {
  }

  @Override
  protected Item getItem() {
    return ModItems.CONTROLLER_AREA_TWEAKER;
  }

  @Override
  protected Optional<T> getBlockEntityFromStack(ItemStack stack, World world) {
    return ControllerAreaTweakerItem.getControllerBlockEntity(stack, world, this.getBlockEntityClass());
  }

  /**
   * {@return the class of the block entities to render.}
   */
  protected abstract Class<T> getBlockEntityClass();

  @Override
  public void render(T be, float tickDelta, MatrixStack matrices,
                     VertexConsumerProvider vertexConsumers, int light, int overlay) {
    if (this.shouldRender(be)) {
      this.renderControllerToAreaLine(be, matrices, vertexConsumers);
      this.renderControllerBox(matrices, vertexConsumers);
      this.renderRegionBox(be, matrices, vertexConsumers);
      this.renderAdditional(be, matrices, vertexConsumers);
    }
  }

  /**
   * Render additonal things after the region and controller boxes have been drawn.
   *
   * @param be Block entity being rendered.
   */
  protected void renderAdditional(T be, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
  }

  /**
   * Render a line from the controller’s to the region’s center.
   */
  private void renderControllerToAreaLine(T be, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
    RenderUtils.renderLineInWorld(
        new Vec3d(0.5, 0.5, 0.5), this.getBoxCenter(be),
        1, 1, 0,
        matrices, vertexConsumers
    );
  }

  /**
   * Renders a box around the controller block.
   */
  private void renderControllerBox(MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
    RenderUtils.renderBoxInWorld(
        0.5, 0.5, 0.5,
        1.001, 1.001, 1.001,
        1, 1, 0,
        matrices, vertexConsumers
    );
  }

  protected Pair<BlockPos, BlockPos> getBoxCorners(T be) {
    BlockPos bePos = be.getPos();
    BlockPos boxPos1 = be.getLowerCorner().subtract(bePos);
    BlockPos boxPos2 = be.getUpperCorner().subtract(bePos);
    return new Pair<>(boxPos1, boxPos2);
  }

  protected Vec3d getBoxCenter(T be) {
    var corners = this.getBoxCorners(be);
    BlockPos boxPos1 = corners.getLeft();
    BlockPos boxPos2 = corners.getRight();
    return new Vec3d(
        (boxPos1.getX() + boxPos2.getX()) / 2.0 + 0.5,
        (boxPos1.getY() + boxPos2.getY()) / 2.0 + 0.5,
        (boxPos1.getZ() + boxPos2.getZ()) / 2.0 + 0.5
    );
  }

  /**
   * {@return a RGB triple containing the color of the inner box.}
   */
  protected abstract Triple<Float, Float, Float> getColor();

  /**
   * Renders a box around the region.
   */
  private void renderRegionBox(T be, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
    var corners = this.getBoxCorners(be);
    BlockPos boxPos1 = corners.getLeft();
    BlockPos boxPos2 = corners.getRight();

    // Active state box
    float r = 0, g = 0, b = 0;
    if (be.isTriggered()) {
      g = 1;
    } else {
      r = 1;
    }
    WorldRenderer.drawBox(
        matrices, vertexConsumers.getBuffer(RenderLayer.getLines()),
        boxPos1.getX(), boxPos1.getY(), boxPos1.getZ(),
        boxPos2.getX() + 1, boxPos2.getY() + 1, boxPos2.getZ() + 1,
        r, g, b, 1,
        r, g, b
    );

    // Colored box
    var color = this.getColor();
    r = color.getLeft();
    g = color.getMiddle();
    b = color.getRight();
    final double gap = 0.01;
    final double gap2 = 1 - gap;
    WorldRenderer.drawBox(
        matrices, vertexConsumers.getBuffer(RenderLayer.getLines()),
        boxPos1.getX() + gap, boxPos1.getY() + gap, boxPos1.getZ() + gap,
        boxPos2.getX() + gap2, boxPos2.getY() + 1 - gap, boxPos2.getZ() + 1 - gap,
        r, g, b, 1,
        r, g, b
    );
  }

  @Override
  public boolean rendersOutsideBoundingBox(T blockEntity) {
    return true;
  }

  @Override
  public int getRenderDistance() {
    return 1000;
  }
}
