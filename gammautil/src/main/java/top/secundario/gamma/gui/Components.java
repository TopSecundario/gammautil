package top.secundario.gamma.gui;

import java.awt.*;

public class Components {

    protected Components() {}

    public static void moveToScreenCenter(Window win) {
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        int wx = (scrSize.width - win.getWidth()) / 2;
        int wy = (scrSize.height - win.getHeight()) / 2;
        win.setLocation(wx, wy);
    }
}
