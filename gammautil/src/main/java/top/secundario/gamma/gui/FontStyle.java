package top.secundario.gamma.gui;

import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

public enum FontStyle {
    PLAIN("PLAIN", Font.PLAIN),
    BOLD("BOLD", Font.BOLD),
    ITALIC("ITALIC", Font.ITALIC),
    BOLD_ITALIC("BOLD|ITALIC", Font.BOLD | Font.ITALIC);


    private final String _desc;
    private final int _style;

    FontStyle(String desc, int style) {
        this._desc = desc;
        this._style = style;
    }

    public int style() {
        return this._style;
    }

    public String toString() {
        return this._desc;
    }

    public static FontStyle from(int style) throws IllegalArgumentException {
        FontStyle e = styleMap.get(style);
        if (null != e) {
            return e;
        } else {
            throw new IllegalArgumentException("No such font style: " + style);
        }
    }

    private static final Map<Integer, FontStyle> styleMap = new TreeMap<>();
    static {
        for (FontStyle e : FontStyle.values()) {
            styleMap.put(e.style(), e);
        }
    }
}
