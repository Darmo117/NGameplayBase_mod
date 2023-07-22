package net.darmo_creations.n_gameplay_base.network.packets;

import io.netty.buffer.Unpooled;
import net.darmo_creations.n_gameplay_base.NGameplayBase;
import net.darmo_creations.n_gameplay_base.Utils;
import net.darmo_creations.n_gameplay_base.block_entities.SetSpawnTriggerControllerBlockEntity;
import net.darmo_creations.n_gameplay_base.network.ServerPacketHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Objects;

public class SetSpawnTriggerControllerDataPacket implements Packet {
  private final BlockPos pos;
  private final BlockPos respawnPos;
  private final Direction respawnFacing;

  @SuppressWarnings("unused") // Used by PacketRegistry class
  public SetSpawnTriggerControllerDataPacket(final PacketByteBuf buf) {
    this.pos = buf.readBlockPos();
    this.respawnPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    this.respawnFacing = buf.readEnumConstant(Direction.class);
  }

  public SetSpawnTriggerControllerDataPacket(BlockPos pos, BlockPos respawnPos, Direction respawnFacing) {
    this.pos = Objects.requireNonNull(pos);
    this.respawnPos = Objects.requireNonNull(respawnPos);
    this.respawnFacing = respawnFacing;
  }

  @Override
  public PacketByteBuf getBuffer() {
    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
    buf.writeBlockPos(this.pos);
    buf.writeInt(this.respawnPos.getX());
    buf.writeInt(this.respawnPos.getY());
    buf.writeInt(this.respawnPos.getZ());
    buf.writeEnumConstant(this.respawnFacing);
    return buf;
  }

  public BlockPos pos() {
    return this.pos;
  }

  public BlockPos getRespawnPosition() {
    return this.respawnPos;
  }

  public Direction getRespawnFacing() {
    return this.respawnFacing;
  }

  /**
   * Server-side handler for this packet.
   */
  public static class ServerHandler implements ServerPacketHandler<SetSpawnTriggerControllerDataPacket> {
    @Override
    public void onPacket(MinecraftServer server, ServerPlayerEntity player, SetSpawnTriggerControllerDataPacket packet) {
      server.execute(() -> Utils.getBlockEntity(SetSpawnTriggerControllerBlockEntity.class, player.world, packet.pos())
          .ifPresent(controller -> {
            try {
              controller.setRespawnPosition(packet.getRespawnPosition());
              controller.setRespawnFacing(packet.getRespawnFacing());
            } catch (IllegalArgumentException e) {
              NGameplayBase.LOGGER.error(e.getMessage(), e);
            }
          })
      );
    }
  }
}
