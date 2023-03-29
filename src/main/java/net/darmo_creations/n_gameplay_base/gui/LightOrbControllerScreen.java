package net.darmo_creations.n_gameplay_base.gui;

import net.darmo_creations.n_gameplay_base.block_entities.LightOrbControllerBlockEntity;
import net.darmo_creations.n_gameplay_base.block_entities.PathCheckpoint;
import net.darmo_creations.n_gameplay_base.network.C2SPacketFactory;
import net.darmo_creations.n_gameplay_base.network.packets.LightOrbControllerDataPacket;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableTextContent;
import org.lwjgl.glfw.GLFW;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * GUI for the light orb controller block.
 */
public class LightOrbControllerScreen extends Screen {
  // Buttons
  private ButtonWidget statusBtn;
  private ButtonWidget loopBtn;
  private ButtonWidget invisibilityBtn;
  private PathCheckpointListWidget checkpointList;
  private int checkpointListY;

  // Data
  private final LightOrbControllerBlockEntity blockEntity;
  private boolean active;
  private boolean loops;
  private boolean invisible;
  private int lightLevel;
  private double speed;
  private List<PathCheckpoint> checkpoints;

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
  public LightOrbControllerScreen(LightOrbControllerBlockEntity blockEntity) {
    super(MutableText.of(new TranslatableTextContent("gui.n_gameplay_base.light_orb_controller.title")));
    this.blockEntity = blockEntity;
    this.active = blockEntity.isActive();
    this.loops = blockEntity.loops();
    this.invisible = blockEntity.isEntityInvisible();
    this.lightLevel = blockEntity.getLightLevel();
    this.speed = blockEntity.getSpeed();
    this.checkpoints = blockEntity.getCheckpoints();
  }

  @Override
  protected void init() {
    super.init();
    final int middle = this.width / 2;
    final int leftButtonX = middle - BUTTON_WIDTH - MARGIN;
    final int rightButtonX = middle + MARGIN;
    //noinspection ConstantConditions
    final int fontHeight = this.client.textRenderer.fontHeight;

    int topY = TITLE_MARGIN;

    int btnW = (int) (BUTTON_WIDTH * 2 / 3.0);
    this.statusBtn = ButtonWidget
        .builder(
            MutableText.of(new TranslatableTextContent(
                "gui.n_gameplay_base.light_orb_controller.status_button."
                    + (this.active ? "active" : "inactive"))
            ),
            button -> {
              this.active = !this.active;
              this.statusBtn.setMessage(MutableText.of(new TranslatableTextContent(
                  "gui.n_gameplay_base.light_orb_controller.status_button."
                      + (this.active ? "active" : "inactive")
              )));
            }
        )
        .dimensions(leftButtonX, topY, btnW, BUTTON_HEIGHT)
        .build();

    this.loopBtn = ButtonWidget
        .builder(
            MutableText.of(new TranslatableTextContent(
                "gui.n_gameplay_base.light_orb_controller.loop_button."
                    + (this.loops ? "active" : "inactive"))
            ),
            button -> {
              this.loops = !this.loops;
              this.loopBtn.setMessage(MutableText.of(new TranslatableTextContent(
                  "gui.n_gameplay_base.light_orb_controller.loop_button."
                      + (this.loops ? "active" : "inactive")
              )));
            }
        )
        .dimensions(middle - btnW / 2, topY, btnW, BUTTON_HEIGHT)
        .build();

    this.invisibilityBtn = ButtonWidget
        .builder(
            MutableText.of(new TranslatableTextContent(
                "gui.n_gameplay_base.light_orb_controller.invisibility_button."
                    + (this.invisible ? "active" : "inactive"))
            ),
            button -> {
              this.invisible = !this.invisible;
              this.invisibilityBtn.setMessage(MutableText.of(new TranslatableTextContent(
                  "gui.n_gameplay_base.light_orb_controller.invisibility_button."
                      + (this.invisible ? "active" : "inactive"))
              ));
            }
        )
        .dimensions(rightButtonX + BUTTON_WIDTH - btnW, topY, btnW, BUTTON_HEIGHT)
        .build();

    topY += BUTTON_HEIGHT + MARGIN;

    SliderWidget lightLevelSlider = new IntSliderWidget(
        leftButtonX, topY,
        BUTTON_WIDTH, BUTTON_HEIGHT,
        0, 15, this.lightLevel,
        value -> this.lightLevel = value,
        value -> MutableText.of(new TranslatableTextContent(
            "gui.n_gameplay_base.light_orb_controller.light_level_slider",
            value
        ))
    );

    SliderWidget speedSlider = new DoubleSliderWidget(
        rightButtonX, topY,
        BUTTON_WIDTH, BUTTON_HEIGHT,
        0, 1, this.speed,
        value -> this.speed = this.getTrueSpeedValue(value),
        value -> MutableText.of(new TranslatableTextContent(
            "gui.n_gameplay_base.light_orb_controller.speed_slider",
            this.getTrueSpeedValue(value)
        ))
    );

    topY += BUTTON_HEIGHT + 2 * MARGIN + fontHeight;
    int bottomY = this.height - BUTTON_HEIGHT - MARGIN;

    ButtonWidget doneBtn = ButtonWidget
        .builder(
            MutableText.of(new TranslatableTextContent("gui.done")),
            b -> this.onDone()
        )
        .dimensions(leftButtonX, bottomY, BUTTON_WIDTH, BUTTON_HEIGHT)
        .build();

    ButtonWidget cancelBtn = ButtonWidget
        .builder(
            MutableText.of(new TranslatableTextContent("gui.cancel")),
            button -> this.client.setScreen(null)
        )
        .dimensions(rightButtonX, bottomY, BUTTON_WIDTH, BUTTON_HEIGHT)
        .build();

    bottomY -= MARGIN;

    this.checkpointList = this.addDrawableChild(new PathCheckpointListWidget(
        this.client,
        this.width, this.height,
        this.checkpointListY = topY, bottomY,
        BUTTON_HEIGHT + 4
    ));
    this.checkpointList.populateEntries(this.checkpoints);

    // Add widgets after list as it renders a background
    this.addDrawableChild(this.statusBtn);
    this.addDrawableChild(this.loopBtn);
    this.addDrawableChild(this.invisibilityBtn);
    this.addDrawableChild(lightLevelSlider);
    this.addDrawableChild(speedSlider);
    this.addDrawableChild(doneBtn);
    this.addDrawableChild(cancelBtn);
  }

  /**
   * Get the actual speed value from the given float.
   */
  private float getTrueSpeedValue(double v) {
    return new BigDecimal(v).setScale(2, RoundingMode.HALF_UP).floatValue();
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
    this.blockEntity.setActive(this.active);
    this.blockEntity.setLoops(this.loops);
    this.blockEntity.setEntityInvisible(this.invisible);
    this.blockEntity.setLightLevel(this.lightLevel);
    this.blockEntity.setSpeed(this.speed);
    this.checkpoints = this.checkpointList.getEntries();
    this.blockEntity.setCheckpoints(this.checkpoints);
    C2SPacketFactory.sendPacket(
        new LightOrbControllerDataPacket(this.blockEntity.getPos(), this.active, this.loops,
            this.invisible, this.lightLevel, this.speed, this.checkpoints));
    //noinspection ConstantConditions
    this.client.setScreen(null);
  }

  @Override
  public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
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
        MutableText.of(new TranslatableTextContent(
            "gui.n_gameplay_base.light_orb_controller.checkpoint_list.title", this.checkpointList.children().size())
        ),
        middle,
        this.checkpointListY - MARGIN - fontHeight,
        0xffffff
    );
  }
}
