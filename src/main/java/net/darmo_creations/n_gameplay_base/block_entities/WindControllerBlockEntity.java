package net.darmo_creations.n_gameplay_base.block_entities;

import net.darmo_creations.n_gameplay_base.Utils;
import net.darmo_creations.n_gameplay_base.blocks.ModBlocks;
import net.darmo_creations.n_gameplay_base.blocks.WindControllerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Objects;

/**
 * Block entity for wind controller.
 *
 * @see WindControllerBlock
 * @see ModBlocks#WIND_CONTROLLER
 */
public class WindControllerBlockEntity extends BlockEntity {
  private static final String ACTIVE_TAG_KEY = "Active";
  private static final String CORNER1_TAG_KEY = "Corner1";
  private static final String CORNER2_TAG_KEY = "Corner2";
  private static final String DIRECTION_TAG_KEY = "Direction";

  /**
   * Whether the wind should blow.
   */
  private boolean active;
  /**
   * Vector representing the wind’s direction,
   * i.e. the acceleration applied to all living entities within the region.
   */
  private Vec3d windDirection;
  /**
   * Lower and upper corners of the windy region.
   */
  private BlockPos lowerCorner, upperCorner;
  /**
   * Actual corners of the windy region as set by players.
   */
  private BlockPos corner1, corner2;

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
    this.setActive(blockState.get(WindControllerBlock.TRIGGERED));
    this.setWindDirection(Vec3d.ZERO);
    this.addCorner(this.getPos().up());
    this.addCorner(this.getPos().up());
  }

  /**
   * Executes one tick of this block entity’s logic.
   */
  public void tick() {
    if (this.active && this.lowerCorner != null && this.upperCorner != null && this.world != null) {
      Box box = new Box(
          this.lowerCorner.getX(), this.lowerCorner.getY(), this.lowerCorner.getZ(),
          this.upperCorner.getX() + 1, this.upperCorner.getY() + 1, this.upperCorner.getZ() + 1
      );
      List<LivingEntity> entities = this.world.getEntitiesByClass(LivingEntity.class, box, LivingEntity::isAlive);
      entities.forEach(
          e -> e.addVelocity(this.windDirection.getX(), this.windDirection.getY(), this.windDirection.getZ()));
    }
  }

  /**
   * Whether this controller is active, i.e. the light orb should react to player.
   */
  public boolean isActive() {
    return this.active;
  }

  /**
   * Set active state.
   *
   * @param active Whether the wind should blow.
   */
  public void setActive(boolean active) {
    this.active = active;
    this.markDirty();
  }

  public Vec3d getWindDirection() {
    return this.windDirection;
  }

  public void setWindDirection(Vec3d windDirection) {
    // Copy vector as they are mutable
    this.windDirection = Objects.requireNonNull(windDirection);
    this.markDirty();
  }

  public void addCorner(BlockPos pos) {
    this.corner2 = this.corner1;
    this.corner1 = pos;
    this.updateLowerUpperCorners();
    this.markDirty();
  }

  public BlockPos getLowerCorner() {
    return this.lowerCorner;
  }

  public BlockPos getUpperCorner() {
    return this.upperCorner;
  }

  private void updateLowerUpperCorners() {
    if (this.corner1 != null && this.corner2 != null) {
      this.lowerCorner = new BlockPos(
          Math.min(this.corner1.getX(), this.corner2.getX()),
          Math.min(this.corner1.getY(), this.corner2.getY()),
          Math.min(this.corner1.getZ(), this.corner2.getZ())
      );
      this.upperCorner = new BlockPos(
          Math.max(this.corner1.getX(), this.corner2.getX()),
          Math.max(this.corner1.getY(), this.corner2.getY()),
          Math.max(this.corner1.getZ(), this.corner2.getZ())
      );
    }
  }

  @Override
  protected void writeNbt(NbtCompound nbt) {
    super.writeNbt(nbt);
    nbt.putBoolean(ACTIVE_TAG_KEY, this.active);
    nbt.put(CORNER1_TAG_KEY, NbtHelper.fromBlockPos(this.corner1));
    nbt.put(CORNER2_TAG_KEY, NbtHelper.fromBlockPos(this.corner2));
    Utils.putVec3d(this.windDirection, nbt, DIRECTION_TAG_KEY);
  }

  @Override
  public void readNbt(NbtCompound nbt) {
    super.readNbt(nbt);
    this.active = nbt.getBoolean(ACTIVE_TAG_KEY);
    this.corner1 = NbtHelper.toBlockPos(nbt.getCompound(CORNER1_TAG_KEY));
    this.corner2 = NbtHelper.toBlockPos(nbt.getCompound(CORNER2_TAG_KEY));
    this.windDirection = Utils.getVec3d(nbt, DIRECTION_TAG_KEY);
    this.updateLowerUpperCorners();
  }

  @Override
  public Packet<ClientPlayPacketListener> toUpdatePacket() {
    return BlockEntityUpdateS2CPacket.create(this);
  }

  @Override
  public NbtCompound toInitialChunkDataNbt() {
    return this.createNbt();
  }
}
