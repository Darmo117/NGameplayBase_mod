package net.darmo_creations.n_gameplay_base.block_entities;

import net.darmo_creations.n_gameplay_base.blocks.ControllerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;

/**
 * Base class for block entities associated to {@link ControllerBlock}s.
 */
public abstract class ControllerBlockEntity extends BlockEntity {
  private static final String TRIGGERED_TAG_KEY = "Triggered";

  private boolean triggered;

  protected ControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }

  public void tick() {
  }

  public boolean isTriggered() {
    return this.triggered;
  }

  public void setTriggered(boolean triggered) {
    this.triggered = triggered;
    this.markDirty();
  }

  @Override
  protected void writeNbt(NbtCompound nbt) {
    super.writeNbt(nbt);
    nbt.putBoolean(TRIGGERED_TAG_KEY, this.triggered);
  }

  @Override
  public void readNbt(NbtCompound nbt) {
    super.readNbt(nbt);
    this.triggered = nbt.getBoolean(TRIGGERED_TAG_KEY);
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
