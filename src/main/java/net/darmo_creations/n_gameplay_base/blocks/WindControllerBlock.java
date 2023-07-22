package net.darmo_creations.n_gameplay_base.blocks;

import net.darmo_creations.n_gameplay_base.block_entities.ModBlockEntities;
import net.darmo_creations.n_gameplay_base.block_entities.WindControllerBlockEntity;
import net.darmo_creations.n_gameplay_base.gui.WindControllerScreen;
import net.darmo_creations.n_gameplay_base.items.ControllerAreaTweakerItem;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.Screen;

/**
 * This block lets players configure an area with "wind" that pushes entities in a certain direction.
 *
 * @see ControllerAreaTweakerItem
 * @see WindControllerBlockEntity
 */
public class WindControllerBlock extends ControllerBlockWithArea<WindControllerBlockEntity> {
  public WindControllerBlock() {
    super(MapColor.LIGHT_GRAY, WindControllerBlockEntity.class);
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
