package net.darmo_creations.n_gameplay_base.block_entities.renderers;

import net.darmo_creations.n_gameplay_base.block_entities.LightOrb;
import net.darmo_creations.n_gameplay_base.block_entities.LightOrbControllerBlockEntity;
import net.darmo_creations.n_gameplay_base.block_entities.PathCheckpoint;
import net.darmo_creations.n_gameplay_base.blocks.LightOrbControllerBlock;
import net.darmo_creations.n_gameplay_base.blocks.ModBlocks;
import net.darmo_creations.n_gameplay_base.items.LightOrbTweakerItem;
import net.darmo_creations.n_gameplay_base.items.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.List;

/**
 * Renderer for the tile entity associated to light orb controllers.
 * <p>
 * Renders the checkpoints as cubes and the path as straight lines between checkpoints.
 *
 * @see LightOrbControllerBlockEntity
 * @see LightOrbControllerBlock
 * @see ModBlocks#LIGHT_ORB_CONTROLLER
 */
public class LightOrbControllerBlockEntityRenderer implements BlockEntityRenderer<LightOrbControllerBlockEntity> {
  /**
   * Constructor required for registration.
   */
  public LightOrbControllerBlockEntityRenderer(BlockEntityRendererFactory.Context ignored) {
  }

  @Override
  public void render(LightOrbControllerBlockEntity be, float tickDelta, MatrixStack matrices,
                     VertexConsumerProvider vertexConsumers, int light, int overlay) {
    PlayerEntity player = MinecraftClient.getInstance().player;
    //noinspection ConstantConditions
    ItemStack stack = player.getMainHandStack();

    if ((player.isCreativeLevelTwoOp() || player.isSpectator())
        && stack.getItem() == ModItems.LIGHT_ORB_TWEAKER
        && LightOrbTweakerItem.getControllerTileEntity(stack, be.getWorld()).map(t -> t.getPos().equals(be.getPos())).orElse(false)) {
      this.renderControllerBox(matrices, vertexConsumers);
      be.getOrb().ifPresent(orb -> this.renderLightOrbBox(be, orb, matrices, vertexConsumers));

      List<PathCheckpoint> checkpoints = be.getCheckpoints();
      for (int i = 0, size = checkpoints.size(); i < size; i++) {
        PathCheckpoint checkpoint = checkpoints.get(i);
        PathCheckpoint nextCheckpoint = null;
        if (i == size - 1) {
          if (be.loops()) {
            nextCheckpoint = checkpoints.get(0);
          }
        } else {
          nextCheckpoint = checkpoints.get(i + 1);
        }
        this.renderCheckpoint(be, checkpoint, i == 0, i == size - 1, matrices, vertexConsumers);
        if (nextCheckpoint != null) {
          this.renderLine(be, checkpoint, nextCheckpoint, matrices, vertexConsumers);
        }
      }
    }
  }

  /**
   * Renders the hit box of the given light orb.
   */
  private void renderLightOrbBox(LightOrbControllerBlockEntity be, LightOrb orb, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
    BlockPos bePos = be.getPos();
    Vec3d pos = orb.getPosition();
    Vec3d hitBox = orb.getHitBoxSize();
    RenderUtils.renderBoxInWorld(
        matrices, vertexConsumers,
        pos.getX() - bePos.getX(), pos.getY() - bePos.getY(), pos.getZ() - bePos.getZ(),
        hitBox.getX(), hitBox.getY(), hitBox.getZ(),
        0xffffff
    );
  }

  /**
   * Renders a box around the controller block.
   */
  private void renderControllerBox(MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
    final double offset = 1e-3;
    RenderUtils.renderBoxInWorld(matrices, vertexConsumers,
        0.5, 0.5, 0.5,
        1 + offset, 1 + offset, 1 + offset,
        0xffff00
    );
  }

  /**
   * Renders boxes for the given checkpoint.
   */
  private void renderCheckpoint(
      LightOrbControllerBlockEntity be, PathCheckpoint checkpoint,
      boolean isFirst, boolean isLast,
      MatrixStack matrices, VertexConsumerProvider vertexConsumers
  ) {
    BlockPos bePos = be.getPos();
    BlockPos checkpointPos = checkpoint.getPos();
    double x = checkpointPos.getX() - bePos.getX() + 0.5;
    double y = checkpointPos.getY() - bePos.getY() + 0.5;
    double z = checkpointPos.getZ() - bePos.getZ() + 0.5;

    if (isFirst || isLast) {
      int color;
      if (isFirst && isLast) {
        color = 0xff0000;
      } else if (isFirst) {
        color = 0xffff00;
      } else {
        color = 0xff00ff;
      }
      RenderUtils.renderBoxInWorld(matrices, vertexConsumers, x, y, z, 1, 1, 1, color);
    }

    int color = checkpoint.isStop() ? 0xff0000 : 0x00ff00;
    RenderUtils.renderBoxInWorld(matrices, vertexConsumers, x, y, z, 0.5, 0.5, 0.5, color);
  }

  /**
   * Renders a line between two checkpoints.
   */
  private void renderLine(LightOrbControllerBlockEntity be, PathCheckpoint checkpoint1, PathCheckpoint checkpoint2,
                          MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
    BlockPos tePos = be.getPos();
    BlockPos pos1 = checkpoint1.getPos();
    BlockPos pos2 = checkpoint2.getPos();
    final float offset = 0.5f;
    float x1 = pos1.getX() - tePos.getX() + offset;
    float y1 = pos1.getY() - tePos.getY() + offset;
    float z1 = pos1.getZ() - tePos.getZ() + offset;
    float x2 = pos2.getX() - tePos.getX() + offset;
    float y2 = pos2.getY() - tePos.getY() + offset;
    float z2 = pos2.getZ() - tePos.getZ() + offset;
    VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLines());
    Matrix4f matrix4f = matrices.peek().getPositionMatrix();
    Matrix3f matrix3f = matrices.peek().getNormalMatrix();
    // Draw 3 lines with different normals to give the resulting line the same width from all camera angles
    vertexConsumer.vertex(matrix4f, x1, y1, z1).color(255, 255, 255, 255).normal(matrix3f, 0, 1, 0).next();
    vertexConsumer.vertex(matrix4f, x2, y2, z2).color(255, 255, 255, 255).normal(matrix3f, 0, 1, 0).next();
    vertexConsumer.vertex(matrix4f, x1, y1, z1).color(255, 255, 255, 255).normal(matrix3f, 1, 0, 0).next();
    vertexConsumer.vertex(matrix4f, x2, y2, z2).color(255, 255, 255, 255).normal(matrix3f, 1, 0, 0).next();
    vertexConsumer.vertex(matrix4f, x1, y1, z1).color(255, 255, 255, 255).normal(matrix3f, 0, 0, 1).next();
    vertexConsumer.vertex(matrix4f, x2, y2, z2).color(255, 255, 255, 255).normal(matrix3f, 0, 0, 1).next();
  }

  @Override
  public boolean rendersOutsideBoundingBox(LightOrbControllerBlockEntity blockEntity) {
    return true;
  }

  @Override
  public int getRenderDistance() {
    return 1000;
  }
}
