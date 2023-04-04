package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
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
 * Light-sensitive barrier blocks have two states: passable and solid. When passable, players can pass through it.
 */
public class LightSensitiveBarrierBlock extends Block
    implements Colored, LightSensitiveBlock<LightSensitiveBarrierBlock> {
  private final BlockColor color;
  private final boolean passable;
  private LightSensitiveBarrierBlock counterpartBlock;

  /**
   * Create a light sensitive barrier block.
   *
   * @param passable Whether this block can be passed through.
   */
  public LightSensitiveBarrierBlock(final BlockColor color, final boolean passable) {
    super(LightSensitiveBlock.getSettings(FabricBlockSettings.of(Material.STONE, color.getMapColor()).sounds(BlockSoundGroup.STONE), passable));
    this.color = color;
    this.passable = passable;
  }

  @Override
  public BlockColor getColor() {
    return this.color;
  }

  @Override
  public BlockState getBlockStateForColor(BlockState state, BlockColor color) {
    ColoredBlockMap<LightSensitiveBarrierBlock> m =
        this.passable
            ? ModBlocks.COLORED_LIGHT_SENSITIVE_BARRIERS_PASSABLE
            : ModBlocks.COLORED_LIGHT_SENSITIVE_BARRIERS;
    return m.get(color).getDefaultState();
  }

  @Override
  public boolean isPassable() {
    return this.passable;
  }

  @Override
  public LightSensitiveBarrierBlock getCounterpartBlock() {
    return this.counterpartBlock;
  }

  @Override
  public void setCounterpartBlock(LightSensitiveBarrierBlock block) {
    if (block == this) {
      //noinspection deprecation,OptionalGetWithoutIsPresent
      throw new IllegalArgumentException("counterpart of block %s set to itself".formatted(this.getRegistryEntry().getKey().get()));
    }
    this.counterpartBlock = block;
    block.counterpartBlock = this;
  }

  @Override
  public void toggleState(BlockState state, World world, BlockPos pos) {
    world.setBlockState(pos, this.getCounterpartBlock().getDefaultState());
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean hasSidedTransparency(BlockState state) {
    return this.isPassable() || super.hasSidedTransparency(state);
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return this.isPassable() ? VoxelShapes.empty() : VoxelShapes.fullCube();
  }

  @SuppressWarnings("deprecation")
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
