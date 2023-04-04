package net.darmo_creations.n_gameplay_base.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;

/**
 * Helper class that performs an operation on all blocks in a given block area.
 *
 * @param <T> Type of the command’s final value.
 */
class AreaCommandHelper<T> {
  private static final int MAX_VOLUME = 32768;

  private final String commandName;
  private final Dynamic2CommandExceptionType tooBigException;
  private final SimpleCommandExceptionType failedException;
  private final SimpleCommandExceptionType invalidValueException;
  private final Function<CommandContext<ServerCommandSource>, Optional<T>> valueProvider;
  private final BlockStateVariantMap.QuadFunction<BlockState, ServerWorld, BlockPos, T, Boolean> applyChanges;

  /**
   * Create a command helper.
   *
   * @param commandName   Name of the associated command.
   * @param valueProvider Function that provides a value based on the given context.
   * @param applyChanges  Function that applies an operation on every block within the command’s area.
   *                      Should return true if the operation could be applied to the given block, false otherwise.
   */
  public AreaCommandHelper(
      String commandName,
      Function<CommandContext<ServerCommandSource>, Optional<T>> valueProvider,
      BlockStateVariantMap.QuadFunction<BlockState, ServerWorld, BlockPos, T, Boolean> applyChanges
  ) {
    this.commandName = commandName;
    this.tooBigException =
        new Dynamic2CommandExceptionType((maxCount, count) -> MutableText.of(new TranslatableTextContent(
            "commands.%s.too_big".formatted(commandName), maxCount, count)));
    this.failedException =
        new SimpleCommandExceptionType(MutableText.of(new TranslatableTextContent(
            "commands.%s.failed".formatted(commandName))));
    this.invalidValueException =
        new SimpleCommandExceptionType(MutableText.of(new TranslatableTextContent(
            "commands.%s.invalid_value".formatted(commandName))));
    this.valueProvider = valueProvider;
    this.applyChanges = applyChanges;
  }

  /**
   * Execute this helper on the given command context.
   */
  int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
    BlockBox range = BlockBox.create(
        BlockPosArgumentType.getLoadedBlockPos(context, "from"),
        BlockPosArgumentType.getLoadedBlockPos(context, "to")
    );
    int volume = range.getBlockCountX() * range.getBlockCountY() * range.getBlockCountZ();
    if (volume > MAX_VOLUME) {
      throw this.tooBigException.create(MAX_VOLUME, volume);
    }
    Optional<T> value = this.valueProvider.apply(context);
    if (value.isEmpty()) {
      throw this.invalidValueException.create();
    }

    ArrayList<BlockPos> list = Lists.newArrayList();
    ServerCommandSource source = context.getSource();
    ServerWorld serverWorld = source.getWorld();

    int nb = 0;
    for (BlockPos blockPos : BlockPos.iterate(range.getMinX(), range.getMinY(), range.getMinZ(), range.getMaxX(), range.getMaxY(), range.getMaxZ())) {
      BlockState state = serverWorld.getBlockState(blockPos);
      if (this.applyChanges.apply(state, serverWorld, blockPos, value.get())) {
        list.add(blockPos.toImmutable());
        nb++;
      }
    }
    for (BlockPos blockPos : list) {
      serverWorld.updateNeighbors(blockPos, serverWorld.getBlockState(blockPos).getBlock());
    }
    if (nb == 0) {
      throw this.failedException.create();
    }

    source.sendFeedback(MutableText.of(new TranslatableTextContent("commands.%s.success".formatted(this.commandName), nb)), true);
    return nb;
  }
}
