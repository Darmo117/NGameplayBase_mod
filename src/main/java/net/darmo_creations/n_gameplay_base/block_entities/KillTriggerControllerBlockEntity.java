package net.darmo_creations.n_gameplay_base.block_entities;

import net.darmo_creations.n_gameplay_base.blocks.KillTriggerControllerBlock;
import net.darmo_creations.n_gameplay_base.blocks.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Block entity for kill trigger controller.
 *
 * @see KillTriggerControllerBlock
 * @see ModBlocks#KILL_TRIGGER_CONTROLLER
 */
public class KillTriggerControllerBlockEntity extends ControllerBlockEntityWithArea {
  /**
   * Create a block entity with an empty region.
   */
  public KillTriggerControllerBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntities.KILL_TRIGGER_CONTROLLER, pos, state);
  }

  @Override
  protected void doTick() {
    //noinspection DataFlowIssue
    this.world.getEntitiesByClass(PlayerEntity.class, this.getBox(), LivingEntity::canTakeDamage)
        .forEach(LivingEntity::kill);
  }
}
