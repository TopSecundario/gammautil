package top.secundario.gamma.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Objects;

import static top.secundario.gamma.common.ObjectS.isNotNull;
import static top.secundario.gamma.common.Strings.isNullOrEmpty;

public class RoundLabel extends JComponent {
    protected static int ipad = 2;

    private String text;

    private int arc = 10;
    private final Rectangle contentBounds;

    public RoundLabel() {
        this(null);
    }

    public RoundLabel(String text) {
        arc = 10;
        contentBounds = new Rectangle(0, 0, 0, 0);

        setBackground(Color.LIGHT_GRAY);
        setFont(new Font("Dialog", Font.PLAIN, 12));

        setText(text);
    }

    public Dimension getMaximumSize() {
        return  new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public Dimension getPreferredSize() {
        return calcGeometryByContent();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (! Objects.equals(this.text, text)) {
            this.text = text;
            revalidate();
            repaint();
        }
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        //printGeometry();

        if ((0 != contentBounds.width) && (0 != contentBounds.height)) {    // valid content
            Graphics2D _g2 = (Graphics2D) g2.create();
            _g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
            _g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Insets insets = getInsets();
            if (isOpaque()) {
                _g2.setColor(getClearColor());
                _g2.fillRect(0, 0, getWidth(), getHeight());
                int wFill = getWidth() - insets.left - insets.right;
                int hFill = getHeight() - insets.top - insets.bottom;
                _g2.setColor(getBackground());
                _g2.fillRoundRect(insets.left, insets.top, wFill, hFill, arc, arc);
            }

            double tx = ((double) getWidth() - insets.left - insets.right - contentBounds.width) / 2;
            double ty = ((double) getHeight() - insets.top - insets.bottom - contentBounds.height) / 2;
            tx = tx + insets.left;
            ty = ty + insets.top;
            //System.out.printf("tx %f, ty %f%n", tx, ty);
            _g2.setColor(getForeground());
            _g2.setFont(getFont());
            _g2.transform(AffineTransform.getTranslateInstance(tx, ty));
            _g2.drawString(text, contentBounds.x, contentBounds.y);

            _g2.dispose();
        }
    }

    // return preferred size
    private Dimension calcGeometryByContent() {
        Insets insets = getInsets();
        if (! isNullOrEmpty(text)) {
            FontMetrics fm = getFontMetrics(getFont());
            contentBounds.width = fm.stringWidth(text);
            contentBounds.height = fm.getHeight();
            contentBounds.width = ipad + contentBounds.width + ipad;
            contentBounds.height = ipad + contentBounds.height + ipad;
            contentBounds.x = ipad;
            contentBounds.y = ipad + fm.getAscent();
            int wInherent = insets.left + contentBounds.width + insets.right;
            int hInherent = insets.top + contentBounds.height + insets.bottom;
            return new Dimension(wInherent, hInherent);
        } else {
            /* no content */
            contentBounds.setBounds(0, 0, 0, 0);
            return new Dimension(insets.left + insets.right, insets.top + insets.bottom);
        }
    }

    protected void printGeometry() {
        Components.printGeometry(this);
        System.out.println("contentBounds: " + contentBounds);
    }

    protected Color getClearColor() {
        Container parent = getParent();
        if (isNotNull(parent)) {
            //System.out.println("parent container bg color: " + parent.getBackground());
            return parent.getBackground();
        }
        //System.out.println("super class bg color: " + super.getBackground());
        return super.getBackground();
    }
}
