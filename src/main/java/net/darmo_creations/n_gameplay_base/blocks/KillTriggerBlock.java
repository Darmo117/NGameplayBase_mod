package net.darmo_creations.n_gameplay_base.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/**
 * A block that kills any survival/adventure player that collides with it.
 */
public class KillTriggerBlock extends Block implements ModBlock {
  public KillTriggerBlock() {
    super(ModBlock.getSettings(FabricBlockSettings.of(Material.AIR).sounds(BlockSoundGroup.STONE).nonOpaque()));
  }

  @Override
  @SuppressWarnings("deprecation")
  public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return VoxelShapes.empty();
  }

  @Override
  @SuppressWarnings("deprecation")
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return VoxelShapes.fullCube();
  }

  @Override
  @SuppressWarnings("deprecation")
  public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
    return 1;
  }

  @Override
  public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
    return true;
  }

  @Override
  @SuppressWarnings("deprecation")
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    if (entity instanceof ServerPlayerEntity p && p.canTakeDamage()) {
      p.kill();
    }
  }

  @Override
  public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
    // Display particle whenever player is holding this block, as with minecraft:barrier and minecraft:light
    ClientPlayerEntity player = MinecraftClient.getInstance().player;
    if (player != null && player.isHolding(this.asItem())) {
      world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK_MARKER, state),
          pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0);
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.INVISIBLE;
  }
}
