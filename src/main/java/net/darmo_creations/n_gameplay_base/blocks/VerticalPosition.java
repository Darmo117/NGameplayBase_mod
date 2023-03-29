package net.darmo_creations.n_gameplay_base.blocks;

import net.minecraft.util.StringIdentifiable;

/**
 * Enumeration of all possible vertical positions for some blocks.
 */
@SuppressWarnings("unused")
public enum VerticalPosition implements StringIdentifiable {
  TOP("top"),
  MIDDLE("middle"),
  BOTTOM("bottom");

  private final String name;

  VerticalPosition(final String name) {
    this.name = name;
  }

  @Override
  public String asString() {
    return this.name;
  }

  @Override
  public String toString() {
    return this.asString();
  }
}
