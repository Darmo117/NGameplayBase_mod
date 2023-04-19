package net.darmo_creations.n_gameplay_base.block_entities.renderers;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;

public final class RenderUtils {
  /**
   * Renders a wireframe box in the world centered around the given coordinates.
   *
   * @param centerPos       Center position.
   * @param size            Box size.
   * @param r               Red intensity between 0 and 1.
   * @param g               Green intensity between 0 and 1.
   * @param b               Blue intensity between 0 and 1.
   * @param matrices        Matrix stack.
   * @param vertexConsumers Vertex consumer provider.
   */
  public static void renderBoxInWorld(
      final Vec3d centerPos, final Vec3d size,
      float r, float g, float b,
      MatrixStack matrices, VertexConsumerProvider vertexConsumers
  ) {
    renderBoxInWorld(
        centerPos.getX(), centerPos.getY(), centerPos.getZ(),
        size.getX(), size.getY(), size.getZ(),
        r, g, b,
        matrices, vertexConsumers
    );
  }

  /**
   * Renders a wireframe box in the world centered around the given coordinates.
   *
   * @param x               X coordinate of center.
   * @param y               Y coordinate of center.
   * @param z               Z coordinate of center.
   * @param xSize           Size along x axis.
   * @param ySize           Size along y axis.
   * @param zSize           Size along z axis.
   * @param r               Red intensity between 0 and 1.
   * @param g               Green intensity between 0 and 1.
   * @param b               Blue intensity between 0 and 1.
   * @param matrices        Matrix stack.
   * @param vertexConsumers Vertex consumer provider.
   */
  public static void renderBoxInWorld(
      double x, double y, double z, double xSize, double ySize, double zSize,
      float r, float g, float b,
      MatrixStack matrices, VertexConsumerProvider vertexConsumers
  ) {
    VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLines());
    double hxSize = xSize / 2;
    double hySize = ySize / 2;
    double hzSize = zSize / 2;
    WorldRenderer.drawBox(
        matrices, vertexConsumer,
        x - hxSize, y - hySize, z - hzSize,
        x + hxSize, y + hySize, z + hzSize,
        r, g, b, 1
    );
  }

  /**
   * Renders a line between two points.
   *
   * @param pos1            First position.
   * @param pos2            Second position.
   * @param r               Red intensity between 0 and 1.
   * @param g               Green intensity between 0 and 1.
   * @param b               Blue intensity between 0 and 1.
   * @param matrices        Matrix stack.
   * @param vertexConsumers Vertex consumer provider.
   */
  public static void renderLineInWorld(Vec3d pos1, Vec3d pos2, float r, float g, float b,
                                       MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
    float x1 = (float) pos1.getX();
    float y1 = (float) pos1.getY();
    float z1 = (float) pos1.getZ();
    float x2 = (float) pos2.getX();
    float y2 = (float) pos2.getY();
    float z2 = (float) pos2.getZ();
    int red = (int) (r * 255);
    int green = (int) (g * 255);
    int blue = (int) (b * 255);
    VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLines());
    Matrix4f matrix4f = matrices.peek().getPositionMatrix();
    Matrix3f matrix3f = matrices.peek().getNormalMatrix();
    // Draw 3 lines with different normals to give the resulting line the same width from all camera angles
    vertexConsumer.vertex(matrix4f, x1, y1, z1).color(red, green, blue, 255).normal(matrix3f, 0, 1, 0).next();
    vertexConsumer.vertex(matrix4f, x2, y2, z2).color(red, green, blue, 255).normal(matrix3f, 0, 1, 0).next();
    vertexConsumer.vertex(matrix4f, x1, y1, z1).color(red, green, blue, 255).normal(matrix3f, 1, 0, 0).next();
    vertexConsumer.vertex(matrix4f, x2, y2, z2).color(red, green, blue, 255).normal(matrix3f, 1, 0, 0).next();
    vertexConsumer.vertex(matrix4f, x1, y1, z1).color(red, green, blue, 255).normal(matrix3f, 0, 0, 1).next();
    vertexConsumer.vertex(matrix4f, x2, y2, z2).color(red, green, blue, 255).normal(matrix3f, 0, 0, 1).next();
  }

  private RenderUtils() {
  }
}
