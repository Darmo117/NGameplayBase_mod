package net.darmo_creations.n_gameplay_base.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.darmo_creations.n_gameplay_base.blocks.LightSensitiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

/**
 * This command can set the state of any light-sensitive barrier blocks in the specified volume.
 */
public class SetPassableCommand {
  private static final int MAX_VOLUME = 32768;
  private static final Dynamic2CommandExceptionType TOO_BIG_EXCEPTION =
      new Dynamic2CommandExceptionType((maxCount, count) -> MutableText.of(new TranslatableTextContent(
          "commands.setpassable.too_big", maxCount, count)));
  private static final SimpleCommandExceptionType FAILED_EXCEPTION =
      new SimpleCommandExceptionType(MutableText.of(new TranslatableTextContent(
          "commands.setpassable.failed")));

  /**
   * Registers this command.
   */
  public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    dispatcher.register(CommandManager.literal("setpassable")
        .requires(source -> source.hasPermissionLevel(2))
        .then(CommandManager.argument("from", BlockPosArgumentType.blockPos())
            .then(CommandManager.argument("to", BlockPosArgumentType.blockPos())
                .then(CommandManager.argument("passable", BoolArgumentType.bool())
                    .executes(SetPassableCommand::execute)))));
  }

  private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
    BlockBox range = BlockBox.create(
        BlockPosArgumentType.getLoadedBlockPos(context, "from"),
        BlockPosArgumentType.getLoadedBlockPos(context, "to")
    );
    int volume = range.getBlockCountX() * range.getBlockCountY() * range.getBlockCountZ();
    if (volume > MAX_VOLUME) {
      throw TOO_BIG_EXCEPTION.create(MAX_VOLUME, volume);
    }
    boolean passable = BoolArgumentType.getBool(context, "passable");

    ArrayList<BlockPos> list = Lists.newArrayList();
    ServerCommandSource source = context.getSource();
    ServerWorld serverWorld = source.getWorld();

    int nb = 0;
    for (BlockPos blockPos : BlockPos.iterate(range.getMinX(), range.getMinY(), range.getMinZ(), range.getMaxX(), range.getMaxY(), range.getMaxZ())) {
      BlockState state = serverWorld.getBlockState(blockPos);
      if (state.getBlock() instanceof LightSensitiveBlock<?> lsb && lsb.setPassable(state, serverWorld, blockPos, passable)) {
        list.add(blockPos.toImmutable());
        nb++;
      }
    }
    for (BlockPos blockPos : list) {
      serverWorld.updateNeighbors(blockPos, serverWorld.getBlockState(blockPos).getBlock());
    }
    if (nb == 0) {
      throw FAILED_EXCEPTION.create();
    }

    source.sendFeedback(MutableText.of(new TranslatableTextContent("commands.setpassable.success", nb)), true);
    return nb;
  }
}
