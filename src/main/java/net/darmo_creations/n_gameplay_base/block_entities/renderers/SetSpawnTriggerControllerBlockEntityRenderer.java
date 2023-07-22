package net.darmo_creations.n_gameplay_base.block_entities.renderers;

import net.darmo_creations.n_gameplay_base.block_entities.SetSpawnTriggerControllerBlockEntity;
import net.darmo_creations.n_gameplay_base.blocks.ModBlocks;
import net.darmo_creations.n_gameplay_base.blocks.SetSpawnTriggerControllerBlock;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import org.apache.commons.lang3.tuple.Triple;

/**
 * Renderer for the block entity associated to set-spawn trigger controllers.
 * <p>
 * Renders the region as a blue wireframe box.
 *
 * @see SetSpawnTriggerControllerBlockEntity
 * @see SetSpawnTriggerControllerBlock
 * @see ModBlocks#SET_SPAWN_TRIGGER_CONTROLLER
 */
public class SetSpawnTriggerControllerBlockEntityRenderer
    extends ControllerBlockEntityWithAreaRenderer<SetSpawnTriggerControllerBlockEntity> {
  private static final Triple<Float, Float, Float> COLOR = Triple.of(0.1f, 0.1f, 1f);

  public SetSpawnTriggerControllerBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    super(context);
  }

  @Override
  protected Class<SetSpawnTriggerControllerBlockEntity> getBlockEntityClass() {
    return SetSpawnTriggerControllerBlockEntity.class;
  }

  @Override
  protected Triple<Float, Float, Float> getColor() {
    return COLOR;
  }

  @Override
  protected void renderAdditional(SetSpawnTriggerControllerBlockEntity be, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
    // Render square at respawn location
    BlockPos respawnPos = be.getRespawnPosition().subtract(be.getPos());
    var color = this.getColor();
    float r = color.getLeft();
    float g = color.getMiddle();
    float b = color.getRight();
    RenderUtils.renderBoxInWorld(
        respawnPos.getX() + 0.5, respawnPos.getY() + 0.01,
        respawnPos.getZ() + 0.5, 1, 0, 1,
        r, g, b,
        matrices, vertexConsumers
    );

    Vec3d respawnCenter = Vec3d.of(respawnPos).add(0.5, 0, 0.5);
    // Render line to show respawn facing
    Vec3f unitVector = be.getRespawnFacing().getUnitVector();
    unitVector.scale(0.5f);
    RenderUtils.renderLineInWorld(
        respawnCenter,
        respawnCenter.add(new Vec3d(unitVector)),
        r, g, b,
        matrices, vertexConsumers
    );
    // Render line from region’s center to respawn location
    RenderUtils.renderLineInWorld(
        respawnCenter,
        this.getBoxCenter(be),
        r, g, b,
        matrices, vertexConsumers
    );
  }
}
