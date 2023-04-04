package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.shape.VoxelShape;

/**
 * A block acting as the top of a door frame.
 * A lamp is added when the player placing the block is sneaking.
 */
public class ColoredDoorFrameTop extends WaterloggableHorizontalFacingBlock implements Colored {
  public static final BooleanProperty LAMP = BooleanProperty.of("lamp");

  private static final VoxelShape NORTH_FRAME_SHAPE = createCuboidShape(-2, 0, 0, 18, 2, 2);
  private static final VoxelShape EAST_FRAME_SHAPE = createCuboidShape(14, 0, -2, 16, 2, 18);
  private static final VoxelShape SOUTH_FRAME_SHAPE = createCuboidShape(-2, 0, 14, 18, 2, 16);
  private static final VoxelShape WEST_FRAME_SHAPE = createCuboidShape(0, 0, -2, 2, 2, 18);

  public static final VoxelShape NORTH_LAMP_SHAPE = createCuboidShape(4, 4, 0, 12, 12, 2);
  public static final VoxelShape WEST_LAMP_SHAPE = createCuboidShape(0, 4, 4, 2, 12, 12);
  public static final VoxelShape SOUTH_LAMP_SHAPE = createCuboidShape(4, 4, 14, 12, 12, 16);
  public static final VoxelShape EAST_LAMP_SHAPE = createCuboidShape(14, 4, 4, 16, 12, 12);

  private final BlockColor color;

  /**
   * Create a door frame top of the given color.
   *
   * @param color Frame top’s color.
   */
  public ColoredDoorFrameTop(final BlockColor color) {
    super(FabricBlockSettings.of(Material.STONE, color.getMapColor())
            .sounds(BlockSoundGroup.STONE)
            .luminance(state -> state.get(LAMP) ? 15 : 0),
        state -> state.get(LAMP) ? NORTH_LAMP_SHAPE : NORTH_FRAME_SHAPE,
        state -> state.get(LAMP) ? EAST_LAMP_SHAPE : EAST_FRAME_SHAPE,
        state -> state.get(LAMP) ? SOUTH_LAMP_SHAPE : SOUTH_FRAME_SHAPE,
        state -> state.get(LAMP) ? WEST_LAMP_SHAPE : WEST_FRAME_SHAPE);
    this.color = color;
    this.setDefaultState(this.getDefaultState().with(LAMP, false));
  }

  @Override
  public BlockColor getColor() {
    return this.color;
  }

  @Override
  public BlockState getBlockStateForColor(BlockState state, BlockColor color) {
    return ModBlocks.COLORED_DOOR_FRAME_TOPS.get(color).getDefaultState()
        .with(FACING, state.get(FACING))
        .with(LAMP, state.get(LAMP))
        .with(WATERLOGGED, state.get(WATERLOGGED));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder.add(LAMP));
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    //noinspection ConstantConditions
    return super.getPlacementState(ctx).with(LAMP, ctx.getPlayer().isSneaking());
  }
}
