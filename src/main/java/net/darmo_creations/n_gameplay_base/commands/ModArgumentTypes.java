package net.darmo_creations.n_gameplay_base.commands;

import net.darmo_creations.n_gameplay_base.NGameplayBase;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;

public final class ModArgumentTypes {
  public static void registerAll() {
    ArgumentTypeRegistry.registerArgumentType(
        new Identifier(NGameplayBase.MOD_ID, "block_color"),
        BlockColorTypeArgument.class,
        ConstantArgumentSerializer.of(BlockColorTypeArgument::create)
    );
  }

  private ModArgumentTypes() {
  }
}
