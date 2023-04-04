package net.darmo_creations.n_gameplay_base.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.darmo_creations.n_gameplay_base.blocks.LightSensitiveBlock;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Optional;

/**
 * This command can set the state of any light-sensitive barrier blocks in the specified volume.
 */
public class SetPassableCommand {
  public static final AreaCommandHelper<Boolean> HELPER = new AreaCommandHelper<>(
      "setpassable",
      context -> Optional.of(BoolArgumentType.getBool(context, "passable")),
      (state, serverWorld, blockPos, passable)
          -> state.getBlock() instanceof LightSensitiveBlock<?> lsb && lsb.setPassable(state, serverWorld, blockPos, passable)
  );

  /**
   * Registers this command.
   */
  public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    dispatcher.register(CommandManager.literal("setpassable")
        .requires(source -> source.hasPermissionLevel(2))
        .then(CommandManager.argument("from", BlockPosArgumentType.blockPos())
            .then(CommandManager.argument("to", BlockPosArgumentType.blockPos())
                .then(CommandManager.argument("passable", BoolArgumentType.bool())
                    .executes(HELPER::execute)))));
  }
}
