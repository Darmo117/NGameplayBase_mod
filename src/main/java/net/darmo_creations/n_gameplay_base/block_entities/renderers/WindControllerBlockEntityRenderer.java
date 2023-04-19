package net.darmo_creations.n_gameplay_base.block_entities.renderers;

import net.darmo_creations.n_gameplay_base.block_entities.WindControllerBlockEntity;
import net.darmo_creations.n_gameplay_base.blocks.ModBlocks;
import net.darmo_creations.n_gameplay_base.blocks.WindControllerBlock;
import net.darmo_creations.n_gameplay_base.items.ModItems;
import net.darmo_creations.n_gameplay_base.items.WindTweakerItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * Renderer for the tile entity associated to wind controllers.
 * <p>
 * Renders the windy region as a wireframe box.
 *
 * @see WindControllerBlockEntity
 * @see WindControllerBlock
 * @see ModBlocks#WIND_CONTROLLER
 */
public class WindControllerBlockEntityRenderer implements BlockEntityRenderer<WindControllerBlockEntity> {
  /**
   * Constructor required for registration.
   */
  public WindControllerBlockEntityRenderer(BlockEntityRendererFactory.Context ignored) {
  }

  @Override
  public void render(WindControllerBlockEntity be, float tickDelta, MatrixStack matrices,
                     VertexConsumerProvider vertexConsumers, int light, int overlay) {
    PlayerEntity player = MinecraftClient.getInstance().player;
    //noinspection ConstantConditions
    ItemStack stack = player.getMainHandStack();

    if ((player.isCreativeLevelTwoOp() || player.isSpectator())
        && stack.getItem() == ModItems.WIND_TWEAKER
        && WindTweakerItem.getControllerTileEntity(stack, be.getWorld()).map(t -> t.getPos().equals(be.getPos())).orElse(false)) {
      this.renderControllerBox(matrices, vertexConsumers);
      this.renderRegionBox(be, matrices, vertexConsumers);
    }
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
   * Renders a box around the windy region.
   */
  private void renderRegionBox(WindControllerBlockEntity be, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
    BlockPos bePos = be.getPos();
    BlockPos boxPos1 = be.getLowerCorner().subtract(bePos);
    BlockPos boxPos2 = be.getUpperCorner().subtract(bePos);
    float r = 0, g = 0, b = 0;
    if (be.isActive()) {
      g = 1;
    } else {
      r = 1;
    }
    WorldRenderer.drawBox(
        matrices, vertexConsumers.getBuffer(RenderLayer.getLines()),
        boxPos1.getX(), boxPos1.getY(), boxPos1.getZ(), boxPos2.getX() + 1, boxPos2.getY() + 1, boxPos2.getZ() + 1,
        r, g, b, 1,
        r, g, b
    );
    // Direction arrow
    Vec3d arrowPos = new Vec3d(
        (boxPos1.getX() + boxPos2.getX()) / 2.0 + 0.5,
        (boxPos1.getY() + boxPos2.getY()) / 2.0 + 0.5,
        (boxPos1.getZ() + boxPos2.getZ()) / 2.0 + 0.5
    );
    Vec3d arrowHeadPos = arrowPos.add(be.getWindDirection().normalize());
    final double arrowBoxSize = 0.1;
    RenderUtils.renderBoxInWorld(
        arrowPos,
        new Vec3d(arrowBoxSize, arrowBoxSize, arrowBoxSize),
        r, g, b,
        matrices, vertexConsumers
    );
    RenderUtils.renderLineInWorld(
        arrowPos,
        arrowHeadPos,
        r, g, b,
        matrices, vertexConsumers
    );
  }

  @Override
  public boolean rendersOutsideBoundingBox(WindControllerBlockEntity blockEntity) {
    return true;
  }

  @Override
  public int getRenderDistance() {
    return 1000;
  }
}
