package net.darmo_creations.n_gameplay_base.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * A command that pushes the selected entities in the given direction.
 */
public class PushCommand {
  /**
   * Registers this command.
   */
  public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    dispatcher.register(CommandManager.literal("push")
        .requires(source -> source.hasPermissionLevel(2))
        .then(CommandManager.argument("target", EntityArgumentType.player())
            .then(CommandManager.argument("direction", Vec3ArgumentType.vec3(false))
                .executes(PushCommand::execute))));
  }

  private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
    ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "target");
    Vec3d direction = Vec3ArgumentType.getVec3(context, "direction");
    player.addVelocity(direction.x, direction.y, direction.z);
    player.velocityModified = true;
    return 1;
  }
}
