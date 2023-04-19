package net.darmo_creations.n_gameplay_base.block_entities.renderers;

import net.darmo_creations.n_gameplay_base.block_entities.LightOrb;
import net.darmo_creations.n_gameplay_base.block_entities.LightOrbControllerBlockEntity;
import net.darmo_creations.n_gameplay_base.block_entities.PathCheckpoint;
import net.darmo_creations.n_gameplay_base.blocks.LightOrbControllerBlock;
import net.darmo_creations.n_gameplay_base.blocks.ModBlocks;
import net.darmo_creations.n_gameplay_base.items.LightOrbTweakerItem;
import net.darmo_creations.n_gameplay_base.items.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

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
  private void renderLightOrbBox(LightOrbControllerBlockEntity be, LightOrb orb,
                                 MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
    RenderUtils.renderBoxInWorld(
        orb.getPosition().subtract(Vec3d.of(be.getPos())),
        orb.getHitBoxSize(),
        1, 1, 1,
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
      int r, g = 0, b = 0;
      if (isFirst && isLast) {
        r = 1;
      } else if (isFirst) {
        r = g = 1;
      } else {
        r = b = 1;
      }
      RenderUtils.renderBoxInWorld(x, y, z, 1, 1, 1, r, g, b, matrices, vertexConsumers);
    }

    int r = 0, g = 0, b = 0;
    if (checkpoint.isStop()) {
      r = 1;
    } else {
      g = 1;
    }
    RenderUtils.renderBoxInWorld(x, y, z, 0.5, 0.5, 0.5, r, g, b, matrices, vertexConsumers);
  }

  /**
   * Renders a line between two checkpoints.
   */
  private void renderLine(LightOrbControllerBlockEntity be, PathCheckpoint checkpoint1, PathCheckpoint checkpoint2,
                          MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
    BlockPos tePos = be.getPos();
    Vec3d p1 = Vec3d.of(checkpoint1.getPos().subtract(tePos)).add(0.5, 0.5, 0.5);
    Vec3d p2 = Vec3d.of(checkpoint2.getPos().subtract(tePos)).add(0.5, 0.5, 0.5);
    RenderUtils.renderLineInWorld(p1, p2, 1, 1, 1, matrices, vertexConsumers);
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
