package net.darmo_creations.n_gameplay_base.items;

import net.darmo_creations.n_gameplay_base.Utils;
import net.darmo_creations.n_gameplay_base.block_entities.WindControllerBlockEntity;
import net.darmo_creations.n_gameplay_base.blocks.WindControllerBlock;
import net.darmo_creations.n_gameplay_base.network.ServerNetworkUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

/**
 * Item used to edit wind regions.
 * This item is not stackable.
 * <p>
 * Usage:
 * <li>Sneak-right-click on a controller block to select it.
 * Corresponding region will then be highlighted while holding this item.
 * <li>Right-click a controller block to open its configuration GUI.
 * <li>Right-click on a block to make it one of the region’s corners.
 *
 * @see WindControllerBlock
 * @see WindControllerBlockEntity
 */
public class WindTweakerItem extends Item {
  private static final String CONTROLLER_POS_TAG_KEY = "ControllerPos";

  public WindTweakerItem(Settings settings) {
    super(settings.maxCount(1));
  }

  @Override
  public ActionResult useOnBlock(ItemUsageContext context) {
    PlayerEntity player = context.getPlayer();
    if (player == null) {
      return ActionResult.FAIL;
    }
    World world = context.getWorld();
    BlockPos pos = context.getBlockPos();
    Hand hand = context.getHand();

    if (player.isCreativeLevelTwoOp()) {
      if (world.getBlockState(pos).getBlock() instanceof WindControllerBlock) {
        if (player.isSneaking()) {
          Optional<WindControllerBlockEntity> be = Utils.getBlockEntity(WindControllerBlockEntity.class, world, pos);
          if (be.isPresent()) {
            NbtCompound tag = new NbtCompound();
            tag.put(CONTROLLER_POS_TAG_KEY, NbtHelper.fromBlockPos(be.get().getPos()));
            player.getStackInHand(hand).setNbt(tag);
            ServerNetworkUtils.sendMessage(world, player,
                Text.translatable(
                    "item.n_gameplay_base.wind_tweaker.action_bar.controller_selected",
                    Utils.blockPosToString(pos)
                ).setStyle(Style.EMPTY.withColor(Formatting.YELLOW)), true);
            return ActionResult.SUCCESS;
          }
        }
      } else {
        Optional<WindControllerBlockEntity> controller = getControllerTileEntity(player.getStackInHand(hand), world);
        if (controller.isPresent()) {
          controller.get().addCorner(pos);
          return ActionResult.SUCCESS;
        }
      }
    }
    return ActionResult.FAIL;
  }

  @Override
  public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
    super.inventoryTick(stack, world, entity, slot, selected);
    if (world.isClient()) {
      Optional<WindControllerBlockEntity> be = getControllerTileEntity(stack, world);
      //noinspection ConstantConditions
      if (be.isEmpty() && stack.hasNbt() && stack.getNbt().contains(CONTROLLER_POS_TAG_KEY)) {
        stack.getNbt().remove(CONTROLLER_POS_TAG_KEY);
      }
    }
  }

  @Override
  public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    Optional<WindControllerBlockEntity> controller = getControllerTileEntity(stack, world);
    if (controller.isPresent()) {
      tooltip.add(Text.translatable(
          "item.n_gameplay_base.wind_tweaker.tooltip.selection",
          Utils.blockPosToString(controller.get().getPos())
      ).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    } else {
      tooltip.add(Text.translatable(
          "item.n_gameplay_base.wind_tweaker.tooltip.no_selection"
      ).setStyle(Style.EMPTY.withColor(Formatting.GRAY).withItalic(true)));
    }
  }

  /**
   * Return the tile entity for the controller block at the given position.
   *
   * @param stack Item stack that contains NBT tag with controller’s position.
   * @param world World to look for block.
   * @return The tile entity, null if none were found or tile entity is of the wrong type.
   */
  public static Optional<WindControllerBlockEntity> getControllerTileEntity(ItemStack stack, World world) {
    if (stack.hasNbt()) {
      //noinspection ConstantConditions
      return Utils.getBlockEntity(WindControllerBlockEntity.class, world,
          NbtHelper.toBlockPos(stack.getNbt().getCompound(CONTROLLER_POS_TAG_KEY)));
    }
    return Optional.empty();
  }
}
