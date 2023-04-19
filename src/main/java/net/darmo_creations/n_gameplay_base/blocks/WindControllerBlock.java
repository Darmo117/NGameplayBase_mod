package net.darmo_creations.n_gameplay_base.blocks;

import net.darmo_creations.n_gameplay_base.Utils;
import net.darmo_creations.n_gameplay_base.block_entities.ModBlockEntities;
import net.darmo_creations.n_gameplay_base.block_entities.WindControllerBlockEntity;
import net.darmo_creations.n_gameplay_base.gui.WindControllerScreen;
import net.darmo_creations.n_gameplay_base.items.WindTweakerItem;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
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

import java.util.Optional;

/**
 * This block lets players configure an area with "wind" that pushes entities in a certain direction.
 *
 * @see WindTweakerItem
 * @see WindControllerBlockEntity
 */
public class WindControllerBlock extends BlockWithEntity implements OperatorBlock, ModBlock {
  public static final BooleanProperty TRIGGERED = Properties.TRIGGERED;

  public WindControllerBlock() {
    // Same settings as command block
    super(ModBlock.getSettings(FabricBlockSettings.of(Material.METAL, MapColor.LIGHT_BLUE).sounds(BlockSoundGroup.METAL)));
    this.setDefaultState(this.getStateManager().getDefaultState().with(TRIGGERED, false));
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    return this.getDefaultState().with(TRIGGERED, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
  }

  @Override
  public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
    super.onPlaced(world, pos, state, placer, itemStack);
    Utils.getBlockEntity(WindControllerBlockEntity.class, world, pos)
        .ifPresent(be -> be.init(state));
  }

  @Override
  @SuppressWarnings("deprecation")
  public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
    boolean powered = world.isReceivingRedstonePower(pos);
    boolean triggered = state.get(TRIGGERED);
    if (powered && !triggered) {
      Utils.getBlockEntity(WindControllerBlockEntity.class, world, pos)
          .ifPresent(e -> e.setActive(true));
      world.setBlockState(pos, state.with(TRIGGERED, true), NOTIFY_ALL);
    } else if (!powered && triggered) {
      world.setBlockState(pos, state.with(TRIGGERED, false), NOTIFY_ALL);
      Utils.getBlockEntity(WindControllerBlockEntity.class, world, pos)
          .ifPresent(e -> e.setActive(false));
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
    Optional<WindControllerBlockEntity> be = Utils.getBlockEntity(WindControllerBlockEntity.class, world, pos);
    if (be.isPresent() && player.isCreativeLevelTwoOp()) {
      if (world.isClient()) {
        MinecraftClient.getInstance().setScreen(new WindControllerScreen(be.get()));
      }
      return ActionResult.SUCCESS;
    }
    return ActionResult.FAIL;
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
    return type == ModBlockEntities.WIND_CONTROLLER
        ? (world_, pos, state_, be) -> ((WindControllerBlockEntity) be).tick()
        : null;
  }

  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new WindControllerBlockEntity(pos, state);
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
