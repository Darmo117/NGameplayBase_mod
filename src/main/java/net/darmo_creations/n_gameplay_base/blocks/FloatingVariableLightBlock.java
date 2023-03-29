package net.darmo_creations.n_gameplay_base.blocks;

import net.darmo_creations.n_gameplay_base.Utils;
import net.darmo_creations.n_gameplay_base.block_entities.FloatingVariableLightBlockEntity;
import net.darmo_creations.n_gameplay_base.block_entities.ModBlockEntities;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/**
 * A floating block that emits light. When a player collides it,
 * the block’s light level toggles between two values over several seconds.
 */
public class FloatingVariableLightBlock extends VariableLightBlock implements BlockEntityProvider {
  private static final VoxelShape SHAPE = createCuboidShape(4, 7, 4, 12, 15, 12);

  public FloatingVariableLightBlock() {
    super(FabricBlockSettings.of(Material.STONE, MapColor.WHITE)
        .sounds(BlockSoundGroup.STONE));
    this.setDefaultState(this.getStateManager().getDefaultState().with(LIGHT_LEVEL, FloatingVariableLightBlockEntity.MIN_LIGHT_LEVEL));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return SHAPE;
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
    return type == ModBlockEntities.FLOATING_VARIABLE_LIGHT_BLOCK
        ? (world_, pos, state_, be) -> ((FloatingVariableLightBlockEntity) be).tick()
        : null;
  }

  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new FloatingVariableLightBlockEntity(pos, state);
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    return this.getDefaultState().with(LIGHT_LEVEL, FloatingVariableLightBlockEntity.MIN_LIGHT_LEVEL);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    if (entity instanceof PlayerEntity) {
      Utils.getBlockEntity(FloatingVariableLightBlockEntity.class, world, pos)
          .ifPresent(FloatingVariableLightBlockEntity::onPlayerColliding);
    }
  }
}
