package net.darmo_creations.n_gameplay_base.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.darmo_creations.n_gameplay_base.blocks.BlockColor;
import net.darmo_creations.n_gameplay_base.blocks.Colored;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

/**
 * A command that changes the color of blocks in the given area that implement the {@link Colored} interface.
 */
public class SetColorCommand {
  public static final AreaCommandHelper<BlockColor> HELPER = new AreaCommandHelper<>(
      "setcolor",
      context -> BlockColorTypeArgument.getColor(context, "color"),
      (state, serverWorld, blockPos, color)
          -> state.getBlock() instanceof Colored lsb && lsb.setColor(state, serverWorld, blockPos, color)
  );

  /**
   * Registers this command.
   */
  public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    dispatcher.register(CommandManager.literal("setcolor")
        .requires(source -> source.hasPermissionLevel(2))
        .then(CommandManager.argument("from", BlockPosArgumentType.blockPos())
            .then(CommandManager.argument("to", BlockPosArgumentType.blockPos())
                .then(CommandManager.argument("color", BlockColorTypeArgument.create())
                    .executes(HELPER::execute)))));
  }
}
