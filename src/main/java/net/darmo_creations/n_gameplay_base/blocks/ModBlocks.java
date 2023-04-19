package net.darmo_creations.n_gameplay_base.blocks;

import net.darmo_creations.n_gameplay_base.NGameplayBase;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.function.Function;

/**
 * Declares all blocks added by this mod.
 */
@SuppressWarnings("unused")
public final class ModBlocks {
  public static final ColoredBlockMap<ColoredBlock> COLORED_BLOCKS =
      generateAndRegisterColoredBlocks("%s_block", ColoredBlock::new);
  public static final ColoredBlockMap<ColoredStairsBlock> COLORED_STAIRS =
      generateAndRegisterColoredBlocks("%s_stairs", color -> new ColoredStairsBlock(COLORED_BLOCKS.get(color)));
  public static final ColoredBlockMap<ColoredSlabBlock> COLORED_SLABS =
      generateAndRegisterColoredBlocks("%s_slab", ColoredSlabBlock::new);
  public static final ColoredBlockMap<ColoredVerticalSlabBlock> COLORED_VERTICAL_SLABS =
      generateAndRegisterColoredBlocks("%s_vslab", ColoredVerticalSlabBlock::new);
  public static final ColoredBlockMap<ColoredHorizontalQuarterBlock> COLORED_HORIZONTAL_QUARTER_BLOCKS =
      generateAndRegisterColoredBlocks("%s_horizontal_quarter_block", ColoredHorizontalQuarterBlock::new);
  public static final ColoredBlockMap<ColoredVerticalQuarterBlock> COLORED_VERTICAL_QUARTER_BLOCKS =
      generateAndRegisterColoredBlocks("%s_vertical_quarter_block", ColoredVerticalQuarterBlock::new);
  public static final ColoredBlockMap<PostBlock> COLORED_POSTS =
      generateAndRegisterColoredBlocks("%s_post", PostBlock::new);
  public static final ColoredBlockMap<ThickPostBlock> COLORED_THICK_POSTS =
      generateAndRegisterColoredBlocks("%s_thick_post", ThickPostBlock::new);
  public static final ColoredBlockMap<ColoredWallBlock> COLORED_WALLS =
      generateAndRegisterColoredBlocks("%s_wall", ColoredWallBlock::new);
  public static final ColoredBlockMap<ColoredCornerBlock> CORNER_BLOCKS =
      generateAndRegisterColoredBlocks("%s_corner_block", ColoredCornerBlock::new);
  public static final ColoredBlockMap<ColoredCompositeBlock> COMPOSITE_BLOCKS =
      generateAndRegisterColoredBlocks("%s_composite_block", ColoredCompositeBlock::new);
  public static final ColoredBlockMap<VerticalPipesBlock> VERTICAL_PIPES =
      generateAndRegisterColoredBlocks("%s_vertical_pipes", VerticalPipesBlock::new);
  public static final ColoredBlockMap<HorizontalPipesBlock> HORIZONTAL_PIPES =
      generateAndRegisterColoredBlocks("%s_horizontal_pipes", HorizontalPipesBlock::new);
  public static final ColoredBlockMap<VentGridBlock> VENT_GRID_BLOCKS =
      generateAndRegisterColoredBlocks("%s_vent_grid", VentGridBlock::new);
  public static final ColoredBlockMap<VerticalGrooveBlock> VERTICAL_GROOVE_BLOCKS =
      generateAndRegisterColoredBlocks("%s_vertical_groove", VerticalGrooveBlock::new);
  public static final ColoredBlockMap<HorizontalGrooveBlock> HORIZONTAL_GROOVE_BLOCKS =
      generateAndRegisterColoredBlocks("%s_horizontal_groove", HorizontalGrooveBlock::new);
  public static final ColoredBlockMap<VerticalGrooveCornerBlock> VERTICAL_GROOVE_CORNER_BLOCKS =
      generateAndRegisterColoredBlocks("%s_vertical_groove_corner", VerticalGrooveCornerBlock::new);
  public static final ColoredBlockMap<HorizontalGrooveCornerBlock> HORIZONTAL_GROOVE_CORNER_BLOCKS =
      generateAndRegisterColoredBlocks("%s_horizontal_groove_corner", HorizontalGrooveCornerBlock::new);

  // Light-sensitive blocks
  public static final ColoredBlockMap<LightSensitiveBarrierBlock> COLORED_LIGHT_SENSITIVE_BARRIERS =
      generateAndRegisterColoredBlocks("%s_light_sensitive_barrier", color -> new LightSensitiveBarrierBlock(color, false));
  public static final ColoredBlockMap<LightSensitiveBarrierBlock> COLORED_LIGHT_SENSITIVE_BARRIERS_PASSABLE =
      generateAndRegisterColoredBlocks("%s_light_sensitive_barrier_passable", color -> new LightSensitiveBarrierBlock(color, true));

  // Light-sensitive slabs
  public static final ColoredBlockMap<LightSensitiveBarrierSlabBlock> COLORED_LIGHT_SENSITIVE_BARRIER_SLABS =
      generateAndRegisterColoredBlocks("%s_light_sensitive_barrier_slab", color -> new LightSensitiveBarrierSlabBlock(color, false));
  public static final ColoredBlockMap<LightSensitiveBarrierSlabBlock> COLORED_LIGHT_SENSITIVE_BARRIER_SLABS_PASSABLE =
      generateAndRegisterColoredBlocks("%s_light_sensitive_barrier_slab_passable", color -> new LightSensitiveBarrierSlabBlock(color, true));

  // Light-sensitive vertical slabs
  public static final ColoredBlockMap<LightSensitiveBarrierVerticalSlabBlock> COLORED_LIGHT_SENSITIVE_BARRIER_VSLABS =
      generateAndRegisterColoredBlocks("%s_light_sensitive_barrier_vslab", color -> new LightSensitiveBarrierVerticalSlabBlock(color, false));
  public static final ColoredBlockMap<LightSensitiveBarrierVerticalSlabBlock> COLORED_LIGHT_SENSITIVE_BARRIER_VSLABS_PASSABLE =
      generateAndRegisterColoredBlocks("%s_light_sensitive_barrier_vslab_passable", color -> new LightSensitiveBarrierVerticalSlabBlock(color, true));

  // Lights
  public static final FloatingVariableLightBlock FLOATING_VARIABLE_LIGHT_BLOCK =
      register("floating_variable_light_block", new FloatingVariableLightBlock(), NGameplayBase.TECHNICAL_GROUP);
  public static final BlockActivatorLamp ACTIVATOR_LAMP = register("activator_lamp", new BlockActivatorLamp(), NGameplayBase.TECHNICAL_GROUP);
  public static final SmallLightsBlock SMALL_LIGHTS = register("small_lights", new SmallLightsBlock());
  public static final SmallLightBlock SMALL_LIGHT = register("small_light", new SmallLightBlock());
  public static final SmallOffsetLightBlock SMALL_OFFSET_LIGHT = register("small_offset_light", new SmallOffsetLightBlock());

  public static final Block[] LIGHT_BLOCKS = new Block[15];

  static {
    for (int i = 1; i < 16; i++) {
      LIGHT_BLOCKS[i - 1] = register(
          "light_block_" + i,
          new Block(FabricBlockSettings.of(Material.STONE, MapColor.WHITE).luminance(i))
      );
    }
  }

  public static final SlabBlock LIGHT_SLAB =
      register("light_slab", new SlabBlock(FabricBlockSettings.of(Material.STONE, MapColor.WHITE).luminance(15)));
  public static final VerticalSlabBlock LIGHT_BLOCK_VSLAB =
      register("light_vslab", new VerticalSlabBlock(FabricBlockSettings.of(Material.STONE, MapColor.WHITE).luminance(15)));
  public static final StairsBlock LIGHT_STAIRS =
      register("light_stairs", new StairsBlock(LIGHT_BLOCKS[14].getDefaultState(), FabricBlockSettings.of(Material.STONE, MapColor.WHITE).luminance(15)));
  public static final HorizontalQuarterBlock LIGHT_HORIZONTAL_QUARTER_BLOCK =
      register("light_horizontal_quarter_block", new HorizontalQuarterBlock(FabricBlockSettings.of(Material.STONE, MapColor.WHITE).luminance(15)));
  public static final VerticalQuarterBlock LIGHT_VERTICAL_QUARTER_BLOCK =
      register("light_vertical_quarter_block", new VerticalQuarterBlock(FabricBlockSettings.of(Material.STONE, MapColor.WHITE).luminance(15)));
  public static final CornerBlock LIGHT_CORNER_BLOCK =
      register("light_corner_block", new CornerBlock(FabricBlockSettings.of(Material.STONE, MapColor.WHITE).luminance(15)));
  public static final CompositeBlock LIGHT_COMPOSITE_BLOCK =
      register("light_composite_block", new CompositeBlock(FabricBlockSettings.of(Material.STONE, MapColor.WHITE).luminance(15)));

  public static final LightBlockerBlock LIGHT_BLOCKER = register("light_blocker", new LightBlockerBlock());

  // Ladders
  public static final ColoredBlockMap<LadderBlockWithoutSupport> COLORED_LADDERS =
      generateAndRegisterColoredBlocks("%s_ladder", LadderBlockWithoutSupport::new);
  public static final ColoredBlockMap<HalfLadderBlock> COLORED_HALF_LADDERS =
      generateAndRegisterColoredBlocks("%s_half_ladder", HalfLadderBlock::new);

  // Doors
  public static final ColoredBlockMap<PlainDoorBlock> COLORED_DOORS =
      generateAndRegisterColoredBlocks("%s_door", color -> new PlainDoorBlock(color, true), NGameplayBase.TECHNICAL_GROUP);
  public static final ColoredBlockMap<PlainDoorBlock> COLORED_PARTIAL_DOORS =
      generateAndRegisterColoredBlocks("%s_partial_door", color -> new PlainDoorBlock(color, false), NGameplayBase.TECHNICAL_GROUP);
  public static final ColoredBlockMap<ColoredDoorFrame> COLORED_DOOR_FRAMES =
      generateAndRegisterColoredBlocks("%s_door_frame", ColoredDoorFrame::new, NGameplayBase.TECHNICAL_GROUP);
  public static final ColoredBlockMap<ColoredDoorFrameTop> COLORED_DOOR_FRAME_TOPS =
      generateAndRegisterColoredBlocks("%s_door_frame_top", ColoredDoorFrameTop::new, NGameplayBase.TECHNICAL_GROUP);

  // Creatures
  public static final LivingBlock LIVING_BLOCK = register("living_block", new LivingBlock(), NGameplayBase.CREATURES_GROUP);
  public static final Block[] CREATURE_BLOCKS = new Block[16];

  static {
    for (int i = 0; i < 16; i++) {
      CREATURE_BLOCKS[i] = register(
          "creature_block_" + i,
          new Block(FabricBlockSettings.of(Material.STONE, MapColor.WHITE).luminance(i)),
          NGameplayBase.CREATURES_GROUP
      );
    }
  }

  public static final LightOrbControllerBlock LIGHT_ORB_CONTROLLER =
      register("light_orb_controller", new LightOrbControllerBlock(), NGameplayBase.TECHNICAL_GROUP);
  public static final LightOrbSourceBlock LIGHT_ORB_SOURCE =
      register("light_orb_source", new LightOrbSourceBlock(), null);

  public static final WindControllerBlock WIND_CONTROLLER =
      register("wind_controller", new WindControllerBlock(), NGameplayBase.TECHNICAL_GROUP);

  /**
   * Instanciates then registers a block for each {@link BlockColor} and puts them in the mod’s “Blocks” item group.
   *
   * @param namePattern  Pattern for the block’s name into which to insert the color name.
   * @param blockFactory A factory that returns a block instance for the given color.
   * @param <T>          Concrete type of generated blocks.
   * @return A map associating a block instance to each {@link BlockColor}.
   */
  private static <T extends Block> ColoredBlockMap<T> generateAndRegisterColoredBlocks(
      final String namePattern,
      final Function<BlockColor, T> blockFactory
  ) {
    return generateAndRegisterColoredBlocks(namePattern, blockFactory, NGameplayBase.BLOCKS_GROUP);
  }

  /**
   * Instanciates then registers a block for each {@link BlockColor}, and puts them in the given item group.
   *
   * @param namePattern  Pattern for the block’s name into which to insert the color name.
   * @param blockFactory A factory that returns a block instance for the given color.
   * @param itemGroup    Item group to put the blocks in.
   * @param <T>          Concrete type of generated blocks.
   * @return A map associating a block instance to each {@link BlockColor}.
   */
  private static <T extends Block> ColoredBlockMap<T> generateAndRegisterColoredBlocks(
      final String namePattern,
      final Function<BlockColor, T> blockFactory,
      final ItemGroup itemGroup
  ) {
    ColoredBlockMap<T> blocks = new ColoredBlockMap<>(BlockColor.class);
    Arrays.stream(BlockColor.values()).forEach(color -> {
      T block = blockFactory.apply(color);
      register(namePattern.formatted(color.asString()), block, itemGroup);
      blocks.put(color, block);
    });
    return blocks;
  }

  /**
   * Registers a block and puts it in the mod’s “Blocks” item group.
   *
   * @param name  Block’s name.
   * @param block Block to register.
   * @param <T>   Type of the block to register.
   * @return The registered block.
   */
  private static <T extends Block> T register(final String name, final T block) {
    return register(name, block, NGameplayBase.BLOCKS_GROUP);
  }

  /**
   * Registers a block and its item, and puts it in the given item group.
   *
   * @param name      Block’s name.
   * @param block     Block to register.
   * @param itemGroup Item group to put the block in.
   * @param <T>       Type of the block to register.
   * @return The registered block.
   */
  private static <T extends Block> T register(final String name, final T block, final ItemGroup itemGroup) {
    Registry.register(Registry.BLOCK, new Identifier(NGameplayBase.MOD_ID, name), block);
    if (itemGroup != null) {
      BlockItem blockItem = new BlockItem(block, new FabricItemSettings().group(itemGroup));
      Registry.register(Registry.ITEM, new Identifier(NGameplayBase.MOD_ID, name), blockItem);
    }
    return block;
  }

  /**
   * Initializes various properties on blocks.
   * <p>
   * Must be called on both clients and server.
   */
  public static void init() {
    COLORED_LIGHT_SENSITIVE_BARRIERS.forEach(
        (color, block) -> block.setCounterpartBlock(COLORED_LIGHT_SENSITIVE_BARRIERS_PASSABLE.get(color)));
    COLORED_LIGHT_SENSITIVE_BARRIER_SLABS.forEach(
        (color, block) -> block.setCounterpartBlock(COLORED_LIGHT_SENSITIVE_BARRIER_SLABS_PASSABLE.get(color)));
    COLORED_LIGHT_SENSITIVE_BARRIER_VSLABS.forEach(
        (color, block) -> block.setCounterpartBlock(COLORED_LIGHT_SENSITIVE_BARRIER_VSLABS_PASSABLE.get(color)));
  }

  /**
   * Registers render layers for some block.
   * <p>
   * Must be called on client only.
   */
  public static void registerBlockRenderLayers() {
    COLORED_LIGHT_SENSITIVE_BARRIERS_PASSABLE.values()
        .forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getTranslucent()));
    COLORED_LIGHT_SENSITIVE_BARRIER_SLABS_PASSABLE.values()
        .forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getTranslucent()));
    COLORED_LIGHT_SENSITIVE_BARRIER_VSLABS_PASSABLE.values()
        .forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getTranslucent()));
  }

  private ModBlocks() {
  }
}
