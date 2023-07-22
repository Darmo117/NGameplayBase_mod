package net.darmo_creations.n_gameplay_base.block_entities;

import net.darmo_creations.n_gameplay_base.blocks.ModBlocks;
import net.darmo_creations.n_gameplay_base.blocks.SetSpawnTriggerControllerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Objects;

/**
 * Block entity for kill trigger controller.
 *
 * @see SetSpawnTriggerControllerBlock
 * @see ModBlocks#SET_SPAWN_TRIGGER_CONTROLLER
 */
public class SetSpawnTriggerControllerBlockEntity extends ControllerBlockEntityWithArea {
  private static final String RESPAWN_POS_KEY = "RespawPosition";
  private static final String RESPAWN_FACING_KEY = "RespawnFacing";

  private BlockPos respawnPosition;
  private Direction respawnFacing;

  /**
   * Create a block entity with an empty region.
   */
  public SetSpawnTriggerControllerBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntities.SET_SPAWN_TRIGGER_CONTROLLER, pos, state);
  }

  @Override
  public void init(BlockState blockState) {
    super.init(blockState);
    this.setRespawnPosition(this.getPos().up());
    this.setRespawnFacing(Direction.NORTH);
  }

  public BlockPos getRespawnPosition() {
    return this.respawnPosition;
  }

  public void setRespawnPosition(BlockPos respawnPosition) {
    this.respawnPosition = Objects.requireNonNull(respawnPosition);
    this.markDirty();
  }

  public Direction getRespawnFacing() {
    return this.respawnFacing;
  }

  public void setRespawnFacing(Direction respawnFacing) {
    this.respawnFacing = Objects.requireNonNull(respawnFacing);
    this.markDirty();
  }

  @Override
  protected void doTick() {
    //noinspection DataFlowIssue
    this.world.getEntitiesByClass(ServerPlayerEntity.class, this.getBox(), LivingEntity::canTakeDamage)
        .forEach(p -> {
          BlockPos spawnPoint = p.getSpawnPointPosition();
          if (spawnPoint != null && !spawnPoint.equals(this.respawnPosition)) {
            p.setSpawnPoint(this.world.getRegistryKey(), this.respawnPosition, this.respawnFacing.asRotation(), true, false);
          }
        });
  }

  @Override
  protected void writeNbt(NbtCompound nbt) {
    super.writeNbt(nbt);
    nbt.put(RESPAWN_POS_KEY, NbtHelper.fromBlockPos(this.respawnPosition));
    nbt.putInt(RESPAWN_FACING_KEY, this.respawnFacing.ordinal());
  }

  @Override
  public void readNbt(NbtCompound nbt) {
    super.readNbt(nbt);
    this.respawnPosition = NbtHelper.toBlockPos(nbt.getCompound(RESPAWN_POS_KEY));
    this.respawnFacing = Direction.values()[nbt.getInt(RESPAWN_FACING_KEY)];
  }
}
