package net.darmo_creations.n_gameplay_base.gui;

import net.darmo_creations.n_gameplay_base.block_entities.WindControllerBlockEntity;
import net.darmo_creations.n_gameplay_base.network.C2SPacketFactory;
import net.darmo_creations.n_gameplay_base.network.packets.WindControllerDataPacket;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

/**
 * GUI for the wind controller block.
 */
public class WindControllerScreen extends Screen {
  // Widgets
  private TextFieldWidget xSpeedTextField;
  private TextFieldWidget ySpeedTextField;
  private TextFieldWidget zSpeedTextField;

  // Data
  private final WindControllerBlockEntity blockEntity;
  private final boolean active;
  private final Vec3d windDirection;

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
  public WindControllerScreen(WindControllerBlockEntity blockEntity) {
    super(Text.translatable("gui.n_gameplay_base.wind_controller.title"));
    this.blockEntity = blockEntity;
    this.active = blockEntity.isTriggered();
    this.windDirection = blockEntity.getWindDirection();
  }

  @Override
  protected void init() {
    super.init();

    final int middle = this.width / 2;
    int btnW = BUTTON_WIDTH / 2;
    int y = (this.height - BUTTON_HEIGHT) / 2;
    //noinspection DataFlowIssue
    this.xSpeedTextField = this.addDrawableChild(
        new TextFieldWidget(this.client.textRenderer, middle - 2 * btnW, y, btnW, BUTTON_HEIGHT, null));
    this.xSpeedTextField.setText(String.valueOf(this.windDirection.getX()));
    this.ySpeedTextField = this.addDrawableChild(
        new TextFieldWidget(this.client.textRenderer, middle - btnW / 2, y, btnW, BUTTON_HEIGHT, null));
    this.ySpeedTextField.setText(String.valueOf(this.windDirection.getY()));
    this.zSpeedTextField = this.addDrawableChild(
        new TextFieldWidget(this.client.textRenderer, middle + btnW, y, btnW, BUTTON_HEIGHT, null));
    this.zSpeedTextField.setText(String.valueOf(this.windDirection.getZ()));

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
    Vec3d dir = new Vec3d(
        getValue(this.xSpeedTextField),
        getValue(this.ySpeedTextField),
        getValue(this.zSpeedTextField)
    );
    this.blockEntity.setWindDirection(dir);
    C2SPacketFactory.sendPacket(
        new WindControllerDataPacket(this.blockEntity.getPos(), dir));
    //noinspection ConstantConditions
    this.client.setScreen(null);
  }

  private static double getValue(final TextFieldWidget TextFieldWidget) {
    try {
      return Double.parseDouble(TextFieldWidget.getText());
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
            "gui.n_gameplay_base.wind_controller.active_label."
                + (this.active ? "active" : "inactive")
        ).setStyle(Style.EMPTY.withColor(this.active ? Formatting.GREEN : Formatting.RED)),
        middle,
        (this.height - fontHeight) / 4,
        0xffffff
    );

    drawTextWithShadow(matrices, this.textRenderer,
        Text.translatable("gui.n_gameplay_base.wind_controller.x_speed_widget.label"),
        this.xSpeedTextField.x, this.xSpeedTextField.y + BUTTON_HEIGHT + fontHeight / 2, 0xa0a0a0);
    drawTextWithShadow(matrices, this.textRenderer,
        Text.translatable("gui.n_gameplay_base.wind_controller.y_speed_widget.label"),
        this.ySpeedTextField.x, this.ySpeedTextField.y + BUTTON_HEIGHT + fontHeight / 2, 0xa0a0a0);
    drawTextWithShadow(matrices, this.textRenderer,
        Text.translatable("gui.n_gameplay_base.wind_controller.z_speed_widget.label"),
        this.zSpeedTextField.x, this.zSpeedTextField.y + BUTTON_HEIGHT + fontHeight / 2, 0xa0a0a0);
  }
}
