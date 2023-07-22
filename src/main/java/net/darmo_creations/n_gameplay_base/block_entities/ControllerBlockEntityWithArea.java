package net.darmo_creations.n_gameplay_base.block_entities;

import net.darmo_creations.n_gameplay_base.blocks.KillTriggerControllerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public abstract class ControllerBlockEntityWithArea extends ControllerBlockEntity {
  private static final String CORNER1_TAG_KEY = "Corner1";
  private static final String CORNER2_TAG_KEY = "Corner2";

  /**
   * Lower and upper corners of the region.
   */
  private BlockPos lowerCorner, upperCorner;
  /**
   * Actual corners of the trigger region as set by players.
   */
  private BlockPos corner1, corner2;

  /**
   * Create a block entity with an empty region.
   */
  public ControllerBlockEntityWithArea(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }

  /**
   * Initialize this block entity with a region of 1x1x1 right above the associated block.
   *
   * @param blockState State of the associated block.
   */
  public void init(BlockState blockState) {
    this.setTriggered(blockState.get(KillTriggerControllerBlock.TRIGGERED));
    this.addCorner(this.getPos().up());
    this.addCorner(this.getPos().up());
  }

  /**
   * Executes one tick of this block entity’s logic.
   */
  @Override
  public void tick() {
    if (this.isTriggered() && this.lowerCorner != null && this.upperCorner != null && this.world != null) {
      this.doTick();
    }
  }

  protected abstract void doTick();

  public Box getBox() {
    return new Box(
        this.lowerCorner.getX(), this.lowerCorner.getY(), this.lowerCorner.getZ(),
        this.upperCorner.getX() + 1, this.upperCorner.getY() + 1, this.upperCorner.getZ() + 1
    );
  }

  public void addCorner(BlockPos pos) {
    this.corner2 = this.corner1;
    this.corner1 = pos;
    this.updateLowerUpperCorners();
    this.markDirty();
  }

  public BlockPos getLowerCorner() {
    return this.lowerCorner;
  }

  public BlockPos getUpperCorner() {
    return this.upperCorner;
  }

  private void updateLowerUpperCorners() {
    if (this.corner1 != null && this.corner2 != null) {
      this.lowerCorner = new BlockPos(
          Math.min(this.corner1.getX(), this.corner2.getX()),
          Math.min(this.corner1.getY(), this.corner2.getY()),
          Math.min(this.corner1.getZ(), this.corner2.getZ())
      );
      this.upperCorner = new BlockPos(
          Math.max(this.corner1.getX(), this.corner2.getX()),
          Math.max(this.corner1.getY(), this.corner2.getY()),
          Math.max(this.corner1.getZ(), this.corner2.getZ())
      );
    }
  }

  @Override
  protected void writeNbt(NbtCompound nbt) {
    super.writeNbt(nbt);
    nbt.put(CORNER1_TAG_KEY, NbtHelper.fromBlockPos(this.corner1));
    nbt.put(CORNER2_TAG_KEY, NbtHelper.fromBlockPos(this.corner2));
  }

  @Override
  public void readNbt(NbtCompound nbt) {
    super.readNbt(nbt);
    this.corner1 = NbtHelper.toBlockPos(nbt.getCompound(CORNER1_TAG_KEY));
    this.corner2 = NbtHelper.toBlockPos(nbt.getCompound(CORNER2_TAG_KEY));
    this.updateLowerUpperCorners();
  }
}
