package net.darmo_creations.n_gameplay_base.blocks;

import net.darmo_creations.n_gameplay_base.block_entities.LightOrbControllerBlockEntity;
import net.darmo_creations.n_gameplay_base.block_entities.ModBlockEntities;
import net.darmo_creations.n_gameplay_base.gui.LightOrbControllerScreen;
import net.darmo_creations.n_gameplay_base.items.LightOrbTweakerItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This block lets players configure light orbs through the use of a special tool and a configuration GUI.
 *
 * @see LightOrbTweakerItem
 * @see LightOrbControllerBlockEntity
 */
public class LightOrbControllerBlock extends ControllerBlock<LightOrbControllerBlockEntity> {
  public LightOrbControllerBlock() {
    super(MapColor.WHITE, LightOrbControllerBlockEntity.class);
  }

  @Override
  protected void onPlaced(LightOrbControllerBlockEntity be, World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
    be.init();
  }

  @Override
  protected void onBreak(LightOrbControllerBlockEntity lightOrbControllerBlockEntity, World world, BlockPos pos, BlockState state, PlayerEntity player) {
    lightOrbControllerBlockEntity.onRemoved();
  }

  @Override
  protected void onTriggerRising(LightOrbControllerBlockEntity be, BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos) {
    be.resetOrb();
  }

  @Override
  protected BlockEntityType<LightOrbControllerBlockEntity> getBlockEntityType() {
    return ModBlockEntities.LIGHT_ORB_CONTROLLER;
  }

  @Override
  protected Screen getScreen(LightOrbControllerBlockEntity be) {
    return new LightOrbControllerScreen(be);
  }
}
