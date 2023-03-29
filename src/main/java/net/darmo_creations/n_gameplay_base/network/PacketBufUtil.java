package net.darmo_creations.n_gameplay_base.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

/**
 * This class defines convenience methods to write to/read from {@link PacketByteBuf} objects.
 */
public final class PacketBufUtil {
  /**
   * Writes a {@link Vec3d} to a {@link PacketByteBuf}.
   *
   * @param byteBuf Destination buffer.
   * @param vec3d   A vector.
   */
  public static void writeVec3d(PacketByteBuf byteBuf, final Vec3d vec3d) {
    byteBuf.writeDouble(vec3d.x);
    byteBuf.writeDouble(vec3d.y);
    byteBuf.writeDouble(vec3d.z);
  }

  /**
   * Reads a {@link Vec3d} from a {@link PacketByteBuf}.
   *
   * @param byteBuf Source buffer.
   * @return A vector.
   */
  public static Vec3d readVec3d(final PacketByteBuf byteBuf) {
    double x = byteBuf.readDouble();
    double y = byteBuf.readDouble();
    double z = byteBuf.readDouble();
    return new Vec3d(x, y, z);
  }

  private PacketBufUtil() {
  }
}
