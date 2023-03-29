package net.darmo_creations.n_gameplay_base.gui;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;
import java.util.function.Function;

public class DoubleSliderWidget extends SliderWidget {
  private final double min;
  private final double max;
  private final Consumer<Double> valueConsumer;
  private final Function<Double, Text> messageFactory;

  public DoubleSliderWidget(int x, int y, int width, int height, final double min, final double max, double value,
                            final Consumer<Double> valueConsumer, final Function<Double, Text> messageFactory) {
    super(x, y, width, height, messageFactory.apply(value), (value - min) / (max - min));
    this.min = min;
    this.max = max;
    this.valueConsumer = valueConsumer;
    this.messageFactory = messageFactory;
  }

  @Override
  protected void updateMessage() {
    this.setMessage(this.messageFactory.apply(this.getValue()));
  }

  @Override
  protected void applyValue() {
    this.valueConsumer.accept(this.getValue());
  }

  public double getValue() {
    return MathHelper.lerp(this.value, this.min, this.max);
  }
}
