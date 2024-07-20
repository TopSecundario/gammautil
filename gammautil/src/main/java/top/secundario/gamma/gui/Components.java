package top.secundario.gamma.gui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Components {

    public static void moveToScreenCenter(Window win) {
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        int wx = (scrSize.width - win.getWidth()) / 2;
        int wy = (scrSize.height - win.getHeight()) / 2;
        win.setLocation(wx, wy);
    }

    public static ImageIcon loadImageIcon(Class<?> clz, String path) {
        URL url = clz.getResource(path);
        if (null != url) {
            return new ImageIcon(url);
        } else {
            System.err.println("Failed to load icon: " + path);
            return null;
        }
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

    public static void printGeometry(JComponent c) {
        System.out.println("size: " + c.getSize());
        System.out.println("min size: " + c.getMinimumSize());
        System.out.println("pref size: " + c.getPreferredSize());
        System.out.println("max size: " + c.getMaximumSize());
        System.out.println("location: " + c.getLocation());
        System.out.println("bounds: " + c.getBounds());
        System.out.println("insets: " + c.getInsets());
        System.out.println("border insets: " + (null != c.getBorder() ? c.getBorder().getBorderInsets(c) : null));
    }

    protected Components() {}
}
