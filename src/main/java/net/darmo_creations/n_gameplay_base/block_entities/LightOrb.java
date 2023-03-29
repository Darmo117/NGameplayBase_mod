package net.darmo_creations.n_gameplay_base.block_entities;

import net.darmo_creations.n_gameplay_base.Utils;
import net.darmo_creations.n_gameplay_base.blocks.LightOrbSourceBlock;
import net.darmo_creations.n_gameplay_base.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

/**
 * A light orb is a virtual “entity” managed by a {@link LightOrbControllerBlockEntity}.
 * It emits particles in not set to invisible.
 */
public class LightOrb {
  private static final String NEXT_CP_INDEX_TAG_KEY = "NextCheckpointIndex";
  private static final String WAIT_FOR_PLAYER_TAG_KEY = "WaitForPlayer";
  private static final String TIME_TO_WAIT_TAG_KEY = "TimeToWait";
  private static final String POSITION_TAG_KEY = "Position";
  private static final String VELOCITY_TAG_KEY = "Velocity";

  /**
   * Box used to detect collisions with players.
   */
  private static final Box BOX = new Box(-0.4, -0.4, -0.4, 0.4, 0.4, 0.4);

  private final LightOrbControllerBlockEntity controller;
  private int nextCheckpointIndex;
  private boolean waitForPlayer;
  private int timeToWait;
  private Vec3d position;
  private Vec3d velocity;
  private BlockPos blockPos;
  private boolean moved;

  /**
   * Creates a new light orb for the given controller.
   *
   * @param controller This obr’s controller.
   */
  public LightOrb(LightOrbControllerBlockEntity controller) {
    this.controller = controller;
    BlockPos defaultPos = controller.getPos().up();
    this.setPosition(defaultPos.getX() + 0.5, defaultPos.getY() + 0.5, defaultPos.getZ() + 0.5);
    this.velocity = Vec3d.ZERO;
    this.nextCheckpointIndex = this.controller.getNextCheckpoint(0).map(Pair::getKey).orElse(-1);
    this.waitForPlayer = true;
    this.timeToWait = 0;
  }

  /**
   * Creates a new light orb from an NBT tag for the given controller.
   *
   * @param controller This orb’s controller.
   * @param nbt        An NBT tag.
   */
  public LightOrb(LightOrbControllerBlockEntity controller, final NbtCompound nbt) {
    this.controller = controller;
    this.nextCheckpointIndex = nbt.getInt(NEXT_CP_INDEX_TAG_KEY);
    this.waitForPlayer = nbt.getBoolean(WAIT_FOR_PLAYER_TAG_KEY);
    this.timeToWait = nbt.getInt(TIME_TO_WAIT_TAG_KEY);
    this.setPosition(Utils.getVec3d(nbt, POSITION_TAG_KEY));
    this.velocity = Utils.getVec3d(nbt, VELOCITY_TAG_KEY);
  }

  /**
   * Returns this orb’s position
   */
  public Vec3d getPosition() {
    return this.position;
  }

  /**
   * Returns the size of the hit box.
   */
  public Vec3d getHitBoxSize() {
    return new Vec3d(BOX.getXLength(), BOX.getYLength(), BOX.getZLength());
  }

  /**
   * Sets this orb’s position.
   *
   * @param vector A vector.
   */
  public void setPosition(Vec3d vector) {
    this.setPosition(vector.getX(), vector.getY(), vector.getZ());
  }

  /**
   * Sets this orb’s position.
   *
   * @param x X position.
   * @param y Y position.
   * @param z Z position.
   */
  public void setPosition(double x, double y, double z) {
    this.position = new Vec3d(x, y, z);
    this.blockPos = new BlockPos(x, y, z);
  }

  /**
   * Returns whether this orb has moved during the last tick.
   */
  public boolean hasMoved() {
    return this.moved;
  }

  /**
   * Updates this orb.
   */
  public void tick() {
    if (this.controller.getWorld() == null) {
      return;
    }
    if (this.controller.getWorld().isClient()) {
      if (!this.controller.isEntityInvisible()) {
        this.controller.getWorld().addImportantParticle(ParticleTypes.END_ROD, true, this.position.getX(), this.position.getY(), this.position.getZ(), 0, 0, 0);
      }
    }

    BlockPos previousBlockPos = this.blockPos;

    if (this.controller.isActive()) {
      Optional<PathCheckpoint> next = this.nextCheckpoint();
      if (next.isEmpty()) {
        this.stop();
      } else if (!this.isStopped()) {
        PathCheckpoint nextCheckpoint = next.get();
        double nextX = this.position.getX() + this.velocity.getX();
        double nextY = this.position.getY() + this.velocity.getY();
        double nextZ = this.position.getZ() + this.velocity.getZ();
        BlockPos nextBlockPos = new BlockPos(nextX, nextY, nextZ);
        BlockPos nextCheckpointPos = nextCheckpoint.getPos();
        boolean reachedNextCP = this.blockPos.getSquaredDistance(nextBlockPos) > this.blockPos.getSquaredDistance(nextCheckpointPos);
        if (reachedNextCP) {
          nextX = nextCheckpointPos.getX() + 0.5;
          nextY = nextCheckpointPos.getY() + 0.5;
          nextZ = nextCheckpointPos.getZ() + 0.5;
        }

        this.setPosition(nextX, nextY, nextZ);
        this.moved = true;

        if (reachedNextCP) {
          Optional<Pair<Integer, PathCheckpoint>> nextNextCheckpoint =
              this.controller.getNextCheckpoint(this.nextCheckpointIndex);
          if (nextCheckpoint.isStop() || nextNextCheckpoint.isEmpty()) { // Last CP may not be set to stop
            this.waitForPlayer = nextNextCheckpoint.isPresent();
            this.stop();
          }
          this.timeToWait = nextCheckpoint.getTicksToWait();
          nextNextCheckpoint.ifPresent(pair -> {
            this.nextCheckpointIndex = pair.getKey();
            if (!nextCheckpoint.isStop()) {
              if (this.timeToWait == 0) {
                this.updateMotion(pair.getValue());
              } else {
                this.stop();
              }
            }
          });
        }
      } else if (this.timeToWait > 0) {
        this.timeToWait--;
        if (this.timeToWait == 0 && !this.waitForPlayer) {
          this.nextCheckpoint().ifPresent(this::updateMotion);
        }
      } else if (this.isPlayerColliding()) {
        this.onPlayerCollision();
      }
    } else {
      this.stop();
    }

    this.placeLight(this.blockPos);
    if (!this.blockPos.equals(previousBlockPos)) {
      this.removeLight(previousBlockPos);
    }
  }

  /**
   * Sets this orb’s motion vector to point towards the given checkpoint from this orb’s current position.
   *
   * @param nextCheckpoint The checkpoint to go to.
   */
  private void updateMotion(PathCheckpoint nextCheckpoint) {
    BlockPos nextCPPos = nextCheckpoint.getPos();
    Vec3i vector = nextCPPos.subtract(this.blockPos);
    double length = Math.sqrt(nextCPPos.getSquaredDistance(this.blockPos));
    double speed = this.controller.getSpeed();
    this.velocity = new Vec3d(
        speed * vector.getX() / length,
        speed * vector.getY() / length,
        speed * vector.getZ() / length
    );
  }

  /**
   * Stops this orb.
   */
  private void stop() {
    this.velocity = Vec3d.ZERO;
    this.moved = false;
  }

  /**
   * Whether this orb is not moving.
   */
  private boolean isStopped() {
    return this.velocity.length() == 0;
  }

  /**
   * Get next checkpoint. Returns null if there is none.
   */
  private Optional<PathCheckpoint> nextCheckpoint() {
    int i = this.nextCheckpointIndex;
    return i != -1 && i < this.controller.getCheckpoints().size()
        ? Optional.of(this.controller.getCheckpoints().get(i))
        : Optional.empty();
  }

  /**
   * Returns whether a player is colliding with this orb.
   */
  private boolean isPlayerColliding() {
    //noinspection ConstantConditions
    return this.controller.getWorld()
        .getEntitiesByType(EntityType.PLAYER, BOX.offset(this.position), player -> !player.isSpectator())
        .stream()
        .findAny()
        .isPresent();
  }

  /**
   * Updates the motion of this orb upon collision with a player, only if all conditions are met.
   */
  private void onPlayerCollision() {
    this.nextCheckpoint().ifPresent(nextCheckpoint -> {
      if (this.waitForPlayer && this.controller.isActive() && this.isStopped() && this.timeToWait == 0) {
        this.updateMotion(nextCheckpoint);
        this.waitForPlayer = false;
      }
    });
  }

  /**
   * Called when this orb is removed. Removes any remaining light source block.
   */
  public void onRemoved() {
    this.removeLight(this.blockPos);
  }

  /**
   * Place a light block at the given position.
   */
  private void placeLight(BlockPos pos) {
    //noinspection ConstantConditions
    BlockState blockState = this.controller.getWorld().getBlockState(pos);
    Block block = blockState.getBlock();
    int lightLevel = this.controller.getLightLevel();
    if (blockState.isAir() ||
        block instanceof LightOrbSourceBlock
            && blockState.get(LightOrbSourceBlock.LIGHT_LEVEL) != lightLevel) {
      this.controller.getWorld().setBlockState(pos, ModBlocks.LIGHT_ORB_SOURCE.getDefaultState()
          .with(LightOrbSourceBlock.LIGHT_LEVEL, lightLevel));
    }
  }

  /**
   * Remove light block at given position.
   */
  private void removeLight(BlockPos pos) {
    //noinspection ConstantConditions
    if (this.controller.getWorld().getBlockState(pos).getBlock() == ModBlocks.LIGHT_ORB_SOURCE) {
      this.controller.getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
    }
  }

  /**
   * Serializes this orb to a tag.
   *
   * @param nbt Tag to serialize to.
   */
  public void writeToNBT(NbtCompound nbt) {
    nbt.putInt(NEXT_CP_INDEX_TAG_KEY, this.nextCheckpointIndex);
    nbt.putBoolean(WAIT_FOR_PLAYER_TAG_KEY, this.waitForPlayer);
    nbt.putInt(TIME_TO_WAIT_TAG_KEY, this.timeToWait);
    Utils.putVec3d(this.position, nbt, POSITION_TAG_KEY);
    Utils.putVec3d(this.velocity, nbt, VELOCITY_TAG_KEY);
  }
}
