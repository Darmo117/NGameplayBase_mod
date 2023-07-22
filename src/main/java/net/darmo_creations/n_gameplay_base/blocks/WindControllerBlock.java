package net.darmo_creations.n_gameplay_base.blocks;

import net.darmo_creations.n_gameplay_base.block_entities.ModBlockEntities;
import net.darmo_creations.n_gameplay_base.block_entities.WindControllerBlockEntity;
import net.darmo_creations.n_gameplay_base.gui.WindControllerScreen;
import net.darmo_creations.n_gameplay_base.items.WindTweakerItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This block lets players configure an area with "wind" that pushes entities in a certain direction.
 *
 * @see WindTweakerItem
 * @see WindControllerBlockEntity
 */
public class WindControllerBlock extends ControllerBlock<WindControllerBlockEntity> {
  public WindControllerBlock() {
    super(MapColor.LIGHT_GRAY, WindControllerBlockEntity.class);
  }

  @Override
  protected void onPlaced(WindControllerBlockEntity be, World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
    be.init(state);
  }

  @Override
  protected BlockEntityType<WindControllerBlockEntity> getBlockEntityType() {
    return ModBlockEntities.WIND_CONTROLLER;
  }

  @Override
  protected Screen getScreen(WindControllerBlockEntity be) {
    return new WindControllerScreen(be);
  }
}
