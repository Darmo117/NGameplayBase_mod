package net.darmo_creations.n_gameplay_base.mixin;

import com.mojang.serialization.Lifecycle;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Disable experimental feature warning when launching a world.
 *
 * @author Parzivail-Modding-Team in HereBeNoDragons mod
 */
@SuppressWarnings("unused")
@Mixin(LevelProperties.class)
public abstract class LevelPropertiesMixin {
  @Shadow
  @Final
  private Lifecycle lifecycle;

  @Inject(method = "getLifecycle()Lcom/mojang/serialization/Lifecycle;", at = @At("HEAD"), cancellable = true)
  private void getLifecycle(CallbackInfoReturnable<Lifecycle> cir) {
    if (this.lifecycle == Lifecycle.experimental()) {
      cir.setReturnValue(Lifecycle.stable());
      cir.cancel();
    }
  }
}