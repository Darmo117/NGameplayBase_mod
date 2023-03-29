package net.darmo_creations.n_gameplay_base.blocks;

import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Direction;

/**
 * Types of vertical slabs.
 */
public enum VerticalSlabType implements StringIdentifiable {
  NORTH("north", Direction.NORTH),
  SOUTH("south", Direction.SOUTH),
  EAST("east", Direction.EAST),
  WEST("west", Direction.WEST),
  DOUBLE("double", null);

  private final String name;
  private final Direction direction;

  VerticalSlabType(final String name, Direction direction) {
    this.name = name;
    this.direction = direction;
  }

  /**
   * Returns the direction associated to this type.
   */
  public Direction getDirection() {
    return this.direction;
  }

  /**
   * Applies the given rotation to this slab type.
   *
   * @param rotation Rotation to apply.
   * @return Resulting slab type.
   */
  public VerticalSlabType rotate(BlockRotation rotation) {
    if (this.getDirection() == null) {
      return this;
    }
    return forDirection(rotation.rotate(this.getDirection()));
  }

  /**
   * Applies the given mirror transformation to this slab type.
   *
   * @param mirror Mirror transformation to apply.
   * @return Resulting slab type.
   */
  public VerticalSlabType mirror(BlockMirror mirror) {
    if (this.getDirection() == null) {
      return this;
    }
    return forDirection(mirror.apply(this.getDirection()));
  }

  @Override
  public String asString() {
    return this.name;
  }

  @Override
  public String toString() {
    return this.asString();
  }

  /**
   * Returns the slab type for the given horizontal direction.
   *
   * @param direction A horizontal direction.
   * @return The corresponding slab type or null if the direction is vertical.
   */
  public static VerticalSlabType forDirection(Direction direction) {
    return switch (direction) {
      case NORTH -> NORTH;
      case SOUTH -> SOUTH;
      case WEST -> WEST;
      case EAST -> EAST;
      default -> null;
    };
  }
}
