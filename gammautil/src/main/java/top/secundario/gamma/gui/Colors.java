package top.secundario.gamma.gui;

import top.secundario.gamma.common.ObjectS;

import java.awt.*;

public class Colors {

    public static String toHTML(Color color) {
        return  (ObjectS.isNotNull(color)) ? (String.format("#%08X", color.getRGB())) : "null";
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
