package net.darmo_creations.n_gameplay_base.blocks;

import net.darmo_creations.n_gameplay_base.Utils;
import net.darmo_creations.n_gameplay_base.block_entities.ControllerBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
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

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * Base class for controller blocks.
 *
 * @param <BE> Type of associated block entity.
 */
public abstract class ControllerBlock<BE extends ControllerBlockEntity>
    extends BlockWithEntity
    implements OperatorBlock, ModBlock {
  public static final BooleanProperty TRIGGERED = Properties.TRIGGERED;

  private final Class<BE> blockEntityClass;

  protected ControllerBlock(MapColor mapColor, Class<BE> blockEntityClass) {
    // Same settings as command blocks
    super(ModBlock.getSettings(FabricBlockSettings.of(Material.METAL, mapColor).sounds(BlockSoundGroup.METAL)));
    this.blockEntityClass = blockEntityClass;
    this.setDefaultState(this.getStateManager().getDefaultState().with(TRIGGERED, false));
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    return this.getDefaultState().with(TRIGGERED, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
  }

  @Override
  public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
    super.onPlaced(world, pos, state, placer, itemStack);
    Utils.getBlockEntity(this.blockEntityClass, world, pos)
        .ifPresent(be -> this.onPlaced(be, world, pos, state, placer, itemStack));
  }

  /**
   * Called when this block is placed.
   *
   * @param be The newly created block entity.
   */
  protected void onPlaced(BE be, World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
  }

  @Override
  public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
    Utils.getBlockEntity(this.blockEntityClass, world, pos)
        .ifPresent(be -> this.onBreak(be, world, pos, state, player));
    super.onBreak(world, pos, state, player);
  }

  /**
   * Called right before a block is broken.
   *
   * @param be The block entity that will be removed.
   */
  protected void onBreak(BE be, World world, BlockPos pos, BlockState state, PlayerEntity player) {
  }

  @Override
  @SuppressWarnings("deprecation")
  public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
    boolean powered = world.isReceivingRedstonePower(pos);
    boolean triggered = state.get(TRIGGERED);
    if (powered && !triggered) {
      Utils.getBlockEntity(this.blockEntityClass, world, pos)
          .ifPresent(e -> {
            e.setTriggered(true);
            this.onTriggerRising(e, state, world, pos, sourceBlock, sourcePos);
          });
      world.setBlockState(pos, state.with(TRIGGERED, true), NOTIFY_ALL);
    } else if (!powered && triggered) {
      world.setBlockState(pos, state.with(TRIGGERED, false), NOTIFY_ALL);
      Utils.getBlockEntity(this.blockEntityClass, world, pos)
          .ifPresent(e -> {
            e.setTriggered(false);
            this.onTriggerFalling(e, state, world, pos, sourceBlock, sourcePos);
          });
    }
  }

  /**
   * Called when this block’s {@link #TRIGGERED} state goes from false to true.
   *
   * @param be The associated block entity.
   */
  protected void onTriggerRising(BE be, BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos) {
  }

  /**
   * Called when this block’s {@link #TRIGGERED} state goes from true to false.
   *
   * @param be The associated block entity.
   */
  protected void onTriggerFalling(BE be, BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos) {
  }

  @SuppressWarnings("deprecation")
  @Override
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
    if (!this.hasScreen()) {
      return ActionResult.FAIL;
    }
    Optional<BE> be = Utils.getBlockEntity(this.blockEntityClass, world, pos);
    if (be.isPresent() && player.isCreativeLevelTwoOp()) {
      if (world.isClient()) {
        MinecraftClient.getInstance().setScreen(this.getScreen(be.get()));
      }
      return ActionResult.SUCCESS;
    }
    return ActionResult.FAIL;
  }

  /**
   * {@return whether to show a screen when this block is used.}
   */
  protected boolean hasScreen() {
    return true;
  }

  /**
   * {@return the screen to show when this block is used.}
   *
   * @param be The block’s entity.
   */
  protected Screen getScreen(BE be) {
    return null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
    return type == this.getBlockEntityType()
        ? (world_, pos, state_, be) -> ((BE) be).tick()
        : null;
  }

  /**
   * {@return the BlockEntityType for the associated block entity.}
   */
  protected abstract BlockEntityType<BE> getBlockEntityType();

  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    try {
      return this.blockEntityClass.getConstructor(BlockPos.class, BlockState.class).newInstance(pos, state);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
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
