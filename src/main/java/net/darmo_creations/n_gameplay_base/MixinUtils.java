package net.darmo_creations.n_gameplay_base;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Utility methods for mixins.
 */
public final class MixinUtils {
  /**
   * Cancel the given callback info if the client player is in adventure mode.
   */
  public static void disableIfAdventureMode(CallbackInfo ci) {
    MinecraftClient client = MinecraftClient.getInstance();
    if (client != null) {
      PlayerEntity player = client.player;
      if (player != null) {
        //noinspection DataFlowIssue
        PlayerListEntry listEntry = client.getNetworkHandler().getPlayerListEntry(player.getGameProfile().getId());
        if (listEntry != null && listEntry.getGameMode() == GameMode.ADVENTURE) {
          ci.cancel();
        }
      }
    }
  }

  private MixinUtils() {
  }
}
