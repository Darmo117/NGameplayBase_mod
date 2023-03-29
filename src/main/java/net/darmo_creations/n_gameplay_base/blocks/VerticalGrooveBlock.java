package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.function.Function;

/**
 * A decorative block representing a sort of groove oriented along the y axis.
 */
public class VerticalGrooveBlock extends WaterloggableHorizontalFacingBlock implements Colored {
  public static final EnumProperty<DoorHinge> SIDE = EnumProperty.of("side", DoorHinge.class);

  private static final VoxelShape NORTH_LEFT_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 0, 0, 8, 16, 4),
      createCuboidShape(1, 0, 4, 3, 16, 8),
      createCuboidShape(5, 0, 4, 7, 16, 8));
  private static final VoxelShape NORTH_RIGHT_SHAPE = VoxelShapes.union(
      createCuboidShape(8, 0, 0, 16, 16, 4),
      createCuboidShape(9, 0, 4, 11, 16, 8),
      createCuboidShape(13, 0, 4, 15, 16, 8));
  private static final VoxelShape SOUTH_LEFT_SHAPE = VoxelShapes.union(
      createCuboidShape(8, 0, 12, 16, 16, 16),
      createCuboidShape(9, 0, 8, 11, 16, 12),
      createCuboidShape(13, 0, 8, 15, 16, 12));
  private static final VoxelShape SOUTH_RIGHT_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 0, 12, 8, 16, 16),
      createCuboidShape(1, 0, 8, 3, 16, 12),
      createCuboidShape(5, 0, 8, 7, 16, 12));
  private static final VoxelShape WEST_LEFT_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 0, 8, 4, 16, 16),
      createCuboidShape(4, 0, 9, 8, 16, 11),
      createCuboidShape(4, 0, 13, 8, 16, 15));
  private static final VoxelShape WEST_RIGHT_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 0, 0, 4, 16, 8),
      createCuboidShape(4, 0, 1, 8, 16, 3),
      createCuboidShape(4, 0, 5, 8, 16, 7));
  private static final VoxelShape EAST_LEFT_SHAPE = VoxelShapes.union(
      createCuboidShape(12, 0, 0, 16, 16, 8),
      createCuboidShape(8, 0, 1, 12, 16, 3),
      createCuboidShape(8, 0, 5, 12, 16, 7));
  private static final VoxelShape EAST_RIGHT_SHAPE = VoxelShapes.union(
      createCuboidShape(12, 0, 8, 16, 16, 16),
      createCuboidShape(8, 0, 9, 12, 16, 11),
      createCuboidShape(8, 0, 13, 12, 16, 15));

  private final BlockColor color;

  public VerticalGrooveBlock(final BlockColor color) {
    super(FabricBlockSettings.of(Material.STONE, color.getMapColor()).sounds(BlockSoundGroup.STONE),
        shapeProvider(NORTH_LEFT_SHAPE, NORTH_RIGHT_SHAPE),
        shapeProvider(EAST_LEFT_SHAPE, EAST_RIGHT_SHAPE),
        shapeProvider(SOUTH_LEFT_SHAPE, SOUTH_RIGHT_SHAPE),
        shapeProvider(WEST_LEFT_SHAPE, WEST_RIGHT_SHAPE));
    this.color = color;
    this.setDefaultState(this.getDefaultState().with(SIDE, DoorHinge.LEFT));
  }

  private static Function<BlockState, VoxelShape> shapeProvider(final VoxelShape leftShape, final VoxelShape rightShape) {
    return state -> state.get(SIDE) == DoorHinge.LEFT ? leftShape : rightShape;
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder.add(SIDE));
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    BlockState state = super.getPlacementState(ctx);
    BlockPos pos = ctx.getBlockPos();
    double xHit = ctx.getHitPos().x - pos.getX();
    double zHit = ctx.getHitPos().z - pos.getZ();
    //noinspection ConstantConditions
    DoorHinge side = switch (state.get(FACING)) {
      case NORTH -> xHit < 0.5 ? DoorHinge.LEFT : DoorHinge.RIGHT;
      case SOUTH -> xHit < 0.5 ? DoorHinge.RIGHT : DoorHinge.LEFT;
      case WEST -> zHit < 0.5 ? DoorHinge.RIGHT : DoorHinge.LEFT;
      case EAST -> zHit < 0.5 ? DoorHinge.LEFT : DoorHinge.RIGHT;
      default -> throw new IllegalStateException();
    };
    return state.with(SIDE, side);
  }

  @Override
  public BlockColor getColor() {
    return this.color;
  }
}
