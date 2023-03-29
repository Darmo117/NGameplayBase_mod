package net.darmo_creations.n_gameplay_base.gui;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;
import java.util.function.Function;

public class IntSliderWidget extends SliderWidget {
  private final int min;
  private final int max;
  private final Consumer<Integer> applyConsumer;
  private final Function<Integer, Text> messageFactory;

  public IntSliderWidget(int x, int y, int width, int height, final int min, final int max, int value,
                         final Consumer<Integer> valueConsumer, final Function<Integer, Text> messageFactory) {
    super(x, y, width, height, messageFactory.apply(value), ((double) value - min) / (max - min));
    this.min = min;
    this.max = max;
    this.applyConsumer = valueConsumer;
    this.messageFactory = messageFactory;
  }

  @Override
  protected void updateMessage() {
    this.setMessage(this.messageFactory.apply(this.getValue()));
  }

  @Override
  protected void applyValue() {
    this.applyConsumer.accept(this.getValue());
  }

  public int getValue() {
    return (int) MathHelper.lerp(this.value, this.min, this.max);
  }
}
