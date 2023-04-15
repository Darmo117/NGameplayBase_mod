package net.darmo_creations.n_gameplay_base;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;

/**
 * This class defines various utility functions.
 */
public final class Utils {
  /**
   * Returns the block entity of the given class at the given position.
   *
   * @param blockEntityClass Block entity’s class.
   * @param world            The world.
   * @param pos              Block entity’s position.
   * @param <T>              Block entity’s type.
   * @return The block entity if found, an empty optional otherwise.
   */
  public static <T extends BlockEntity> Optional<T> getBlockEntity(Class<T> blockEntityClass, World world, BlockPos pos) {
    BlockEntity be = world.getBlockEntity(pos);
    if (blockEntityClass.isInstance(be)) {
      return Optional.of(blockEntityClass.cast(be));
    }
    return Optional.empty();
  }

  /**
   * Converts a block position to a string.
   */
  public static String blockPosToString(BlockPos pos) {
    return "%d %d %d".formatted(pos.getX(), pos.getY(), pos.getZ());
  }

  /**
   * Performs a true modulo operation using the mathematical definition of "a mod b".
   *
   * @param a Value to get the modulo of.
   * @param b The divisor.
   * @return a mod b
   */
  public static int trueModulo(int a, int b) {
    return ((a % b) + b) % b;
  }

  /**
   * Extracts a {@link Vec3d} from an NBT tag.
   *
   * @param nbt The NBT tag to query from.
   * @param key Tag’s key.
   * @return The vector.
   */
  public static Vec3d getVec3d(final NbtCompound nbt, final String key) {
    NbtCompound tag = nbt.getCompound(key);
    return new Vec3d(tag.getDouble("X"), tag.getDouble("Y"), tag.getDouble("Z"));
  }

  /**
   * Puts a {@link Vec3d} in an NBT tag.
   *
   * @param vector The vector to serialize.
   * @param nbt    The NBT tag to insert into.
   * @param key    Tag’s key.
   */
  public static void putVec3d(final Vec3d vector, final NbtCompound nbt, final String key) {
    NbtCompound tag = new NbtCompound();
    tag.putDouble("X", vector.getX());
    tag.putDouble("Y", vector.getY());
    tag.putDouble("Z", vector.getZ());
    nbt.put(key, tag);
  }

  private Utils() {
  }
}
