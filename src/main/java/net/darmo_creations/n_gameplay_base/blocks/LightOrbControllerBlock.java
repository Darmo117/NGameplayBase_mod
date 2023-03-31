package net.darmo_creations.n_gameplay_base.blocks;

import net.darmo_creations.n_gameplay_base.Utils;
import net.darmo_creations.n_gameplay_base.block_entities.LightOrbControllerBlockEntity;
import net.darmo_creations.n_gameplay_base.block_entities.ModBlockEntities;
import net.darmo_creations.n_gameplay_base.gui.LightOrbControllerScreen;
import net.darmo_creations.n_gameplay_base.items.LightOrbTweakerItem;
import net.darmo_creations.n_gameplay_base.items.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * This block lets players configure light orbs through the use of a special tool and a configuration GUI.
 *
 * @see LightOrbTweakerItem
 * @see LightOrbControllerBlockEntity
 */
public class LightOrbControllerBlock extends BlockWithEntity implements OperatorBlock, ModBlock {
  public static final BooleanProperty TRIGGERED = Properties.TRIGGERED;

  public LightOrbControllerBlock() {
    // Same settings as command block
    super(ModBlock.getSettings(FabricBlockSettings.of(Material.METAL, MapColor.WHITE).sounds(BlockSoundGroup.METAL)));
    this.setDefaultState(this.getStateManager().getDefaultState().with(TRIGGERED, false));
  }

  @Override
  public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
    super.onPlaced(world, pos, state, placer, itemStack);
    Utils.getBlockEntity(LightOrbControllerBlockEntity.class, world, pos)
        .ifPresent(LightOrbControllerBlockEntity::init);
  }

  @Override
  public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
    Utils.getBlockEntity(LightOrbControllerBlockEntity.class, world, pos)
        .ifPresent(LightOrbControllerBlockEntity::onRemoved);
    super.onBreak(world, pos, state, player);
  }

  @Override
  @SuppressWarnings("deprecation")
  public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
    boolean powered = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.up());
    boolean triggered = state.get(TRIGGERED);
    if (powered && !triggered) {
      Utils.getBlockEntity(LightOrbControllerBlockEntity.class, world, pos)
          .ifPresent(LightOrbControllerBlockEntity::resetOrb);
      world.setBlockState(pos, state.with(TRIGGERED, true), NOTIFY_ALL);
    } else if (!powered && triggered) {
      world.setBlockState(pos, state.with(TRIGGERED, false), NOTIFY_ALL);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
    Optional<LightOrbControllerBlockEntity> be = Utils.getBlockEntity(LightOrbControllerBlockEntity.class, world, pos);
    if (be.isPresent() && player.isCreativeLevelTwoOp() && player.getStackInHand(hand).getItem() == ModItems.LIGHT_ORB_TWEAKER) {
      if (world.isClient()) {
        MinecraftClient.getInstance().setScreen(new LightOrbControllerScreen(be.get()));
      }
      return ActionResult.SUCCESS;
    } else {
      return ActionResult.FAIL;
    }
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
    return type == ModBlockEntities.LIGHT_ORB_CONTROLLER
        ? (world_, pos, state_, be) -> ((LightOrbControllerBlockEntity) be).tick()
        : null;
  }

  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new LightOrbControllerBlockEntity(pos, state);
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    builder.add(TRIGGERED);
  }

  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.MODEL;
  }
}
