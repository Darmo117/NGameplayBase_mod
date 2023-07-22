package net.darmo_creations.n_gameplay_base.blocks;

import net.darmo_creations.n_gameplay_base.block_entities.KillTriggerControllerBlockEntity;
import net.darmo_creations.n_gameplay_base.block_entities.ModBlockEntities;
import net.darmo_creations.n_gameplay_base.items.ControllerAreaTweakerItem;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;

/**
 * This block lets players configure an area which kills any survival/adventure player that enters it.
 *
 * @see ControllerAreaTweakerItem
 * @see KillTriggerControllerBlockEntity
 */
public class KillTriggerControllerBlock extends ControllerBlockWithArea<KillTriggerControllerBlockEntity> {
  public KillTriggerControllerBlock() {
    super(MapColor.RED, KillTriggerControllerBlockEntity.class);
  }

  @Override
  protected BlockEntityType<KillTriggerControllerBlockEntity> getBlockEntityType() {
    return ModBlockEntities.KILL_TRIGGER_CONTROLLER;
  }

  @Override
  protected boolean hasScreen() {
    return false;
  }
}
