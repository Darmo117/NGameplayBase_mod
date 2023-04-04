package net.darmo_creations.n_gameplay_base.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.darmo_creations.n_gameplay_base.blocks.BlockColor;
import net.minecraft.command.CommandSource;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Command argument type for {@link BlockColor}s.
 */
public class BlockColorTypeArgument implements ArgumentType<String> {
  public static BlockColorTypeArgument create() {
    return new BlockColorTypeArgument();
  }

  public static Optional<BlockColor> getColor(final CommandContext<?> context, final String argumentName) {
    String name = context.getArgument(argumentName, String.class);
    for (BlockColor color : BlockColor.values()) {
      if (color.asString().equals(name)) {
        return Optional.of(color);
      }
    }
    return Optional.empty();
  }

  @Override
  public String parse(StringReader reader) {
    return reader.readUnquotedString();
  }

  @Override
  public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
    return CommandSource
        .suggestMatching(
            Arrays.stream(BlockColor.values())
                .map(BlockColor::asString)
                .toList(),
            builder
        );
  }
}
