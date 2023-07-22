package net.darmo_creations.n_gameplay_base.block_entities;

import net.darmo_creations.n_gameplay_base.NGameplayBase;
import net.darmo_creations.n_gameplay_base.block_entities.renderers.KillTriggerControllerBlockEntityRenderer;
import net.darmo_creations.n_gameplay_base.block_entities.renderers.LightOrbControllerBlockEntityRenderer;
import net.darmo_creations.n_gameplay_base.block_entities.renderers.SetSpawnTriggerControllerBlockEntityRenderer;
import net.darmo_creations.n_gameplay_base.block_entities.renderers.WindControllerBlockEntityRenderer;
import net.darmo_creations.n_gameplay_base.blocks.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * Declares all block entity types added by this mod.
 */
public final class ModBlockEntities {
  public static final BlockEntityType<FloatingVariableLightBlockEntity> FLOATING_VARIABLE_LIGHT_BLOCK =
      register("floating_variable_light", FloatingVariableLightBlockEntity::new, ModBlocks.FLOATING_VARIABLE_LIGHT_BLOCK);

  // Controllers
  public static final BlockEntityType<LightOrbControllerBlockEntity> LIGHT_ORB_CONTROLLER =
      register("light_orb_controller", LightOrbControllerBlockEntity::new, ModBlocks.LIGHT_ORB_CONTROLLER);
  public static final BlockEntityType<WindControllerBlockEntity> WIND_CONTROLLER =
      register("wind_controller", WindControllerBlockEntity::new, ModBlocks.WIND_CONTROLLER);
  public static final BlockEntityType<KillTriggerControllerBlockEntity> KILL_TRIGGER_CONTROLLER =
      register("kill_trigger_controller", KillTriggerControllerBlockEntity::new, ModBlocks.KILL_TRIGGER_CONTROLLER);
  public static final BlockEntityType<SetSpawnTriggerControllerBlockEntity> SET_SPAWN_TRIGGER_CONTROLLER =
      register("set_spawn_trigger_controller", SetSpawnTriggerControllerBlockEntity::new, ModBlocks.SET_SPAWN_TRIGGER_CONTROLLER);

  /**
   * Registers a block entity type.
   *
   * @param name    Block entity’s name.
   * @param factory A factory for the block entity type.
   * @param blocks  Block to associate to the block entity.
   * @param <T>     Type of the block entity type.
   * @param <U>     Type of the associated block entity.
   * @return The registered block entity type.
   */
  @SuppressWarnings("unchecked")
  private static <T extends BlockEntityType<U>, U extends BlockEntity> T register(
      final String name, FabricBlockEntityTypeBuilder.Factory<U> factory, final Block... blocks
  ) {
    return (T) Registry.register(
        Registry.BLOCK_ENTITY_TYPE,
        new Identifier(NGameplayBase.MOD_ID, name),
        FabricBlockEntityTypeBuilder.create(factory, blocks).build()
    );
  }

  /**
   * Dummy method called from {@link NGameplayBase#onInitialize()} to register block entity types:
   * it forces the class to be loaded during mod initialization, while the registries are unlocked.
   * <p>
   * Must be called on both clients and server.
   */
  public static void init() {
  }

  /**
   * Registers block entity renderers.
   * <p>
   * Must be called on client only.
   */
  public static void registerRenderers() {
    BlockEntityRendererFactories.register(LIGHT_ORB_CONTROLLER, LightOrbControllerBlockEntityRenderer::new);
    BlockEntityRendererFactories.register(WIND_CONTROLLER, WindControllerBlockEntityRenderer::new);
    BlockEntityRendererFactories.register(KILL_TRIGGER_CONTROLLER, KillTriggerControllerBlockEntityRenderer::new);
    BlockEntityRendererFactories.register(SET_SPAWN_TRIGGER_CONTROLLER, SetSpawnTriggerControllerBlockEntityRenderer::new);
  }

  private ModBlockEntities() {
  }
}
