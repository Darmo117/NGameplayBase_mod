package net.darmo_creations.n_gameplay_base;

import net.darmo_creations.n_gameplay_base.block_entities.ModBlockEntities;
import net.darmo_creations.n_gameplay_base.blocks.BlockColor;
import net.darmo_creations.n_gameplay_base.blocks.ModBlocks;
import net.darmo_creations.n_gameplay_base.commands.ModArgumentTypes;
import net.darmo_creations.n_gameplay_base.commands.PushCommand;
import net.darmo_creations.n_gameplay_base.commands.SetColorCommand;
import net.darmo_creations.n_gameplay_base.commands.SetPassableCommand;
import net.darmo_creations.n_gameplay_base.dimensions.VoidDimensionEffects;
import net.darmo_creations.n_gameplay_base.items.ModItems;
import net.darmo_creations.n_gameplay_base.network.C2SPacketFactory;
import net.darmo_creations.n_gameplay_base.network.PacketRegistry;
import net.darmo_creations.n_gameplay_base.network.packets.LightOrbControllerDataPacket;
import net.darmo_creations.n_gameplay_base.sounds.ModSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.mixin.client.rendering.DimensionEffectsAccessor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mod’s main class. Common initializer for both client and server.
 */
public class NGameplayBase implements ModInitializer {
  public static final String MOD_ID = "n_gameplay_base";
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  // Creative mode’s item groups
  public static final ItemGroup BLOCKS_GROUP = FabricItemGroupBuilder.build(
      new Identifier(MOD_ID, "building"),
      () -> new ItemStack(ModBlocks.COLORED_LIGHT_SENSITIVE_BARRIERS.get(BlockColor.LIGHT_GRAY))
  );
  public static final ItemGroup TECHNICAL_GROUP = FabricItemGroupBuilder.build(
      new Identifier(MOD_ID, "technical"),
      () -> new ItemStack(ModItems.LIGHT_ORB_TWEAKER)
  );
  public static final ItemGroup CREATURES_GROUP = FabricItemGroupBuilder.build(
      new Identifier(MOD_ID, "creatures"),
      () -> new ItemStack(ModBlocks.LIVING_BLOCK)
  );

  public static final Identifier VOID_DIMENSION_EFFECTS_KEY = new Identifier(MOD_ID, "void");

  @SuppressWarnings("UnstableApiUsage")
  @Override
  public void onInitialize() {
    ModArgumentTypes.registerAll();
    ModBlocks.init();
    ModItems.init();
    ModBlockEntities.init();
    ModSounds.init();
    // Inject custom dimension effects. Custom dimension and dimension type are added through datapack.
    DimensionEffectsAccessor.getIdentifierMap().put(VOID_DIMENSION_EFFECTS_KEY, new VoidDimensionEffects());
    this.registerServerPacketHandlers();
    this.registerCommands();
  }

  /**
   * Registers all packets and associated handlers.
   */
  private void registerServerPacketHandlers() {
    PacketRegistry.registerPacket(
        C2SPacketFactory.LIGHT_ORB_CONTROLLER_DATA_PACKET_ID,
        LightOrbControllerDataPacket.class,
        new LightOrbControllerDataPacket.ServerHandler()
    );
  }

  /**
   * Registers all custom commands.
   */
  private void registerCommands() {
    CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
      SetPassableCommand.register(dispatcher);
      SetColorCommand.register(dispatcher);
      PushCommand.register(dispatcher);
    });
  }
}
