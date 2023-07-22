package net.darmo_creations.n_gameplay_base.blocks;

import net.darmo_creations.n_gameplay_base.block_entities.ModBlockEntities;
import net.darmo_creations.n_gameplay_base.block_entities.SetSpawnTriggerControllerBlockEntity;
import net.darmo_creations.n_gameplay_base.gui.SetSpawnTriggerControllerScreen;
import net.darmo_creations.n_gameplay_base.items.ControllerAreaTweakerItem;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.Screen;

/**
 * This block lets players configure an area which sets the spawnpoint of any survival/adventure player that enters it.
 *
 * @see ControllerAreaTweakerItem
 * @see SetSpawnTriggerControllerBlockEntity
 */
public class SetSpawnTriggerControllerBlock extends ControllerBlockWithArea<SetSpawnTriggerControllerBlockEntity> {
  public SetSpawnTriggerControllerBlock() {
    super(MapColor.LIGHT_BLUE, SetSpawnTriggerControllerBlockEntity.class);
  }

  @Override
  protected BlockEntityType<SetSpawnTriggerControllerBlockEntity> getBlockEntityType() {
    return ModBlockEntities.SET_SPAWN_TRIGGER_CONTROLLER;
  }

  @Override
  protected Screen getScreen(SetSpawnTriggerControllerBlockEntity be) {
    return new SetSpawnTriggerControllerScreen(be);
  }
}
