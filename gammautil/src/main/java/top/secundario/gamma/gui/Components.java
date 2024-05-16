package top.secundario.gamma.gui;

import javax.swing.*;
import java.awt.*;

public class Components {

    public static void moveToScreenCenter(Window win) {
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        int wx = (scrSize.width - win.getWidth()) / 2;
        int wy = (scrSize.height - win.getHeight()) / 2;
        win.setLocation(wx, wy);
    }

    public static Color adaptForBgColor(JComponent c) {
        Color bgColor = c.getBackground();
        if (null != bgColor) {
            Color adaptedFgColor = Colors.adaptForBgColor(bgColor);
            c.setForeground(adaptedFgColor);
            return adaptedFgColor;
        } else {
            return c.getForeground();
        }
    }

    protected Components() {}
}
