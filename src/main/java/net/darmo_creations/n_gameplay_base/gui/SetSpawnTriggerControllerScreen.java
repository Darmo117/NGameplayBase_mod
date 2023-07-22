package net.darmo_creations.n_gameplay_base.gui;

import net.darmo_creations.n_gameplay_base.block_entities.SetSpawnTriggerControllerBlockEntity;
import net.darmo_creations.n_gameplay_base.network.C2SPacketFactory;
import net.darmo_creations.n_gameplay_base.network.packets.SetSpawnTriggerControllerDataPacket;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.lwjgl.glfw.GLFW;

/**
 * GUI for the set-spawn trigger controller block.
 */
public class SetSpawnTriggerControllerScreen extends Screen {
  // Widgets
  private TextFieldWidget respawnXTextField;
  private TextFieldWidget respawnYTextField;
  private TextFieldWidget respawnZTextField;
  private CyclingButtonWidget<Direction> respawnFacingButton;

  // Data
  private final SetSpawnTriggerControllerBlockEntity blockEntity;
  private final boolean active;

  // Layout
  public static final int TITLE_MARGIN = 30;
  public static final int MARGIN = 4;
  public static final int BUTTON_WIDTH = 150;
  public static final int BUTTON_HEIGHT = 20;

  /**
   * Creates a GUI for the given block entity.
   *
   * @param blockEntity The block entity.
   */
  public SetSpawnTriggerControllerScreen(SetSpawnTriggerControllerBlockEntity blockEntity) {
    super(Text.translatable("gui.n_gameplay_base.set_spawn_trigger_controller.title"));
    this.blockEntity = blockEntity;
    this.active = blockEntity.isTriggered();
  }

  @Override
  protected void init() {
    super.init();

    final int middle = this.width / 2;
    int btnW = BUTTON_WIDTH / 2;
    int y = (this.height - BUTTON_HEIGHT) / 2;
    BlockPos respawnPos = this.blockEntity.getRespawnPosition();
    //noinspection DataFlowIssue
    this.respawnXTextField = this.addDrawableChild(
        new TextFieldWidget(this.client.textRenderer, middle - 2 * btnW, y, btnW, BUTTON_HEIGHT, null));
    this.respawnXTextField.setText(String.valueOf(respawnPos.getX()));
    this.respawnYTextField = this.addDrawableChild(
        new TextFieldWidget(this.client.textRenderer, middle - btnW / 2, y, btnW, BUTTON_HEIGHT, null));
    this.respawnYTextField.setText(String.valueOf(respawnPos.getY()));
    this.respawnZTextField = this.addDrawableChild(
        new TextFieldWidget(this.client.textRenderer, middle + btnW, y, btnW, BUTTON_HEIGHT, null));
    this.respawnZTextField.setText(String.valueOf(respawnPos.getZ()));

    y += BUTTON_HEIGHT * 3;
    this.respawnFacingButton = this.addDrawableChild(
        CyclingButtonWidget.<Direction>builder(d -> Text.translatable("direction." + d.asString()))
            .values(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST)
            .initially(this.blockEntity.getRespawnFacing())
            .build(middle - btnW, y, BUTTON_WIDTH, BUTTON_HEIGHT,
                Text.translatable("gui.n_gameplay_base.set_spawn_trigger_controller.respawn_facing_button.label")));

    final int leftButtonX = middle - BUTTON_WIDTH - MARGIN;
    final int rightButtonX = middle + MARGIN;
    int bottomY = this.height - BUTTON_HEIGHT - MARGIN;
    this.addDrawableChild(new ButtonWidget(
        leftButtonX, bottomY, BUTTON_WIDTH, BUTTON_HEIGHT,
        Text.translatable("gui.done"),
        b -> this.onDone()
    ));
    this.addDrawableChild(new ButtonWidget(
        rightButtonX, bottomY, BUTTON_WIDTH, BUTTON_HEIGHT,
        Text.translatable("gui.cancel"),
        button -> this.client.setScreen(null)
    ));
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
      this.onDone();
      return true;
    } else {
      return super.keyPressed(keyCode, scanCode, modifiers);
    }
  }

  private void onDone() {
    BlockPos pos = new BlockPos(
        getValue(this.respawnXTextField),
        getValue(this.respawnYTextField),
        getValue(this.respawnZTextField)
    );
    this.blockEntity.setRespawnPosition(pos);
    Direction facing = this.respawnFacingButton.getValue();
    this.blockEntity.setRespawnFacing(facing);
    C2SPacketFactory.sendPacket(
        new SetSpawnTriggerControllerDataPacket(this.blockEntity.getPos(), pos, facing));
    //noinspection ConstantConditions
    this.client.setScreen(null);
  }

  private static int getValue(final TextFieldWidget TextFieldWidget) {
    try {
      return Integer.parseInt(TextFieldWidget.getText());
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  @Override
  public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    this.renderBackground(matrices);

    //noinspection ConstantConditions
    final int fontHeight = this.client.textRenderer.fontHeight;
    final int middle = this.width / 2;

    super.render(matrices, mouseX, mouseY, delta);

    drawCenteredText(
        matrices,
        this.textRenderer,
        this.getTitle(),
        middle,
        (TITLE_MARGIN - fontHeight) / 2,
        0xffffff
    );
    drawCenteredText(
        matrices,
        this.textRenderer,
        Text.translatable(
            "gui.n_gameplay_base.set_spawn_trigger_controller.active_label."
                + (this.active ? "active" : "inactive")
        ).setStyle(Style.EMPTY.withColor(this.active ? Formatting.GREEN : Formatting.RED)),
        middle,
        (this.height - fontHeight) / 4,
        0xffffff
    );

    drawTextWithShadow(matrices, this.textRenderer,
        Text.translatable("gui.n_gameplay_base.set_spawn_trigger_controller.respawn_x_widget.label"),
        this.respawnXTextField.x, this.respawnXTextField.y + BUTTON_HEIGHT + fontHeight / 2, 0xa0a0a0);
    drawTextWithShadow(matrices, this.textRenderer,
        Text.translatable("gui.n_gameplay_base.set_spawn_trigger_controller.respawn_y_widget.label"),
        this.respawnYTextField.x, this.respawnYTextField.y + BUTTON_HEIGHT + fontHeight / 2, 0xa0a0a0);
    drawTextWithShadow(matrices, this.textRenderer,
        Text.translatable("gui.n_gameplay_base.set_spawn_trigger_controller.respawn_z_widget.label"),
        this.respawnZTextField.x, this.respawnZTextField.y + BUTTON_HEIGHT + fontHeight / 2, 0xa0a0a0);
  }
}
