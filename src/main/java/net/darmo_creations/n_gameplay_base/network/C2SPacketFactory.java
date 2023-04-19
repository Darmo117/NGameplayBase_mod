package net.darmo_creations.n_gameplay_base.network;

import net.darmo_creations.n_gameplay_base.NGameplayBase;
import net.darmo_creations.n_gameplay_base.network.packets.Packet;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * A class that defines static methods that create packets to be sent from the client to the server.
 */
public class C2SPacketFactory {
  public static final Identifier LIGHT_ORB_CONTROLLER_DATA_PACKET_ID =
      new Identifier(NGameplayBase.MOD_ID, "light_orb_controller_data_packet");
  public static final Identifier WIND_CONTROLLER_DATA_PACKET_ID =
      new Identifier(NGameplayBase.MOD_ID, "wind_controller_data_packet");

  /**
   * Sends a packet to the server.
   *
   * @param packet The packet to send.
   * @throws IllegalArgumentException If the packet has not been registered through
   *                                  {@link PacketRegistry#registerPacket(Identifier, Class, ServerPacketHandler)}.
   */
  public static void sendPacket(final Packet packet) {
    Optional<Identifier> id = PacketRegistry.getPacketID(packet.getClass());
    if (id.isEmpty()) {
      throw new IllegalArgumentException("invalid packet type " + packet.getClass().getName());
    }
    ClientPlayNetworking.send(id.get(), packet.getBuffer());
  }
}
