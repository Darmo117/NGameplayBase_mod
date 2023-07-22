package net.darmo_creations.n_gameplay_base.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * This mixin adds a message to {@link PlayerEntity.SleepFailureReason#NOT_POSSIBLE_HERE}.
 */
@SuppressWarnings("unused")
@Mixin(PlayerEntity.SleepFailureReason.class)
public class PlayerEntity$SleepFailureReasonMixin {
  @SuppressWarnings("EqualsBetweenInconvertibleTypes")
  @Inject(method = "getMessage", at = @At("HEAD"), cancellable = true)
  private void onGetMessage(CallbackInfoReturnable<Text> cir) {
    // Using equals to test equality as we cannot inherit from enums, "==" operator would not compile
    if (this.equals(PlayerEntity.SleepFailureReason.NOT_POSSIBLE_HERE)) {
      cir.setReturnValue(Text.translatable("block.minecraft.bed.not_possible_here")
          .setStyle(Style.EMPTY.withItalic(true)));
    }
  }
}
