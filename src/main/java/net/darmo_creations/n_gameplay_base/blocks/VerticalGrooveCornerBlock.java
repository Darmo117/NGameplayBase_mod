package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.function.Function;

public class VerticalGrooveCornerBlock extends WaterloggableHorizontalFacingBlock implements Colored {
  public static final EnumProperty<DoorHinge> SIDE = EnumProperty.of("side", DoorHinge.class);
  public static final EnumProperty<BlockHalf> HALF = Properties.BLOCK_HALF;

  private static final VoxelShape NORTH_LEFT_BOTTOM_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 0, 0, 8, 4, 4),
      createCuboidShape(1, 4, 0, 3, 8, 5),
      createCuboidShape(1, 0, 4, 3, 5, 8),
      createCuboidShape(1, 5, 5, 3, 7, 7),
      createCuboidShape(5, 4, 0, 7, 8, 5),
      createCuboidShape(5, 0, 4, 7, 5, 8),
      createCuboidShape(5, 5, 5, 7, 7, 7));
  private static final VoxelShape NORTH_RIGHT_BOTTOM_SHAPE = VoxelShapes.union(
      createCuboidShape(8, 0, 0, 16, 4, 4),
      createCuboidShape(9, 4, 0, 11, 8, 5),
      createCuboidShape(9, 0, 4, 11, 5, 8),
      createCuboidShape(9, 5, 5, 11, 7, 7),
      createCuboidShape(13, 4, 0, 15, 8, 5),
      createCuboidShape(13, 0, 4, 15, 5, 8),
      createCuboidShape(13, 5, 5, 15, 7, 7));
  private static final VoxelShape NORTH_LEFT_TOP_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 12, 0, 8, 16, 4),
      createCuboidShape(1, 8, 0, 3, 12, 5),
      createCuboidShape(1, 11, 4, 3, 16, 8),
      createCuboidShape(1, 9, 5, 3, 11, 7),
      createCuboidShape(5, 8, 0, 7, 12, 5),
      createCuboidShape(5, 11, 4, 7, 16, 8),
      createCuboidShape(5, 9, 5, 7, 11, 7));
  private static final VoxelShape NORTH_RIGHT_TOP_SHAPE = VoxelShapes.union(
      createCuboidShape(8, 12, 0, 16, 16, 4),
      createCuboidShape(9, 8, 0, 11, 12, 5),
      createCuboidShape(9, 11, 4, 11, 16, 8),
      createCuboidShape(9, 9, 5, 11, 11, 7),
      createCuboidShape(13, 8, 0, 15, 12, 5),
      createCuboidShape(13, 11, 4, 15, 16, 8),
      createCuboidShape(13, 9, 5, 15, 11, 7));

  private static final VoxelShape SOUTH_LEFT_BOTTOM_SHAPE = VoxelShapes.union(
      createCuboidShape(8, 0, 12, 16, 4, 16),
      createCuboidShape(9, 4, 11, 11, 8, 16),
      createCuboidShape(9, 0, 8, 11, 5, 12),
      createCuboidShape(9, 5, 9, 11, 7, 11),
      createCuboidShape(13, 4, 11, 15, 8, 16),
      createCuboidShape(13, 0, 8, 15, 5, 12),
      createCuboidShape(13, 5, 9, 15, 7, 11));
  private static final VoxelShape SOUTH_RIGHT_BOTTOM_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 0, 12, 8, 4, 16),
      createCuboidShape(1, 4, 11, 3, 8, 16),
      createCuboidShape(1, 0, 8, 3, 5, 12),
      createCuboidShape(1, 5, 9, 3, 7, 11),
      createCuboidShape(5, 4, 11, 7, 8, 16),
      createCuboidShape(5, 0, 8, 7, 5, 12),
      createCuboidShape(5, 5, 9, 7, 7, 11));
  private static final VoxelShape SOUTH_LEFT_TOP_SHAPE = VoxelShapes.union(
      createCuboidShape(8, 12, 12, 16, 16, 16),
      createCuboidShape(9, 8, 11, 11, 12, 16),
      createCuboidShape(9, 11, 8, 11, 16, 12),
      createCuboidShape(9, 9, 9, 11, 11, 11),
      createCuboidShape(13, 8, 11, 15, 12, 16),
      createCuboidShape(13, 11, 8, 15, 16, 12),
      createCuboidShape(13, 9, 9, 15, 11, 11));
  private static final VoxelShape SOUTH_RIGHT_TOP_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 12, 12, 8, 16, 16),
      createCuboidShape(1, 8, 11, 3, 12, 16),
      createCuboidShape(1, 11, 8, 3, 16, 12),
      createCuboidShape(1, 9, 9, 3, 11, 11),
      createCuboidShape(5, 8, 11, 7, 12, 16),
      createCuboidShape(5, 11, 8, 7, 16, 12),
      createCuboidShape(5, 9, 9, 7, 11, 11));

  private static final VoxelShape WEST_LEFT_BOTTOM_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 0, 8, 4, 4, 16),
      createCuboidShape(0, 4, 9, 5, 8, 11),
      createCuboidShape(4, 0, 9, 8, 5, 11),
      createCuboidShape(5, 5, 9, 7, 7, 11),
      createCuboidShape(0, 4, 13, 5, 8, 15),
      createCuboidShape(4, 0, 13, 8, 5, 15),
      createCuboidShape(5, 5, 13, 7, 7, 15));
  private static final VoxelShape WEST_RIGHT_BOTTOM_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 0, 0, 4, 4, 8),
      createCuboidShape(0, 4, 1, 5, 8, 3),
      createCuboidShape(4, 0, 1, 8, 5, 3),
      createCuboidShape(5, 5, 1, 7, 7, 3),
      createCuboidShape(0, 4, 5, 5, 8, 7),
      createCuboidShape(4, 0, 5, 8, 5, 7),
      createCuboidShape(5, 5, 5, 7, 7, 7));
  private static final VoxelShape WEST_LEFT_TOP_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 12, 8, 4, 16, 16),
      createCuboidShape(0, 8, 9, 5, 12, 11),
      createCuboidShape(4, 11, 9, 8, 16, 11),
      createCuboidShape(5, 9, 9, 7, 11, 11),
      createCuboidShape(0, 8, 13, 5, 12, 15),
      createCuboidShape(4, 11, 13, 8, 16, 15),
      createCuboidShape(5, 9, 13, 7, 11, 15));
  private static final VoxelShape WEST_RIGHT_TOP_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 12, 0, 4, 16, 8),
      createCuboidShape(0, 8, 1, 5, 12, 3),
      createCuboidShape(4, 11, 1, 8, 16, 3),
      createCuboidShape(5, 9, 1, 7, 11, 3),
      createCuboidShape(0, 8, 5, 5, 12, 7),
      createCuboidShape(4, 11, 5, 8, 16, 7),
      createCuboidShape(5, 9, 5, 7, 11, 7));

  private static final VoxelShape EAST_LEFT_BOTTOM_SHAPE = VoxelShapes.union(
      createCuboidShape(12, 0, 0, 16, 4, 8),
      createCuboidShape(11, 4, 1, 16, 8, 3),
      createCuboidShape(8, 0, 1, 12, 5, 3),
      createCuboidShape(9, 5, 1, 11, 7, 3),
      createCuboidShape(11, 4, 5, 16, 8, 7),
      createCuboidShape(8, 0, 5, 12, 5, 7),
      createCuboidShape(9, 5, 5, 11, 7, 7));
  private static final VoxelShape EAST_RIGHT_BOTTOM_SHAPE = VoxelShapes.union(
      createCuboidShape(12, 0, 8, 16, 4, 16),
      createCuboidShape(11, 4, 9, 16, 8, 11),
      createCuboidShape(8, 0, 9, 12, 5, 11),
      createCuboidShape(9, 5, 9, 11, 7, 11),
      createCuboidShape(11, 4, 13, 16, 8, 15),
      createCuboidShape(8, 0, 13, 12, 5, 15),
      createCuboidShape(9, 5, 13, 11, 7, 15));
  private static final VoxelShape EAST_LEFT_TOP_SHAPE = VoxelShapes.union(
      createCuboidShape(12, 12, 0, 16, 16, 8),
      createCuboidShape(11, 8, 1, 16, 12, 3),
      createCuboidShape(8, 11, 1, 12, 16, 3),
      createCuboidShape(9, 9, 1, 11, 11, 3),
      createCuboidShape(11, 8, 5, 16, 12, 7),
      createCuboidShape(8, 11, 5, 12, 16, 7),
      createCuboidShape(9, 9, 5, 11, 11, 7));
  private static final VoxelShape EAST_RIGHT_TOP_SHAPE = VoxelShapes.union(
      createCuboidShape(12, 12, 8, 16, 16, 16),
      createCuboidShape(11, 8, 9, 16, 12, 11),
      createCuboidShape(8, 11, 9, 12, 16, 11),
      createCuboidShape(9, 9, 9, 11, 11, 11),
      createCuboidShape(11, 8, 13, 16, 12, 15),
      createCuboidShape(8, 11, 13, 12, 16, 15),
      createCuboidShape(9, 9, 13, 11, 11, 15));

  private final BlockColor color;

  public VerticalGrooveCornerBlock(final BlockColor color) {
    super(FabricBlockSettings.of(Material.STONE, color.getMapColor()).sounds(BlockSoundGroup.STONE),
        shapeProvider(NORTH_LEFT_BOTTOM_SHAPE, NORTH_RIGHT_BOTTOM_SHAPE, NORTH_LEFT_TOP_SHAPE, NORTH_RIGHT_TOP_SHAPE),
        shapeProvider(EAST_LEFT_BOTTOM_SHAPE, EAST_RIGHT_BOTTOM_SHAPE, EAST_LEFT_TOP_SHAPE, EAST_RIGHT_TOP_SHAPE),
        shapeProvider(SOUTH_LEFT_BOTTOM_SHAPE, SOUTH_RIGHT_BOTTOM_SHAPE, SOUTH_LEFT_TOP_SHAPE, SOUTH_RIGHT_TOP_SHAPE),
        shapeProvider(WEST_LEFT_BOTTOM_SHAPE, WEST_RIGHT_BOTTOM_SHAPE, WEST_LEFT_TOP_SHAPE, WEST_RIGHT_TOP_SHAPE));
    this.color = color;
    this.setDefaultState(this.getDefaultState().with(SIDE, DoorHinge.LEFT).with(HALF, BlockHalf.BOTTOM));
  }

  private static Function<BlockState, VoxelShape> shapeProvider(
      final VoxelShape leftBottomShape, final VoxelShape rightBottomShape,
      final VoxelShape leftTopShape, final VoxelShape rightTopShape
  ) {
    return state -> {
      boolean isLeft = state.get(SIDE) == DoorHinge.LEFT;
      return state.get(HALF) == BlockHalf.BOTTOM
          ? (isLeft ? leftBottomShape : rightBottomShape)
          : (isLeft ? leftTopShape : rightTopShape);
    };
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder.add(SIDE, HALF));
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    BlockState state = super.getPlacementState(ctx);
    BlockPos pos = ctx.getBlockPos();
    double xHit = ctx.getHitPos().x - pos.getX();
    double yHit = ctx.getHitPos().y - pos.getY();
    double zHit = ctx.getHitPos().z - pos.getZ();
    //noinspection ConstantConditions
    DoorHinge side = switch (state.get(FACING)) {
      case NORTH -> xHit < 0.5 ? DoorHinge.LEFT : DoorHinge.RIGHT;
      case SOUTH -> xHit < 0.5 ? DoorHinge.RIGHT : DoorHinge.LEFT;
      case WEST -> zHit < 0.5 ? DoorHinge.RIGHT : DoorHinge.LEFT;
      case EAST -> zHit < 0.5 ? DoorHinge.LEFT : DoorHinge.RIGHT;
      default -> throw new IllegalStateException();
    };
    return state.with(SIDE, side).with(HALF, yHit < 0.5 ? BlockHalf.BOTTOM : BlockHalf.TOP);
  }

  @Override
  public BlockColor getColor() {
    return this.color;
  }
}
