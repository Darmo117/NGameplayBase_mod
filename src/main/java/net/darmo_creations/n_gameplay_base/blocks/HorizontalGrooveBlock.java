package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.function.Function;

/**
 * A decorative block representing a sort of groove oriented along the x or z axis.
 */
public class HorizontalGrooveBlock extends WaterloggableHorizontalFacingBlock implements Colored {
  public static final EnumProperty<Position> POSITION = EnumProperty.of("position", Position.class);

  private static final VoxelShape NORTH_FLOOR_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 0, 0, 16, 4, 8),
      createCuboidShape(0, 4, 1, 16, 8, 3),
      createCuboidShape(0, 4, 5, 16, 8, 7));
  private static final VoxelShape SOUTH_FLOOR_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 0, 8, 16, 4, 16),
      createCuboidShape(0, 4, 9, 16, 8, 11),
      createCuboidShape(0, 4, 13, 16, 8, 15));
  private static final VoxelShape WEST_FLOOR_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 0, 0, 8, 4, 16),
      createCuboidShape(1, 4, 0, 3, 8, 16),
      createCuboidShape(5, 4, 0, 7, 8, 16));
  private static final VoxelShape EAST_FLOOR_SHAPE = VoxelShapes.union(
      createCuboidShape(8, 0, 0, 16, 4, 16),
      createCuboidShape(9, 4, 0, 11, 8, 16),
      createCuboidShape(13, 4, 0, 15, 8, 16));

  private static final VoxelShape NORTH_CEILING_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 12, 0, 16, 16, 8),
      createCuboidShape(0, 8, 1, 16, 12, 3),
      createCuboidShape(0, 8, 5, 16, 12, 7));
  private static final VoxelShape SOUTH_CEILING_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 12, 8, 16, 16, 16),
      createCuboidShape(0, 8, 9, 16, 12, 11),
      createCuboidShape(0, 8, 13, 16, 12, 15));
  private static final VoxelShape WEST_CEILING_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 12, 0, 8, 16, 16),
      createCuboidShape(1, 8, 0, 3, 12, 16),
      createCuboidShape(5, 8, 0, 7, 12, 16));
  private static final VoxelShape EAST_CEILING_SHAPE = VoxelShapes.union(
      createCuboidShape(8, 12, 0, 16, 16, 16),
      createCuboidShape(9, 8, 0, 11, 12, 16),
      createCuboidShape(13, 8, 0, 15, 12, 16));

  private static final VoxelShape NORTH_WALL_BOTTOM_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 0, 0, 16, 8, 4),
      createCuboidShape(0, 1, 4, 16, 3, 8),
      createCuboidShape(0, 5, 4, 16, 7, 8));
  private static final VoxelShape SOUTH_WALL_BOTTOM_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 0, 12, 16, 8, 16),
      createCuboidShape(0, 1, 8, 16, 3, 12),
      createCuboidShape(0, 5, 8, 16, 7, 12));
  private static final VoxelShape WEST_WALL_BOTTOM_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 0, 0, 4, 8, 16),
      createCuboidShape(4, 1, 0, 8, 3, 16),
      createCuboidShape(4, 5, 0, 8, 7, 16));
  private static final VoxelShape EAST_WALL_BOTTOM_SHAPE = VoxelShapes.union(
      createCuboidShape(12, 0, 0, 16, 8, 16),
      createCuboidShape(8, 1, 0, 12, 3, 16),
      createCuboidShape(8, 5, 0, 12, 7, 16));

  private static final VoxelShape NORTH_WALL_TOP_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 8, 0, 16, 16, 4),
      createCuboidShape(0, 9, 4, 16, 11, 8),
      createCuboidShape(0, 13, 4, 16, 15, 8));
  private static final VoxelShape SOUTH_WALL_TOP_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 8, 12, 16, 16, 16),
      createCuboidShape(0, 9, 8, 16, 11, 12),
      createCuboidShape(0, 13, 8, 16, 15, 12));
  private static final VoxelShape WEST_WALL_TOP_SHAPE = VoxelShapes.union(
      createCuboidShape(0, 8, 0, 4, 16, 16),
      createCuboidShape(4, 9, 0, 8, 11, 16),
      createCuboidShape(4, 13, 0, 8, 15, 16));
  private static final VoxelShape EAST_WALL_TOP_SHAPE = VoxelShapes.union(
      createCuboidShape(12, 8, 0, 16, 16, 16),
      createCuboidShape(8, 9, 0, 12, 11, 16),
      createCuboidShape(8, 13, 0, 12, 15, 16));

  private final BlockColor color;

  public HorizontalGrooveBlock(final BlockColor color) {
    super(FabricBlockSettings.of(Material.STONE, color.getMapColor()).sounds(BlockSoundGroup.STONE),
        shapeProvider(NORTH_FLOOR_SHAPE, NORTH_WALL_BOTTOM_SHAPE, NORTH_WALL_TOP_SHAPE, NORTH_CEILING_SHAPE),
        shapeProvider(EAST_FLOOR_SHAPE, EAST_WALL_BOTTOM_SHAPE, EAST_WALL_TOP_SHAPE, EAST_CEILING_SHAPE),
        shapeProvider(SOUTH_FLOOR_SHAPE, SOUTH_WALL_BOTTOM_SHAPE, SOUTH_WALL_TOP_SHAPE, SOUTH_CEILING_SHAPE),
        shapeProvider(WEST_FLOOR_SHAPE, WEST_WALL_BOTTOM_SHAPE, WEST_WALL_TOP_SHAPE, WEST_CEILING_SHAPE));
    this.color = color;
    this.setDefaultState(this.getDefaultState().with(POSITION, Position.FLOOR));
  }

  private static Function<BlockState, VoxelShape> shapeProvider(
      final VoxelShape floorShape, final VoxelShape wallBottomShape,
      final VoxelShape wallTopShape, final VoxelShape ceilingShape
  ) {
    return state -> switch (state.get(POSITION)) {
      case FLOOR -> floorShape;
      case WALL_BOTTOM -> wallBottomShape;
      case WALL_TOP -> wallTopShape;
      case CEILING -> ceilingShape;
    };
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder.add(POSITION));
  }

  @Override
  public BlockColor getColor() {
    return this.color;
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    BlockState state = super.getPlacementState(ctx);
    Vec3d hitPos = ctx.getHitPos();
    BlockPos blockPos = ctx.getBlockPos();
    double xHit = hitPos.x - blockPos.getX();
    double yHit = hitPos.y - blockPos.getY();
    double zHit = hitPos.z - blockPos.getZ();
    Position position = switch (ctx.getSide()) {
      case DOWN -> Position.CEILING;
      case UP -> Position.FLOOR;
      case NORTH, SOUTH, WEST, EAST -> yHit < 0.5 ? Position.WALL_BOTTOM : Position.WALL_TOP;
    };
    Direction playerFacing = ctx.getPlayerFacing();
    Direction facing;
    if (ctx.getSide().getAxis() == Direction.Axis.Y) {
      if (playerFacing == Direction.NORTH || playerFacing == Direction.SOUTH) {
        facing = zHit < 0.5 ? Direction.NORTH : Direction.SOUTH;
      } else {
        facing = xHit < 0.5 ? Direction.WEST : Direction.EAST;
      }
    } else {
      facing = playerFacing;
    }
    //noinspection ConstantConditions
    return state.with(POSITION, position).with(FACING, facing);
  }

  public enum Position implements StringIdentifiable {
    FLOOR("floor"),
    WALL_BOTTOM("wall_bottom"),
    WALL_TOP("wall_top"),
    CEILING("ceiling");

    private final String name;

    Position(final String name) {
      this.name = name;
    }

    @Override
    public String asString() {
      return this.name;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }
}
