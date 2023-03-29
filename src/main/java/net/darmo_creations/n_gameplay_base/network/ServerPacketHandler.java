package net.darmo_creations.n_gameplay_base.network;

import net.darmo_creations.n_gameplay_base.network.packets.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public interface ServerPacketHandler<T extends Packet> {
  void onPacket(MinecraftServer server, ServerPlayerEntity player, final T packet);
}
