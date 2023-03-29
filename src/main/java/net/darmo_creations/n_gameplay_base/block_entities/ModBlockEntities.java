package net.darmo_creations.n_gameplay_base.block_entities;

import net.darmo_creations.n_gameplay_base.NGameplayBase;
import net.darmo_creations.n_gameplay_base.block_entities.renderers.LightOrbControllerBlockEntityRenderer;
import net.darmo_creations.n_gameplay_base.blocks.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Declares all block entity types added by this mod.
 */
public final class ModBlockEntities {
  public static final BlockEntityType<FloatingVariableLightBlockEntity> FLOATING_VARIABLE_LIGHT_BLOCK =
      register("floating_variable_light", FloatingVariableLightBlockEntity::new, ModBlocks.FLOATING_VARIABLE_LIGHT_BLOCK);
  public static final BlockEntityType<LightOrbControllerBlockEntity> LIGHT_ORB_CONTROLLER =
      register("light_orb_controller", LightOrbControllerBlockEntity::new, ModBlocks.LIGHT_ORB_CONTROLLER);

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
  private static <T extends BlockEntityType<U>, U extends BlockEntity> T register(
      final String name, FabricBlockEntityTypeBuilder.Factory<U> factory, final Block... blocks
  ) {
    //noinspection unchecked
    return (T) Registry.register(
        Registries.BLOCK_ENTITY_TYPE,
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
   * Must be called on client only.
   */
  public static void registerRenderers() {
    BlockEntityRendererFactories.register(LIGHT_ORB_CONTROLLER, LightOrbControllerBlockEntityRenderer::new);
  }

  private ModBlockEntities() {
  }
}
