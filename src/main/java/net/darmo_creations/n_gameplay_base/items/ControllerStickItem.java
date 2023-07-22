package net.darmo_creations.n_gameplay_base.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

import java.util.List;

/**
 * A tool that activates rendering of all controller blocks.
 */
public class ControllerStickItem extends Item {
  public ControllerStickItem(Settings settings) {
    super(settings.rarity(Rarity.EPIC).maxCount(1));
  }

  @Override
  public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(Text.translatable("item.n_gameplay_base.controller_stick.tooltip")
        .setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
  }

  @Override
  public boolean hasGlint(ItemStack stack) {
    return true;
  }
}
