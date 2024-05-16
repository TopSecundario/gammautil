package top.secundario.gamma.gui;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Wrapper for Swing class {@link GridBagConstraints}
 */
public class GBC extends GridBagConstraints {
    public static int defaultGap = 5;


    public GBC(int row, int col) {
        this(row, col, defaultGap);
    }

    public GBC(int row, int col, int gap) {
        super();
        init(row, col, gap);
    }

    public GBC(String loc) throws IllegalArgumentException {
        this(loc, defaultGap);
    }

    public GBC(String loc, int gap) throws IllegalArgumentException {
        super();
        Cell cell = transformExcelLoc(loc);
        init(cell.row, cell.col, gap);
    }

    public GBC span(int rowspan, int colspan) {
        this.gridwidth = colspan;
        this.gridheight = rowspan;
        return this;
    }

    public GBC rowSpan(int rowspan) {
        return span(rowspan, 1);
    }

    public GBC colSpan(int colspan) {
        return span(1, colspan);
    }

    public GBC weight(double weightx, double weighty) {
        this.weightx = weightx;
        this.weighty = weighty;
        return this;
    }

    public GBC weightX(double weightx) {
        return weight(weightx, 0);
    }

    public GBC weightY(double weighty) {
        return weight(0, weighty);
    }

    public GBC Fill(int fill) {
        this.fill = fill;
        return this;
    }

    public GBC hFill() {
        return Fill(GridBagConstraints.HORIZONTAL);
    }

    public GBC vFill() {
        return Fill(GridBagConstraints.VERTICAL);
    }

    public GBC hvFill() {
        return Fill(GridBagConstraints.BOTH);
    }

    public GBC INSETS(int top, int left, int bottom, int right) {
        this.insets = new Insets(top, left, bottom, right);
        return this;
    }

    public GBC Anchor(int anchor) {
        this.anchor = anchor;
        return this;
    }

    public GBC wAnchor() {
        return Anchor(GridBagConstraints.WEST);
    }

    public GBC eAnchor() {
        return Anchor(GridBagConstraints.EAST);
    }


    protected record Cell(int row, int col) {}

    protected static final Pattern EXCEL_LOC_PATTERN = Pattern.compile("^([A-Z]{1})(\\d+)$");    // a simple pattern

    protected static Cell transformExcelLoc(String excelLoc) throws IllegalArgumentException {
        Matcher m = EXCEL_LOC_PATTERN.matcher(excelLoc);
        if (m.find()) {
            int col = m.group(1).charAt(0) - 'A';
            int row = Integer.parseInt(m.group(2)) - 1;
            return new Cell(row, col);
        } else {
            throw new IllegalArgumentException("Invalid excel cell loc: " + excelLoc);
        }
    }

    private void init(int row, int col, int gap) {
        this.gridx = col;
        this.gridy = row;

        int tInset = (0 != row) ? 0 : gap;
        int lInset = (0 != col) ? 0 : gap;
        this.insets = new Insets(tInset, lInset, gap, gap);
    }
}
