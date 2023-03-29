package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

/**
 * A centered door of a single color. May only partially close.
 */
public class PlainDoorBlock extends DoorBlock implements Colored {
  private static final VoxelShape TOP_OPEN_X = createCuboidShape(6, 14, 0, 10, 16, 16);
  private static final VoxelShape TOP_OPEN_Z = createCuboidShape(0, 14, 6, 16, 16, 10);
  private static final VoxelShape CLOSED_X = createCuboidShape(6, 0, 0, 10, 16, 16);
  private static final VoxelShape CLOSED_Z = createCuboidShape(0, 0, 6, 16, 16, 10);
  private static final VoxelShape PARTIALLY_CLOSED_X = createCuboidShape(6, 8, 0, 10, 16, 16);
  private static final VoxelShape PARTIALLY_CLOSED_Z = createCuboidShape(0, 8, 6, 16, 16, 10);

  private final BlockColor color;
  private final boolean fullyCloses;

  /**
   * Creates a plain door.
   *
   * @param color       Door’s color.
   * @param fullyCloses Whether the door can fully close.
   */
  public PlainDoorBlock(final BlockColor color, final boolean fullyCloses) {
    // Must use metal material to have same behavior as vanilla’s iron door
    super(ModBlock.getSettings(FabricBlockSettings.of(Material.METAL, color.getMapColor()).sounds(BlockSoundGroup.STONE)));
    this.color = color;
    this.fullyCloses = fullyCloses;
  }

  @Override
  public BlockColor getColor() {
    return this.color;
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return state.get(OPEN) && state.get(HALF) == DoubleBlockHalf.LOWER
        ? BlockRenderType.INVISIBLE
        : BlockRenderType.MODEL;
  }

  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    boolean isOpen = state.get(OPEN);
    boolean isBottom = state.get(HALF) == DoubleBlockHalf.LOWER;
    Direction.Axis axis = state.get(FACING).getAxis();

    if (isOpen) {
      if (isBottom) {
        return VoxelShapes.empty();
      } else {
        return axis == Direction.Axis.X ? TOP_OPEN_X : TOP_OPEN_Z;
      }
    } else {
      if (isBottom && !this.fullyCloses) {
        return axis == Direction.Axis.X ? PARTIALLY_CLOSED_X : PARTIALLY_CLOSED_Z;
      } else {
        return axis == Direction.Axis.X ? CLOSED_X : CLOSED_Z;
      }
    }
  }
}
