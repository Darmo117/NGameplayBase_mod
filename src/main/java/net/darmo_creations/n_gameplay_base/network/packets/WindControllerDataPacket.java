package net.darmo_creations.n_gameplay_base.network.packets;

import io.netty.buffer.Unpooled;
import net.darmo_creations.n_gameplay_base.NGameplayBase;
import net.darmo_creations.n_gameplay_base.Utils;
import net.darmo_creations.n_gameplay_base.block_entities.WindControllerBlockEntity;
import net.darmo_creations.n_gameplay_base.network.ServerPacketHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public class WindControllerDataPacket implements Packet {
  private final BlockPos pos;
  private final Vec3d windDirection;

  @SuppressWarnings("unused") // Used by PacketRegistry class
  public WindControllerDataPacket(final PacketByteBuf buf) {
    this.pos = buf.readBlockPos();
    this.windDirection = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
  }

  public WindControllerDataPacket(BlockPos pos, Vec3d windDirection) {
    this.pos = Objects.requireNonNull(pos);
    this.windDirection = Objects.requireNonNull(windDirection);
  }

  @Override
  public PacketByteBuf getBuffer() {
    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
    buf.writeBlockPos(this.pos);
    buf.writeDouble(this.windDirection.getX());
    buf.writeDouble(this.windDirection.getY());
    buf.writeDouble(this.windDirection.getZ());
    return buf;
  }

  public BlockPos pos() {
    return this.pos;
  }

  public Vec3d getWindDirection() {
    return this.windDirection;
  }

  /**
   * Server-side handler for this packet.
   */
  public static class ServerHandler implements ServerPacketHandler<WindControllerDataPacket> {
    @Override
    public void onPacket(MinecraftServer server, ServerPlayerEntity player, WindControllerDataPacket packet) {
      server.execute(() -> Utils.getBlockEntity(WindControllerBlockEntity.class, player.world, packet.pos())
          .ifPresent(controller -> {
            try {
              controller.setWindDirection(packet.getWindDirection());
            } catch (IllegalArgumentException e) {
              NGameplayBase.LOGGER.error(e.getMessage(), e);
            }
          })
      );
    }
  }
}
