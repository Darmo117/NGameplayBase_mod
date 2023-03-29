package net.darmo_creations.n_gameplay_base.blocks;

import net.minecraft.block.MapColor;
import net.minecraft.util.StringIdentifiable;

/**
 * Enumeration of all available NaissanceE block colors.
 */
public enum BlockColor implements StringIdentifiable {
  WHITE("white", MapColor.WHITE),
  LIGHT_GRAY("light_gray", MapColor.LIGHT_GRAY),
  GRAY("gray", MapColor.GRAY),
  BLACK("black", MapColor.BLACK);

  private final String name;
  private final MapColor mapColor;

  BlockColor(final String name, final MapColor mapColor) {
    this.name = name;
    this.mapColor = mapColor;
  }

  /**
   * Returns the map color for this block color.
   */
  public MapColor getMapColor() {
    return this.mapColor;
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
