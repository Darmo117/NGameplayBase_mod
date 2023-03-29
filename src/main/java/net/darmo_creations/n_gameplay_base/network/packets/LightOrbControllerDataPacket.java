package net.darmo_creations.n_gameplay_base.network.packets;

import io.netty.buffer.Unpooled;
import net.darmo_creations.n_gameplay_base.NGameplayBase;
import net.darmo_creations.n_gameplay_base.Utils;
import net.darmo_creations.n_gameplay_base.block_entities.LightOrbControllerBlockEntity;
import net.darmo_creations.n_gameplay_base.block_entities.PathCheckpoint;
import net.darmo_creations.n_gameplay_base.network.ServerPacketHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.LinkedList;
import java.util.List;

public class LightOrbControllerDataPacket implements Packet {
  private final BlockPos pos;
  private final boolean active;
  private final boolean loops;
  private final boolean invisible;
  private final int lightLevel;
  private final double speed;
  private final List<PathCheckpoint> checkpoints;

  public LightOrbControllerDataPacket(final PacketByteBuf buf) {
    this.pos = buf.readBlockPos();
    this.active = buf.readBoolean();
    this.loops = buf.readBoolean();
    this.invisible = buf.readBoolean();
    this.lightLevel = buf.readInt();
    this.speed = buf.readDouble();
    final int checkpointsNb = buf.readInt();
    this.checkpoints = new LinkedList<>();
    for (int i = 0; i < checkpointsNb; i++) {
      //noinspection ConstantConditions
      this.checkpoints.add(new PathCheckpoint(buf.readNbt()));
    }
  }

  public LightOrbControllerDataPacket(
      final BlockPos pos,
      final boolean active,
      final boolean loops,
      final boolean invisible,
      final int lightLevel,
      final double speed,
      final List<PathCheckpoint> checkpoints
  ) {
    this.pos = pos;
    this.active = active;
    this.loops = loops;
    this.invisible = invisible;
    this.lightLevel = lightLevel;
    this.speed = speed;
    this.checkpoints = checkpoints;
  }

  @Override
  public PacketByteBuf getBuffer() {
    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
    buf.writeBlockPos(this.pos);
    buf.writeBoolean(this.active);
    buf.writeBoolean(this.loops);
    buf.writeBoolean(this.invisible);
    buf.writeInt(this.lightLevel);
    buf.writeDouble(this.speed);
    buf.writeInt(this.checkpoints.size());
    this.checkpoints.forEach(checkpoint -> buf.writeNbt(checkpoint.toNBT()));
    return buf;
  }

  public BlockPos pos() {
    return this.pos;
  }

  public boolean active() {
    return this.active;
  }

  public boolean loops() {
    return this.loops;
  }

  public boolean invisible() {
    return this.invisible;
  }

  public int lightLevel() {
    return this.lightLevel;
  }

  public double speed() {
    return this.speed;
  }

  public List<PathCheckpoint> checkpoints() {
    return this.checkpoints;
  }

  /**
   * Server-side handler for this packet.
   */
  public static class ServerHandler implements ServerPacketHandler<LightOrbControllerDataPacket> {
    @Override
    public void onPacket(MinecraftServer server, ServerPlayerEntity player, LightOrbControllerDataPacket packet) {
      server.execute(() -> Utils.getBlockEntity(LightOrbControllerBlockEntity.class, player.world, packet.pos())
          .ifPresent(controller -> {
            controller.setActive(packet.active());
            controller.setLoops(packet.loops());
            controller.setEntityInvisible(packet.invisible());
            try {
              controller.setLightLevel(packet.lightLevel());
              controller.setSpeed(packet.speed());
              controller.setCheckpoints(packet.checkpoints());
            } catch (IllegalArgumentException e) {
              NGameplayBase.LOGGER.error(e.getMessage(), e);
              return;
            }
            controller.resetOrb();
          })
      );
    }
  }
}
