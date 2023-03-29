package net.darmo_creations.n_gameplay_base.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

/**
 * This class defines some server-side utility networking methods.
 */
public final class ServerNetworkUtils {
  /**
   * Sends a chat message to a player. Does nothing if the world is remote (i.e. client-side).
   *
   * @param world       The world the player is in.
   * @param player      The player to send the message to.
   * @param text        Message’s text.
   * @param inActionBar Whether to send the message in the player’s action bar.
   */
  public static void sendMessage(final World world, PlayerEntity player, final Text text, final boolean inActionBar) {
    if (!world.isClient()) {
      player.sendMessage(text, inActionBar);
    }
  }

  private ServerNetworkUtils() {
  }
}
