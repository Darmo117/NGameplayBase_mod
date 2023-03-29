package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/**
 * A light-emitting block that emits a redstone signal when a player enters in contact with it.
 */
public class BlockActivatorLamp extends Block {
  public static final BooleanProperty POWERED = Properties.POWERED;

  private static final VoxelShape COLLISION_AABB = createCuboidShape(0.1, 0.1, 0.1, 15.9, 15.9, 15.9);
  private static final Box HITBOX = VoxelShapes.fullCube().getBoundingBox();

  private static final int TICK_DELAY = 10;

  public BlockActivatorLamp() {
    super(ModBlock.getSettings(FabricBlockSettings.of(Material.STONE, MapColor.WHITE).luminance(15)));
    this.setDefaultState(this.getStateManager().getDefaultState().with(POWERED, false));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder.add(POWERED));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return COLLISION_AABB;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean emitsRedstonePower(BlockState state) {
    return true;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, net.minecraft.util.math.random.Random random) {
    this.updateState(world, pos, state);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    this.updateState(world, pos, world.getBlockState(pos));
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
    if (moved || state.isOf(newState.getBlock())) {
      return;
    }
    if (state.get(POWERED)) {
      this.updateNeighbors(world, pos);
    }
    super.onStateReplaced(state, world, pos, newState, false);
  }

  @Override
  public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
    if (this.getRedstoneStrength(state) > 0) {
      this.updateNeighbors(world, pos);
    }
    super.onBreak(world, pos, state, player);
  }

  @SuppressWarnings("deprecation")
  @Override
  public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
    return this.getRedstoneStrength(state);
  }

  @SuppressWarnings("deprecation")
  @Override
  public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
    return this.getRedstoneStrength(state);
  }

  /**
   * Update redstone power state.
   *
   * @param world World the block is in.
   * @param pos   Block’s position.
   * @param state Block’s current state.
   */
  private void updateState(World world, BlockPos pos, BlockState state) {
    if (!world.isClient()) {
      int strength = world.getNonSpectatingEntities(PlayerEntity.class, HITBOX.offset(pos)).isEmpty() ? 0 : 15;
      if (this.getRedstoneStrength(state) != strength) {
        world.setBlockState(pos, state.with(POWERED, strength != 0), 2);
        this.updateNeighbors(world, pos);
      }
      if (strength != 0) {
        world.createAndScheduleBlockTick(pos, this, TICK_DELAY);
      }
    }
  }

  /**
   * Update all neighboring blocks.
   *
   * @param world Current world.
   * @param pos   Block’s position.
   */
  private void updateNeighbors(World world, BlockPos pos) {
    world.updateNeighborsAlways(pos, this);
  }

  /**
   * Get redstone signal strength for block state.
   */
  private int getRedstoneStrength(BlockState state) {
    return state.get(POWERED) ? 15 : 0;
  }
}
