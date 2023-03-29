package net.darmo_creations.n_gameplay_base.gui;

import net.darmo_creations.n_gameplay_base.Utils;
import net.darmo_creations.n_gameplay_base.block_entities.PathCheckpoint;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableTextContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Custom list GUI component that displays a list of checkpoints with buttons to edit/delete/reorder them.
 */
public class PathCheckpointListWidget extends EntryListWidget<PathCheckpointListWidget.GuiEntry> {
  public PathCheckpointListWidget(MinecraftClient client, int width, int height, int top, int bottom, int slotHeight) {
    super(client, width, height, top, bottom, slotHeight);
  }

  @Override
  public void appendNarrations(NarrationMessageBuilder builder) {
  }

  /**
   * Set the entries of this list.
   */
  public void populateEntries(List<PathCheckpoint> entries) {
    this.replaceEntries(entries.stream().map(GuiEntry::new).collect(Collectors.toList()));
    this.updateEntriesButtons();
  }

  /**
   * Get all entries as they have been updated by player.
   */
  public List<PathCheckpoint> getEntries() {
    List<PathCheckpoint> list = new ArrayList<>();
    for (int i = 0; i < this.children().size(); i++) {
      list.add(this.getEntry(i).getCheckpoint());
    }
    return list;
  }

  @Override
  protected int getScrollbarPositionX() {
    return super.getScrollbarPositionX() + 35;
  }

  /**
   * Called when the "stop" button is clicked on an entry.
   */
  protected void onStopButtonClicked() {
    this.updateEntriesButtons();
  }

  /**
   * Called when the "delete" button is clicked on an entry.
   */
  protected void onDeleteButtonClicked(int entryIndex) {
    this.children().remove(entryIndex);
    this.updateEntriesButtons();
  }

  /**
   * Called when the "move up" button is clicked on an entry.
   */
  protected void onMoveUpButtonClicked(int entryIndex) {
    GuiEntry entry = this.children().get(entryIndex);
    this.children().set(entryIndex, this.children().get(entryIndex - 1));
    this.children().set(entryIndex - 1, entry);
    this.updateEntriesButtons();
  }

  /**
   * Called when the "move down" button is clicked on an entry.
   */
  protected void onMoveDownButtonClicked(int entryIndex) {
    GuiEntry entry = this.children().get(entryIndex);
    this.children().set(entryIndex, this.children().get(entryIndex + 1));
    this.children().set(entryIndex + 1, entry);
    this.updateEntriesButtons();
  }

  /**
   * Updates buttons states of all entries.
   */
  private void updateEntriesButtons() {
    int size = this.children().size();
    for (int i = 0; i < size; i++) {
      GuiEntry entry = this.children().get(i);
      entry.setDeleteButtonEnabled(size > 1);
      entry.setMoveUpButtonEnabled(i > 0);
      entry.setMoveDownButtonEnabled(i < size - 1);
    }
  }

  /**
   * A list entry for a single checkpoint. Displays its coordinates and buttons to edit it.
   */
  public class GuiEntry extends Entry<GuiEntry> {
    private final TextFieldWidget ticksTextField;
    private final ButtonWidget stopBtn;
    private final ButtonWidget deleteBtn;
    private final ButtonWidget moveUpBtn;
    private final ButtonWidget moveDownBtn;
    private final List<Element> elements; // List of all elements for easier access in event methods

    private final PathCheckpoint checkpoint;

    /**
     * Create an entry for the given checkpoint.
     */
    public GuiEntry(PathCheckpoint checkpoint) {
      this.checkpoint = checkpoint;

      this.ticksTextField = new TextFieldWidget(PathCheckpointListWidget.this.client.textRenderer, 0, 0, 30, 20,
          MutableText.of(new TranslatableTextContent("gui.n_gameplay_base.light_orb_controllerr.checkpoint_list.entry.wait_field")));
      this.ticksTextField.setText("" + this.checkpoint.getTicksToWait());
      this.ticksTextField.setChangedListener(s -> {
        int i = -1;
        try {
          i = Integer.parseInt(this.ticksTextField.getText());
        } catch (NumberFormatException ignored) {
        }
        if (i >= 0) {
          this.checkpoint.setTicksToWait(i);
        }
      });

      this.stopBtn = new ButtonWidget(
          0, 0, 50, 20,
          MutableText.of(new TranslatableTextContent(
              "gui.n_gameplay_base.light_orb_controller.checkpoint_list.entry.stop_button."
                  + (this.checkpoint.isStop() ? "active" : "inactive")
          )),
          button -> {
            this.checkpoint.setStop(!this.checkpoint.isStop());
            button.setMessage(MutableText.of(new TranslatableTextContent(
                ("gui.n_gameplay_base.light_orb_controller.checkpoint_list.entry.stop_button."
                    + (this.checkpoint.isStop() ? "active" : "inactive"))
            )));
            PathCheckpointListWidget.this.onStopButtonClicked();
          }
      );

      this.deleteBtn = new ButtonWidget(
          0, 0, 20, 20,
          MutableText.of(new TranslatableTextContent(
              "gui.n_gameplay_base.light_orb_controller.checkpoint_list.entry.delete_button"
          )),
          button -> PathCheckpointListWidget.this.onDeleteButtonClicked(this.index())
      );

      this.moveUpBtn = new ButtonWidget(
          0, 0, 20, 20,
          MutableText.of(new TranslatableTextContent(
              "gui.n_gameplay_base.light_orb_controller.checkpoint_list.entry.move_up_button"
          )),
          button -> PathCheckpointListWidget.this.onMoveUpButtonClicked(this.index())
      );

      this.moveDownBtn = new ButtonWidget(
          0, 0, 20, 20,
          MutableText.of(new TranslatableTextContent(
              "gui.n_gameplay_base.light_orb_controller.checkpoint_list.entry.move_down_button"
          )),
          button -> PathCheckpointListWidget.this.onMoveDownButtonClicked(this.index())
      );

      this.elements = Arrays.asList(this.ticksTextField, this.stopBtn, this.deleteBtn, this.moveUpBtn, this.moveDownBtn);
    }

    public PathCheckpoint getCheckpoint() {
      return this.checkpoint;
    }

    public int index() {
      return PathCheckpointListWidget.this.children().indexOf(this);
    }

    public void setDeleteButtonEnabled(boolean enabled) {
      this.deleteBtn.active = enabled;
    }

    public void setMoveUpButtonEnabled(boolean enabled) {
      this.moveUpBtn.active = enabled;
    }

    public void setMoveDownButtonEnabled(boolean enabled) {
      this.moveDownBtn.active = enabled;
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
      MinecraftClient client = PathCheckpointListWidget.this.client;

      int xOffset = entryWidth - this.deleteBtn.getWidth();
      this.deleteBtn.x = x + xOffset;
      this.deleteBtn.y = y + (entryHeight - this.deleteBtn.getHeight()) / 2;
      this.deleteBtn.renderButton(matrices, mouseX, mouseY, tickDelta);

      xOffset -= this.moveUpBtn.getWidth() + 4;
      this.moveUpBtn.x = x + xOffset;
      this.moveUpBtn.y = y + (entryHeight - this.moveUpBtn.getHeight()) / 2;
      this.moveUpBtn.renderButton(matrices, mouseX, mouseY, tickDelta);

      xOffset -= this.moveDownBtn.getWidth() + 4;
      this.moveDownBtn.x = x + xOffset;
      this.moveDownBtn.y = y + (entryHeight - this.moveDownBtn.getHeight()) / 2;
      this.moveDownBtn.renderButton(matrices, mouseX, mouseY, tickDelta);

      xOffset -= this.stopBtn.getWidth() + 4;
      this.stopBtn.x = x + xOffset;
      this.stopBtn.y = y + (entryHeight - this.stopBtn.getHeight()) / 2;
      this.stopBtn.renderButton(matrices, mouseX, mouseY, tickDelta);

      xOffset -= this.ticksTextField.getWidth() + 6;
      this.ticksTextField.x = x + xOffset;
      this.ticksTextField.y = y + (entryHeight - this.ticksTextField.getHeight()) / 2;
      this.ticksTextField.render(matrices, mouseX, mouseY, tickDelta);

      int textY = y + (entryHeight - client.textRenderer.fontHeight) / 2;
      String text = Utils.blockPosToString(this.checkpoint.getPos()) + " » "
          + I18n.translate("gui.n_gameplay_base.light_orb_controller.checkpoint_list.entry.wait_field");
      xOffset -= client.textRenderer.getWidth(text) + 4;
      client.textRenderer.draw(matrices, text, x + xOffset, textY, 0xffffff);
    }

    /*
     * Forward all events to sub-elements
     */

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
      return this.elements.stream().anyMatch(element -> element.mouseClicked(mouseX, mouseY, button))
          || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
      return this.elements.stream().anyMatch(element -> element.mouseReleased(mouseX, mouseY, button))
          || super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      return this.elements.stream().anyMatch(element -> element.mouseDragged(mouseX, mouseY, button, deltaX, deltaY))
          || super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
      this.elements.forEach(element -> element.mouseMoved(mouseX, mouseY));
      super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      return this.elements.stream().anyMatch(element -> element.keyPressed(keyCode, scanCode, modifiers))
          || super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
      return this.elements.stream().anyMatch(element -> element.keyReleased(keyCode, scanCode, modifiers))
          || super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
      return this.elements.stream().anyMatch(element -> element.charTyped(chr, modifiers))
          || super.charTyped(chr, modifiers);
    }
  }
}
