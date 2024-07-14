package top.secundario.gamma.gui;

@FunctionalInterface
public interface BiSliderValueChangedHandler {
    public void valueChanged(int newLowValue, int newHighValue);
}
