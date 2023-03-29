package net.darmo_creations.n_gameplay_base.dimensions;

import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.math.Vec3d;

/**
 * Custom effects for the custom void dimensions defined in this mod’s datapack.
 * <p>
 * Features:
 * <li>No clouds</li>
 * <li>No skybox</li>
 * <li>Fog</li>
 * <li>No thick fog</li>
 */
public class VoidDimensionEffects extends DimensionEffects {
  public VoidDimensionEffects() {
    super(Float.NaN, false, SkyType.NONE, false, false);
  }

  @Override
  public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
    return color;
  }

  @Override
  public boolean useThickFog(int camX, int camY) {
    return false;
  }
}
