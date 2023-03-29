package net.darmo_creations.n_gameplay_base.sounds;

import net.darmo_creations.n_gameplay_base.NGameplayBase;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

/**
 * Declares all sounds added by this mod.
 */
@SuppressWarnings("unused")
public final class ModSounds {
  public static final SoundEvent WIND_GUST = register("wind_gust");

  /**
   * Registers a new sound event.
   *
   * @param identifier Sound’s identifier.
   * @return The created sound event.
   */
  private static SoundEvent register(final String identifier) {
    Identifier id = new Identifier(NGameplayBase.MOD_ID, identifier);
    return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
  }

  /**
   * Dummy method called from {@link NGameplayBase#onInitialize()} to register sounds:
   * it forces the class to be loaded during mod initialization, while the registries are unlocked.
   * <p>
   * Must be called on both clients and server.
   */
  public static void init() {
  }

  private ModSounds() {
  }
}
