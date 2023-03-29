package net.darmo_creations.n_gameplay_base.blocks;

import net.minecraft.block.Block;

import java.util.EnumMap;

/**
 * Wrapper around the {@link EnumMap} class with keys of type {@link BlockColor}.
 *
 * @param <T> Type of values.
 */
public class ColoredBlockMap<T extends Block> extends EnumMap<BlockColor, T> {
  public ColoredBlockMap(final Class<BlockColor> keyType) {
    super(keyType);
  }
}
