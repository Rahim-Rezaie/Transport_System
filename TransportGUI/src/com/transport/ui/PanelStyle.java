package com.transport.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Shared styling utility — each panel uses this with its own colour palette.
 */
public class PanelStyle {

    // ── Build a top header bar ─────────────────────────────────────────────
    public static JPanel buildHeader(String emoji, String title, String subtitle,
                                     Color panelBg, Color headerBg, Color accent) {
        JPanel header = new JPanel(new BorderLayout(0, 4));
        header.setBackground(headerBg);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, accent),
                new EmptyBorder(14, 22, 14, 22)));

        JLabel titleLbl = new JLabel(emoji + "  " + title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLbl.setForeground(new Color(230, 235, 255));

        JLabel subLbl = new JLabel(subtitle);
        subLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLbl.setForeground(accent.darker());

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(titleLbl);
        text.add(Box.createVerticalStrut(2));
        text.add(subLbl);

        header.add(text, BorderLayout.WEST);
        return header;
    }

    // ── Build a styled JTable ─────────────────────────────────────────────
    public static JTable buildStyledTable(DefaultTableModel model,
                                          Color rowEven, Color rowOdd,
                                          Color headerBg, Color accent,
                                          Color textColor) {
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (isRowSelected(row)) {
                    c.setBackground(new Color(accent.getRed(), accent.getGreen(),
                            accent.getBlue(), 60));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(row % 2 == 0 ? rowEven : rowOdd);
                    c.setForeground(textColor);
                }
                return c;
            }
        };

        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);
        table.setBackground(rowEven);
        table.setForeground(textColor);
        table.setSelectionBackground(new Color(accent.getRed(), accent.getGreen(),
                accent.getBlue(), 60));
        table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setBackground(headerBg);
        header.setForeground(accent);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, accent));

        DefaultTableCellRenderer centre = new DefaultTableCellRenderer();
        centre.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < model.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centre);
        }
        return table;
    }

    // ── Build a styled button ──────────────────────────────────────────────
    public static JButton buildButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(9, 22, 9, 22));

        Color hover = bg.brighter();
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(bg);    }
        });
        return btn;
    }

    // ── Build a styled button bar ──────────────────────────────────────────
    public static JPanel buildButtonBar(Color bg, JButton... buttons) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bar.setBackground(bg);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,
                bg.brighter().brighter()));
        for (JButton b : buttons) bar.add(b);
        return bar;
    }

    // ── Style a JScrollPane ────────────────────────────────────────────────
    public static void styleScrollPane(JScrollPane sp, Color viewportBg, Color border) {
        sp.getViewport().setBackground(viewportBg);
        sp.setBorder(BorderFactory.createLineBorder(border, 1));
        sp.getVerticalScrollBar().setBackground(viewportBg.darker());
        sp.getHorizontalScrollBar().setBackground(viewportBg.darker());
    }
}
