package net.darmo_creations.n_gameplay_base.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class ControllerStickItem extends Item {
  private static final String ACTIVE = "Active";

  public ControllerStickItem(Settings settings) {
    super(settings.rarity(Rarity.EPIC).maxCount(1));
  }

  @Override
  public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    ItemStack itemStack = user.getStackInHand(hand);
    if (!user.isCreativeLevelTwoOp()) {
      return TypedActionResult.fail(itemStack);
    }
    setActive(!isActive(itemStack), itemStack);
    return TypedActionResult.success(itemStack, true);
  }

  @Override
  public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(Text.translatable(
        "item.n_gameplay_base.controller_stick.tooltip"
    ).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    if (isActive(stack)) {
      tooltip.add(Text.translatable(
          "item.n_gameplay_base.controller_stick.tooltip.active"
      ).setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
    } else {
      tooltip.add(Text.translatable(
          "item.n_gameplay_base.controller_stick.tooltip.inactive"
      ).setStyle(Style.EMPTY.withColor(Formatting.RED)));
    }
  }

  @Override
  public boolean hasGlint(ItemStack stack) {
    return true;
  }

  private static void setActive(boolean active, ItemStack stack) {
    if (stack.getNbt() == null) {
      stack.setNbt(new NbtCompound());
    }
    stack.getNbt().putBoolean(ACTIVE, active);
  }

  public static boolean isActive(ItemStack stack) {
    return stack.getItem() instanceof ControllerStickItem
        && Optional.ofNullable(stack.getNbt()).map(nbt -> nbt.getBoolean(ACTIVE)).orElse(false);
  }
}
