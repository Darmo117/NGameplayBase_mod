package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/**
 * This block represents a light-sensitive vertical slab.
 *
 * @see LightSensitiveBarrierBlock
 */
public class LightSensitiveBarrierVerticalSlabBlock extends VerticalSlabBlock
    implements Colored, LightSensitiveBlock<LightSensitiveBarrierVerticalSlabBlock> {
  private final BlockColor color;
  private final boolean passable;
  private LightSensitiveBarrierVerticalSlabBlock counterpartBlock;

  /**
   * Create a light sensitive barrier vertical slab.
   *
   * @param passable Whether this block can be passed through.
   */
  public LightSensitiveBarrierVerticalSlabBlock(final BlockColor color, final boolean passable) {
    super(LightSensitiveBlock.getSettings(FabricBlockSettings.of(Material.STONE, color.getMapColor())
        .sounds(BlockSoundGroup.STONE), passable));
    this.color = color;
    this.passable = passable;
  }

  @Override
  public BlockColor getColor() {
    return this.color;
  }

  @Override
  public void toggleState(BlockState state, World world, BlockPos pos) {
    world.setBlockState(pos, this.counterpartBlock.getDefaultState().with(TYPE, state.get(TYPE)));
  }

  @Override
  public boolean isPassable() {
    return this.passable;
  }

  @Override
  public LightSensitiveBarrierVerticalSlabBlock getCounterpartBlock() {
    return this.counterpartBlock;
  }

  @Override
  public void setCounterpartBlock(LightSensitiveBarrierVerticalSlabBlock block) {
    if (block == this) {
      //noinspection deprecation,OptionalGetWithoutIsPresent
      throw new IllegalArgumentException("counterpart of block %s set to itself".formatted(this.getRegistryEntry().getKey().get()));
    }
    this.counterpartBlock = block;
    block.counterpartBlock = this;
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return this.isPassable() ? VoxelShapes.empty() : super.getOutlineShape(state, world, pos, context);
  }

  @Override
  public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
    return this.isPassable() || super.canPathfindThrough(state, world, pos, type);
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return this.isPassable() ? VoxelShapes.empty() : super.getCameraCollisionShape(state, world, pos, context);
  }

  @SuppressWarnings("deprecation")
  @Override
  public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
    return this.isPassable() ? 1 : super.getAmbientOcclusionLightLevel(state, world, pos);
  }

  @Override
  public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
    return this.isPassable() || super.isTranslucent(state, world, pos);
  }
}
