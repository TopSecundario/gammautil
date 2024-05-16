package top.secundario.gamma.gui;

import java.awt.*;

public class Colors {

    public static String toHTML(Color color) {
        return String.format("#%08X", color.getRGB());
    }

    public static Color adaptForBgColor(Color bgColor) {
        float[] hsbvals = Color.RGBtoHSB(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), null);
        if (hsbvals[2] < 0.5f)      /* brightness */
            return Color.WHITE;
        else
            return Color.BLACK;
    }

    protected Colors() {}
}
