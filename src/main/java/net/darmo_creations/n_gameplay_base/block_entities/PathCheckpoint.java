package net.darmo_creations.n_gameplay_base.block_entities;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

/**
 * Mutable implementation of interface.
 */
public class PathCheckpoint implements Cloneable {
  private static final String POS_TAG_KEY = "Pos";
  private static final String STOP_TAG_KEY = "IsStop";
  private static final String TICKS_TAG_KEY = "TicksToWait";

  private final BlockPos pos;
  private boolean stop;
  private int ticksToWait;

  /**
   * Create a checkpoint for the given NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public PathCheckpoint(NbtCompound tag) {
    this(
        NbtHelper.toBlockPos(tag.getCompound(POS_TAG_KEY)),
        tag.getBoolean(STOP_TAG_KEY),
        tag.getInt(TICKS_TAG_KEY)
    );
  }

  /**
   * Create a checkpoint for the given position and stop state.
   *
   * @param pos         Block position.
   * @param stop        Whether the light orb should stop at this checkpoint.
   * @param ticksToWait Number of ticks the entity has to wait for before moving again.
   */
  public PathCheckpoint(final BlockPos pos, boolean stop, int ticksToWait) {
    this.pos = Objects.requireNonNull(pos);
    this.stop = stop;
    this.ticksToWait = ticksToWait;
  }

  /**
   * The block position of this checkpoint.
   */
  public BlockPos getPos() {
    return this.pos;
  }

  /**
   * Whether the light orb should stop at this checkpoint.
   */
  public boolean isStop() {
    return this.stop;
  }

  /**
   * Set whether the light orb should stop at this checkpoint.
   */
  public void setStop(boolean stop) {
    this.stop = stop;
  }

  /**
   * Get the number of ticks the orb has to wait for before moving again.
   */
  public int getTicksToWait() {
    return this.ticksToWait;
  }

  /**
   * Set the number of ticks the orb has to wait for before moving again.
   */
  public void setTicksToWait(int ticksToWait) {
    if (ticksToWait < 0) {
      throw new IllegalArgumentException("wait time should be ≥ 0");
    }
    this.ticksToWait = ticksToWait;
  }

  /**
   * Convert this checkpoint to an NBT tag.
   */
  public NbtCompound toNBT() {
    NbtCompound tag = new NbtCompound();
    tag.put(POS_TAG_KEY, NbtHelper.fromBlockPos(this.pos));
    tag.putBoolean(STOP_TAG_KEY, this.stop);
    tag.putInt(TICKS_TAG_KEY, this.ticksToWait);
    return tag;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    PathCheckpoint that = (PathCheckpoint) o;
    return this.stop == that.stop && this.ticksToWait == that.ticksToWait && this.pos.equals(that.pos);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.pos, this.stop, this.ticksToWait);
  }

  @Override
  public PathCheckpoint clone() {
    try {
      return (PathCheckpoint) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return String.format("PathCheckpoint{pos=%s,stop=%b,wait=%d}", this.pos, this.stop, this.ticksToWait);
  }
}
