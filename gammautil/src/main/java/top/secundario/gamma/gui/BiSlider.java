package top.secundario.gamma.gui;

import top.secundario.gamma.common.Strings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Math.round;

public class BiSlider extends JComponent implements MouseMotionListener, MouseListener {
    private int minValue;
    private int maxValue;
    private String minTickLabel;
    private String maxTickLabel;
    private int lowSlidedValue;
    private int highSlidedValue;

    private final Insets iInsets;
    private int cursorWidth = 20;
    private int cursorHeight = 17;
    private int sliderThick = 20;
    private int tickLabelGap = 50;
    private final BiSliderGeometry geometry;
    private Color outSelectionColor;
    private Color inSelectionColor;
    private int cursorOutlineWidth = 1;
    private Color lowCursorFillColor;
    private Color lowCursorOutlineColor;
    private Color highCursorFillColor;
    private Color highCursorOutlineColor;

    private final java.util.List<BiSliderValueChangingHandler> vChangingHandlerList;
    private final java.util.List<BiSliderValueChangedHandler> vChangedHandlerList;


    public BiSlider() {
        this(0, 100);
    }

    public BiSlider(int minValue, int maxValue) {
        setFont(new Font("Dialog", Font.PLAIN, 12));
        setOpaque(true);
        setForeground(Color.BLACK);
        setBackground(Color.WHITE);

        addMouseMotionListener(this);
        addMouseListener(this);
        vChangingHandlerList = new ArrayList<>();
        vChangedHandlerList = new ArrayList<>();

        iInsets = new Insets(0, 10, 0, 10);
        geometry = new BiSliderGeometry();
        outSelectionColor = new Color(128, 128, 128);
        inSelectionColor = new Color(19, 133, 53);
        lowCursorFillColor = new Color(128, 121, 0);
        highCursorFillColor = new Color(0, 81, 116);
        lowCursorOutlineColor = new Color(119, 7, 11);
        highCursorOutlineColor = new Color(81, 18, 82);

        setMinValue(minValue);
        setMaxValue(maxValue);
        setMinTickLabel(String.valueOf(minValue));
        setMaxTickLabel(String.valueOf(maxValue));
        setLowSlidedValue(minValue);
        setHighSlidedValue(maxValue);
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        if (this.minValue != minValue) {
            this.minValue = minValue;
            revalidate();
            repaint();
        }
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        if (this.maxValue != maxValue) {
            this.maxValue = maxValue;
            revalidate();
            repaint();
        }
    }

    public String getMinTickLabel() {
        return minTickLabel;
    }

    public void setMinTickLabel(String minTickLabel) {
        minTickLabel = (! Strings.isNullOrEmpty(minTickLabel)) ? minTickLabel : String.valueOf(minValue);
        if (! Objects.equals(this.minTickLabel, minTickLabel)) {
            this.minTickLabel = minTickLabel;
            revalidate();
            repaint();
        }
    }

    public String getMaxTickLabel() {
        return maxTickLabel;
    }

    public void setMaxTickLabel(String maxTickLabel) {
        maxTickLabel = (! Strings.isNullOrEmpty(maxTickLabel)) ? maxTickLabel : String.valueOf(maxValue);
        if (! Objects.equals(this.maxTickLabel, maxTickLabel)) {
            this.maxTickLabel = maxTickLabel;
            revalidate();
            repaint();
        }
    }

    public int getLowSlidedValue() {
        return lowSlidedValue;
    }

    public void setLowSlidedValue(int lowSlidedValue) {
        if (this.lowSlidedValue != lowSlidedValue) {
            this.lowSlidedValue = lowSlidedValue;
            repaint();
        }
    }

    public int getHighSlidedValue() {
        return highSlidedValue;
    }

    public void setHighSlidedValue(int highSlidedValue) {
        if (this.highSlidedValue != highSlidedValue) {
            this.highSlidedValue = highSlidedValue;
            repaint();
        }
    }

    public boolean addValueChangingHandler(BiSliderValueChangingHandler handler) {
        return vChangingHandlerList.add(handler);
    }

    public boolean removeValueChangingHandler(BiSliderValueChangingHandler handler) {
        return vChangingHandlerList.remove(handler);
    }

    public boolean addValueChangedHandler(BiSliderValueChangedHandler handler) {
        return vChangedHandlerList.add(handler);
    }

    public boolean removeValueChangedHandler(BiSliderValueChangedHandler handler) {
        return vChangedHandlerList.remove(handler);
    }

    protected void notifyAllValueChangingHandlers(int newLowValue, int newHighValue) {
        vChangingHandlerList.forEach(_handler -> _handler.valueChanging(newLowValue, newHighValue));
    }

    protected void notifyAllValueChangedHandlers(int newLowValue, int newHighValue) {
        vChangedHandlerList.forEach(_handler -> _handler.valueChanged(newLowValue, newHighValue));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isOpaque()) {
            g2.setColor(getClearColor());
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        drawSlider(g2);
        drawTickLabel(g2);

        drawLowCursor(g2);
        drawHighCursor(g2);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        calcGeometryByContent();

        Insets xInsets = getInsets();               // external insets

        /* component height */
        int ch = xInsets.top + iInsets.top + cursorHeight + sliderThick + geometry.tickLabelHeight + iInsets.bottom + xInsets.bottom;
        /* component width */
        int cw = xInsets.left + iInsets.left + geometry.minTickLabelWidth + tickLabelGap + geometry.maxTickLabelWidth + iInsets.right + xInsets.right;

        return new Dimension(cw, ch);
    }

    private void calcGeometryByContent() {
        FontMetrics fm = getFontMetrics(getFont());
        geometry.tickLabelHeight = fm.getHeight();
        geometry.minTickLabelWidth = fm.stringWidth(minTickLabel);
        geometry.maxTickLabelWidth = fm.stringWidth(maxTickLabel);
        geometry.tickLabelAscent = fm.getAscent();
    }

    private void drawSlider(Graphics2D g2) {
        int cw = getWidth();                        // component width
        int ch = getHeight();                       // component height
        Insets xInsets = getInsets();               // external insets

        int sl = cw - xInsets.left - iInsets.left - xInsets.right - iInsets.right;          // slider length
        /* slider thick */
        int st = ch - xInsets.top - iInsets.top - cursorHeight - geometry.tickLabelHeight - xInsets.bottom - iInsets.bottom;
        int sx = xInsets.left + iInsets.left;                                               // slider x
        int sy = xInsets.top + iInsets.top + cursorHeight;                                  // slider y

        g2.setColor(outSelectionColor);
        g2.fillRect(sx, sy, sl, st);

        float scale = (float) sl / (maxValue - minValue);
        int lcx = sx + round(lowSlidedValue * scale);                                       // low cursor x
        int hcx = sx + round(highSlidedValue * scale);                                      // high cursor x

        g2.setColor(inSelectionColor);
        g2.fillRect(lcx, sy, hcx - lcx, st);

        geometry.sliderThick = st;
        geometry.lcx = lcx;
        geometry.hcx = hcx;
        geometry.pxPerTick = scale;
    }

    private void drawTickLabel(Graphics2D g2) {
        Insets xInsets = getInsets();               // external insets
        int cw = getWidth();                        // component width

        /* tick y */
        int ty = xInsets.top + iInsets.top + cursorHeight + geometry.sliderThick + geometry.tickLabelAscent;
        int min_t_x = xInsets.left;                                            // min tick x
        int max_t_x = cw - xInsets.right - geometry.maxTickLabelWidth;         // max tick x

        g2.setFont(getFont());
        g2.setColor(getForeground());
        g2.drawString(minTickLabel, min_t_x, ty);
        g2.drawString(maxTickLabel, max_t_x, ty);
    }

    private void drawLowCursor(Graphics2D g2) {
        Insets xInsets = getInsets();               // external insets

        float hte = cursorWidth / 2.0f;                        // half of top edge
        int cby = xInsets.top + iInsets.top + cursorHeight;    // cursor bottom y

        GeneralPath slc = new GeneralPath();                   // triangle shape of low cursor
        slc.moveTo(geometry.lcx, cby);
        slc.lineTo(geometry.lcx - hte, cby - cursorHeight);
        slc.lineTo(geometry.lcx + hte, cby - cursorHeight);
        slc.closePath();

        g2.setColor(lowCursorFillColor);
        g2.fill(slc);
        g2.drawLine(geometry.lcx, cby, geometry.lcx, cby + geometry.sliderThick - 1);
        if (geometry.lcHover) {
            g2.setColor(lowCursorOutlineColor);
            g2.draw(slc);
        }

        geometry.slc = slc;
    }

    private void drawHighCursor(Graphics2D g2) {
        Insets xInsets = getInsets();               // external insets

        float hte = cursorWidth / 2.0f;                        // half of top edge
        int cby = xInsets.top + iInsets.top + cursorHeight;    // cursor bottom y

        GeneralPath shc = new GeneralPath();                   // triangle shape of high cursor
        shc.moveTo(geometry.hcx, cby);
        shc.lineTo(geometry.hcx - hte, cby - cursorHeight);
        shc.lineTo(geometry.hcx + hte, cby - cursorHeight);
        shc.closePath();

        g2.setColor(highCursorFillColor);
        g2.fill(shc);
        g2.drawLine(geometry.hcx, cby, geometry.hcx, cby + geometry.sliderThick - 1);
        if (geometry.hcHover) {
            g2.setColor(highCursorOutlineColor);
            g2.draw(shc);
        }

        geometry.shc = shc;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int valueChanging = 0;

        if (geometry.lcHover) {
            geometry.lcDrag = true;
            int dx = e.getX() - geometry.lastMouseX;
            geometry.lastMouseX = e.getX();
            int dv = round(dx / geometry.pxPerTick);
            lowSlidedValue += dv;
            if (lowSlidedValue < minValue) {
                lowSlidedValue = minValue;
            }
            if (lowSlidedValue > highSlidedValue) {
                lowSlidedValue = highSlidedValue;
            }
            if (lowSlidedValue > maxValue) {
                lowSlidedValue = maxValue;
            }
            repaint();
            valueChanging |= 1;
        }

        if (geometry.hcHover) {
            geometry.hcDrag = true;
            int dx = e.getX() - geometry.lastMouseX;
            geometry.lastMouseX = e.getX();
            int dv = round(dx / geometry.pxPerTick);
            highSlidedValue += dv;
            if (highSlidedValue < minValue) {
                highSlidedValue = minValue;
            }
            if (highSlidedValue < lowSlidedValue) {
                highSlidedValue = lowSlidedValue;
            }
            if (highSlidedValue > maxValue) {
                highSlidedValue = maxValue;
            }
            repaint();
            valueChanging |= 2;
        }

        if (0 != valueChanging) {
            notifyAllValueChangingHandlers(lowSlidedValue, highSlidedValue);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        boolean shouldHover = geometry.slc.contains(e.getX(), e.getY());
        if (geometry.lcHover != shouldHover) {
            geometry.lcHover = shouldHover;
            repaint();
        }

        shouldHover = geometry.shc.contains(e.getX(), e.getY());
        if (geometry.hcHover != shouldHover) {
            geometry.hcHover = shouldHover;
            repaint();
        }
    }

    protected Color getClearColor() {
        Container parent = getParent();
        if (null != parent) {
            return parent.getBackground();
        }
        return super.getBackground();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        geometry.lastMouseX = e.getX();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int valueChanged = 0;

        if (geometry.lcHover) {
            geometry.lcDrag = false;
            repaint();
            valueChanged |= 1;
        }

        if (geometry.hcHover) {
            geometry.hcDrag = false;
            repaint();
            valueChanged |= 2;
        }

        if (0 != valueChanged) {
            notifyAllValueChangedHandlers(lowSlidedValue, highSlidedValue);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    private static class BiSliderGeometry {
        int tickLabelHeight;
        int minTickLabelWidth;
        int maxTickLabelWidth;
        int tickLabelAscent;
        int sliderThick;
        int lcx;                           // low cursor x
        int hcx;                           // high cursor x
        Shape slc;                         // shape of low cursor
        Shape shc;                         // shape of high cursor
        boolean lcHover;                   // low cursor hover
        boolean hcHover;                   // high cursor hover
        boolean lcDrag;                    // low cursor drag
        boolean hcDrag;                    // high cursor drag
        float pxPerTick;
        int lastMouseX;
    }
}
