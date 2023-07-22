package net.darmo_creations.n_gameplay_base.block_entities;

import net.darmo_creations.n_gameplay_base.Utils;
import net.darmo_creations.n_gameplay_base.blocks.ModBlocks;
import net.darmo_creations.n_gameplay_base.blocks.WindControllerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

/**
 * Block entity for wind controller.
 *
 * @see WindControllerBlock
 * @see ModBlocks#WIND_CONTROLLER
 */
public class WindControllerBlockEntity extends ControllerBlockEntityWithArea {
  private static final String DIRECTION_TAG_KEY = "Direction";

  /**
   * Vector representing the wind’s direction,
   * i.e. the acceleration applied to all living entities within the region.
   */
  private Vec3d windDirection;

  /**
   * Create a block entity with an empty region.
   */
  public WindControllerBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntities.WIND_CONTROLLER, pos, state);
  }

  /**
   * Initialize this block entity with a wind speed of (0, 0, 0)
   * and a region of 1x1x1 right above the associated block.
   *
   * @param blockState State of the associated block.
   */
  public void init(BlockState blockState) {
    super.init(blockState);
    this.setWindDirection(Vec3d.ZERO);
  }

  @Override
  protected void doTick() {
    //noinspection DataFlowIssue
    this.world.getEntitiesByClass(LivingEntity.class, this.getBox(), LivingEntity::isAlive)
        .forEach(e -> e.addVelocity(this.windDirection.getX(), this.windDirection.getY(), this.windDirection.getZ()));
  }

  public Vec3d getWindDirection() {
    return this.windDirection;
  }

  public void setWindDirection(Vec3d windDirection) {
    // Copy vector as they are mutable
    this.windDirection = Objects.requireNonNull(windDirection);
    this.markDirty();
  }

  @Override
  protected void writeNbt(NbtCompound nbt) {
    super.writeNbt(nbt);
    Utils.putVec3d(this.windDirection, nbt, DIRECTION_TAG_KEY);
  }

  @Override
  public void readNbt(NbtCompound nbt) {
    super.readNbt(nbt);
    this.windDirection = Utils.getVec3d(nbt, DIRECTION_TAG_KEY);
  }
}
