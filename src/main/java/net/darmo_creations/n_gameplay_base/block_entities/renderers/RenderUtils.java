package net.darmo_creations.n_gameplay_base.block_entities.renderers;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;

public final class RenderUtils {
  /**
   * Renders a wireframe box in the world centered around the given coordinates.
   *
   * @param matrices        Matrix stack.
   * @param vertexConsumers Vertex consumer provider.
   * @param x               X coordinate of center.
   * @param y               Y coordinate of center.
   * @param z               Z coordinate of center.
   * @param xSize           Size along x axis.
   * @param ySize           Size along y axis.
   * @param zSize           Size along z axis.
   * @param color           Color as a ARGB integer. Alpha channel is reversed:
   *                        255 means fully transparent, 0 means fully opaque.
   */
  public static void renderBoxInWorld(
      MatrixStack matrices, VertexConsumerProvider vertexConsumers,
      double x, double y, double z, double xSize, double ySize, double zSize,
      int color
  ) {
    VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLines());
    double hxSize = xSize / 2;
    double hySize = ySize / 2;
    double hzSize = zSize / 2;
    float alpha = 1 - (((color & 0xff000000) >> 24) / 255f);
    float red = ((color & 0xff0000) >> 16) / 255f;
    float green = ((color & 0xff00) >> 8) / 255f;
    float blue = (color & 0xff) / 255f;
    WorldRenderer.drawBox(
        matrices, vertexConsumer,
        x - hxSize, y - hySize, z - hzSize,
        x + hxSize, y + hySize, z + hzSize,
        red, green, blue, alpha
    );
  }

  private RenderUtils() {
  }
}
