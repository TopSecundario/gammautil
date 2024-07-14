package top.secundario.gamma.gui;

@FunctionalInterface
public interface BiSliderValueChangingHandler {
    public void valueChanging(int newLowValue, int newHighValue);
}
