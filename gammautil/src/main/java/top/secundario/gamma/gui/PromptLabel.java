package top.secundario.gamma.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Objects;

import top.secundario.gamma.common.ObjectS;
import top.secundario.gamma.common.Strings;

public class PromptLabel extends JComponent {
    private String prompt;
    private String content;

    /* geometry */
    private int gap;          /* gap between prompt area and content */
    private int ipad;         /* inner padding of prompt/content area */
    private int xpad;         /* external padding of prompt/content area */
    private int xPrompt;
    private int yPrompt;
    private int xContent;
    private int yContent;
    private int hInherent;
    private int wInherent;
    private int arc;
    private Rectangle rectPrompt;
    private Rectangle rectContent;
    /* draw control */
    private boolean drawPrompt;
    private boolean drawContent;

    /* facade */
    private Font fontPrompt;
    private Font fontContent;
    private Color bgColorPrompt;
    private Color fgColorPrompt;
    private Color bgColorContent;
    private Color fgColorContent;
    private int xOffset;
    private int yOffset;


    public PromptLabel() {
        this(null, null);
    }

    public PromptLabel(String prompt, String content) {
        gap = 5;
        ipad = 2;
        xpad = 2;
        arc = 10;
        rectPrompt = new Rectangle();
        rectContent = new Rectangle();

        Font baseFont = new Font("Monospaced", Font.PLAIN, 16);
        setFont(baseFont);
        setForeground(Color.BLACK);
        setBackground(Color.GRAY);

        fontPrompt = baseFont;
        fontContent = baseFont.deriveFont(Font.BOLD, 20);
        bgColorPrompt = getBackground();
        fgColorPrompt = getForeground();
        bgColorContent = getBackground();
        fgColorContent = getForeground();

        setPrompt(prompt);
        setContent(content);

        setOpaque(false);
    }

    public void revalidate() {
        calcGeometry();
        super.revalidate();
    }

    public Dimension getPreferredSize() {
        calcGeometry();
        return  new Dimension(wInherent, hInherent);
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        if (! Objects.equals(this.prompt, prompt)) {
            this.prompt = prompt;
            revalidate();
            repaint();
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (! Objects.equals(this.content, content)) {
            this.content = content;
            revalidate();
            repaint();
        }
    }

    public Color getPromptFgColor() {
        return fgColorPrompt;
    }

    public void setPromptFgColor(Color fgColorPrompt) throws IllegalArgumentException {
        if (ObjectS.isNull(fgColorPrompt))
            throw new IllegalArgumentException("Prompt foreground color can't be null!");

        if (! this.fgColorPrompt.equals(fgColorPrompt)) {
            this.fgColorPrompt = fgColorPrompt;
            repaint();
        }
    }

    public Color getContentFgColor() {
        return fgColorContent;
    }

    public void setContentFgColor(Color fgColorContent) throws IllegalArgumentException {
        if (ObjectS.isNull(fgColorContent))
            throw new IllegalArgumentException("Prompt background color can't be null!");

        if (! this.fgColorContent.equals(fgColorContent)) {
            this.fgColorContent = fgColorContent;
            repaint();
        }
    }

    public Color getPromptBgColor() {
        return bgColorPrompt;
    }

    public void setPromptBgColor(Color bgColorPrompt) {
        if (! Objects.equals(this.bgColorPrompt, bgColorPrompt)) {
            this.bgColorPrompt = bgColorPrompt;
            repaint();
        }
    }

    public Color getContentBgColor() {
        return bgColorContent;
    }

    public void setContentBgColor(Color bgColorContent) {
        if (! Objects.equals(this.bgColorContent, bgColorContent)) {
            this.bgColorContent = bgColorContent;
            repaint();
        }
    }

    public Font getPromptFont() {
        return fontPrompt;
    }

    public void setPromptFont(Font fontPrompt) throws IllegalArgumentException {
        if (ObjectS.isNull(fontPrompt))
            throw new IllegalArgumentException("Prompt font can't be null!");

        if (! this.fontPrompt.equals(fontPrompt)) {
            this.fontPrompt = fontPrompt;
            revalidate();
            repaint();
        }
    }

    public Font getContentFont() {
        return fontContent;
    }

    public void setContentFont(Font fontContent) throws IllegalArgumentException {
        if (ObjectS.isNull(fontContent))
            throw new IllegalArgumentException("Content font can't be null!");

        if (! this.fontContent.equals(fontContent)) {
            this.fontContent = fontContent;
            revalidate();
            repaint();
        }
    }

    private void calcGeometry() {
        drawPrompt = false;
        FontMetrics fmPrompt = (fontPrompt != null) ? getFontMetrics(fontPrompt) : null;
        int hPrompt = 0;
        int wPrompt = 0;
        if ((! Strings.isNullOrEmpty(prompt)) && (null != fmPrompt)) {
            hPrompt = fmPrompt.getHeight();
            wPrompt = fmPrompt.stringWidth(prompt);
            drawPrompt = true;
        }

        drawContent = false;
        FontMetrics fmContent = (null != fontContent) ? getFontMetrics(fontContent) : null;
        int hContent = 0;
        int wContent = 0;
        if ((! Strings.isNullOrEmpty(content)) && (null != fmContent)) {
            hContent = fmContent.getHeight();
            wContent = fmContent.stringWidth(content);
            drawContent = true;
        }

        if (hPrompt >= hContent) {
            hInherent = xpad + ipad + hPrompt + ipad + xpad;
            yPrompt = xpad + ipad;
            yContent = yPrompt + (hPrompt - hContent) / 2;
        } else {
            hInherent = xpad + ipad + hContent + ipad + xpad;
            yContent = xpad + ipad;
            yPrompt = yContent + (hContent - hPrompt) / 2;
        }
        rectPrompt.y = yPrompt - ipad;
        rectPrompt.height = ipad + hPrompt + ipad;
        rectContent.y = yContent - ipad;
        rectContent.height = ipad + hContent + ipad;
        yPrompt = yPrompt + ((null != fmPrompt) ? fmPrompt.getAscent() : 0);
        yContent = yContent + ((null != fmContent) ? fmContent.getAscent() : 0);

        xPrompt = xpad + ipad;
        xContent = xPrompt + wPrompt + ipad + gap + ipad;
        /* wInherent = xpad + ipad + wPrompt + ipad + gap + ipad + wContent + ipad + xpad */
        wInherent = xContent + wContent + ipad + xpad;
        rectPrompt.x = xpad;
        rectPrompt.width = ipad + wPrompt + ipad;
        rectContent.x = xContent - ipad;
        rectContent.width = ipad + wContent + ipad;

        Border border = getBorder();
        if (ObjectS.isNotNull(border)) {
            Insets insets = border.getBorderInsets(this);
            wInherent = insets.left + wInherent + insets.right;
            hInherent = insets.top + hInherent + insets.bottom;
            xOffset = insets.left;
            yOffset = insets.top;
        } else {
            xOffset = 0;
            yOffset = 0;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (ObjectS.isNotNull(bgColorPrompt)) {
            g2.setColor(bgColorPrompt);
            g2.fillRoundRect(rectPrompt.x + xOffset, rectPrompt.y + yOffset, rectPrompt.width, rectPrompt.height, arc, arc);
        }
        if (ObjectS.isNotNull(bgColorContent)) {
            g2.setColor(bgColorContent);
            g2.fillRoundRect(rectContent.x + xOffset, rectContent.y + yOffset, rectContent.width, rectContent.height, arc, arc);
        }

        if (drawPrompt) {
            g2.setColor(fgColorPrompt);
            g2.setFont(fontPrompt);
            g2.drawString(prompt, xPrompt + xOffset, yPrompt + yOffset);
        }
        if (drawContent) {
            g2.setColor(fgColorContent);
            g2.setFont(fontContent);
            g2.drawString(content, xContent + xOffset, yContent + yOffset);
        }
    }
}
