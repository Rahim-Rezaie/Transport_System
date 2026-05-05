package com.transport.ui;

import com.transport.dao.SearchDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class SearchPanel extends JPanel {

    // ── Colour palette ──────────────────────────────────────────────────────
    private static final Color BG_DARK       = new Color(18, 22, 35);
    private static final Color BG_CARD       = new Color(28, 33, 50);
    private static final Color BG_ROW_ALT    = new Color(33, 39, 60);
    private static final Color ACCENT_BLUE   = new Color(79, 139, 255);
    private static final Color ACCENT_GREEN  = new Color(52, 211, 153);
    private static final Color ACCENT_ORANGE = new Color(251, 146, 60);
    private static final Color TEXT_PRIMARY  = new Color(230, 235, 255);
    private static final Color TEXT_MUTED    = new Color(130, 140, 170);
    private static final Color BORDER_COLOR  = new Color(50, 58, 85);

    // ── Search DAO ──────────────────────────────────────────────────────────
    private final SearchDAO searchDAO = new SearchDAO();

    // ── UI components ───────────────────────────────────────────────────────
    private JTextField searchField;
    private JButton    searchButton;
    private JTabbedPane resultTabs;

    // Driver tab
    private JPanel    driverSummaryPanel;
    private JLabel    lblDriverName, lblDriverStatus, lblCurrentTrip,
                      lblTruckPlate, lblTotalCommission;
    private JTable    driverTable;
    private DefaultTableModel driverModel;

    // Truck tab
    private JTable    truckTable;
    private DefaultTableModel truckModel;

    // Trip tab
    private JTable    tripTable;
    private DefaultTableModel tripModel;

    // Status bar
    private JLabel statusBar;

    // ── Constructor ─────────────────────────────────────────────────────────
    public SearchPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BG_DARK);

        add(buildTopBar(),    BorderLayout.NORTH);
        add(buildContent(),   BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  TOP BAR  (title + search field + button)
    // ════════════════════════════════════════════════════════════════════════
    private JPanel buildTopBar() {
        JPanel topBar = new JPanel(new BorderLayout(20, 0));
        topBar.setBackground(BG_CARD);
        topBar.setBorder(new EmptyBorder(16, 24, 16, 24));

        // Title
        JLabel title = new JLabel("🔍  Global Search");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_PRIMARY);

        // Hint
        JLabel hint = new JLabel("Search by driver name · truck plate · trip ID");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hint.setForeground(TEXT_MUTED);

        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.add(title);
        titleBlock.add(Box.createVerticalStrut(2));
        titleBlock.add(hint);

        // Search bar
        searchField = new JTextField(30);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        searchField.setBackground(new Color(40, 47, 70));
        searchField.setForeground(TEXT_PRIMARY);
        searchField.setCaretColor(ACCENT_BLUE);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(8, 14, 8, 14)));
        searchField.putClientProperty("JTextField.placeholderText", "e.g. Ali, ABC-1234, or trip 5…");

        // Search button
        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchButton.setBackground(ACCENT_BLUE);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setBorderPainted(false);
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBorder(new EmptyBorder(9, 24, 9, 24));

        // Hover effect
        searchButton.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                searchButton.setBackground(new Color(100, 155, 255));
            }
            @Override public void mouseExited(MouseEvent e) {
                searchButton.setBackground(ACCENT_BLUE);
            }
        });

        // Actions
        searchButton.addActionListener(e -> doSearch());
        searchField.addActionListener(e -> doSearch()); // fire on Enter

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchBar.setOpaque(false);
        searchBar.add(searchField);
        searchBar.add(searchButton);

        topBar.add(titleBlock, BorderLayout.WEST);
        topBar.add(searchBar,  BorderLayout.EAST);

        // Bottom separator
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_DARK);
        wrapper.add(topBar, BorderLayout.CENTER);
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_COLOR);
        wrapper.add(sep, BorderLayout.SOUTH);
        return wrapper;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  MAIN CONTENT  (tabbed results)
    // ════════════════════════════════════════════════════════════════════════
    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BG_DARK);
        content.setBorder(new EmptyBorder(16, 20, 8, 20));

        resultTabs = new JTabbedPane(JTabbedPane.TOP);
        resultTabs.setBackground(BG_DARK);
        resultTabs.setForeground(TEXT_PRIMARY);
        resultTabs.setFont(new Font("Segoe UI", Font.BOLD, 13));

        resultTabs.addTab("👤  Drivers", buildDriverTab());
        resultTabs.addTab("🚛  Trucks",  buildTruckTab());
        resultTabs.addTab("🗺  Trips",   buildTripTab());

        content.add(resultTabs, BorderLayout.CENTER);
        return content;
    }

    // ── Driver Tab ──────────────────────────────────────────────────────────
    private JPanel buildDriverTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(12, 4, 4, 4));

        // Summary card row
        driverSummaryPanel = new JPanel(new GridLayout(1, 5, 12, 0));
        driverSummaryPanel.setBackground(BG_DARK);
        driverSummaryPanel.setVisible(false);

        lblDriverName       = makeSummaryLabel("Driver",       "—", ACCENT_BLUE);
        lblDriverStatus     = makeSummaryLabel("Status",       "—", ACCENT_GREEN);
        lblCurrentTrip      = makeSummaryLabel("Current Trip", "—", ACCENT_ORANGE);
        lblTruckPlate       = makeSummaryLabel("Truck Plate",  "—", new Color(196, 148, 255));
        lblTotalCommission  = makeSummaryLabel("Total Earned", "—", ACCENT_GREEN);

        driverSummaryPanel.add(wrapSummaryCard(lblDriverName,      ACCENT_BLUE));
        driverSummaryPanel.add(wrapSummaryCard(lblDriverStatus,    ACCENT_GREEN));
        driverSummaryPanel.add(wrapSummaryCard(lblCurrentTrip,     ACCENT_ORANGE));
        driverSummaryPanel.add(wrapSummaryCard(lblTruckPlate,      new Color(196, 148, 255)));
        driverSummaryPanel.add(wrapSummaryCard(lblTotalCommission, ACCENT_GREEN));

        // Table
        String[] cols = {"ID", "Name", "Phone", "License No", "Status",
                         "Trip ID", "Trip Status", "Start Date", "End Date",
                         "Truck No.", "Total Commission"};
        driverModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        driverTable = buildStyledTable(driverModel);

        panel.add(driverSummaryPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(driverTable), BorderLayout.CENTER);
        styleScrollPane(panel);
        return panel;
    }

    // ── Truck Tab ───────────────────────────────────────────────────────────
    private JPanel buildTruckTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(12, 4, 4, 4));

        String[] cols = {"ID", "Truck No.", "Status",
                         "Current Driver", "Trip ID", "Trip Status",
                         "Start Date", "End Date"};
        truckModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        truckTable = buildStyledTable(truckModel);

        panel.add(new JScrollPane(truckTable), BorderLayout.CENTER);
        styleScrollPane(panel);
        return panel;
    }

    // ── Trip Tab ────────────────────────────────────────────────────────────
    private JPanel buildTripTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(12, 4, 4, 4));

        String[] cols = {"Trip ID", "Status", "Start Date", "End Date",
                         "Commission", "Driver", "Truck Plate",
                         "Invoice Amount", "Invoice Status"};
        tripModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tripTable = buildStyledTable(tripModel);

        panel.add(new JScrollPane(tripTable), BorderLayout.CENTER);
        styleScrollPane(panel);
        return panel;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  STATUS BAR
    // ════════════════════════════════════════════════════════════════════════
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(BG_CARD);
        bar.setBorder(new EmptyBorder(6, 24, 6, 24));

        statusBar = new JLabel("Enter a name, plate, or trip ID and press Search.");
        statusBar.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusBar.setForeground(TEXT_MUTED);
        bar.add(statusBar, BorderLayout.WEST);
        return bar;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  SEARCH LOGIC
    // ════════════════════════════════════════════════════════════════════════
    private void doSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            statusBar.setText("⚠  Please enter a search term.");
            return;
        }

        // Disable button while searching
        searchButton.setEnabled(false);
        searchButton.setText("Searching…");
        statusBar.setText("Searching for '" + keyword + "'...");

        // Run in background thread so UI stays responsive
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            List<Object[]> drivers, trucks, trips;

            @Override
            protected Void doInBackground() {
                drivers = searchDAO.searchDrivers(keyword);
                trucks  = searchDAO.searchTrucks(keyword);
                trips   = searchDAO.searchTrips(keyword);
                return null;
            }

            @Override
            protected void done() {
                populateDrivers(drivers);
                populateTrucks(trucks);
                populateTrips(trips);

                int total = drivers.size() + trucks.size() + trips.size();
                if (total == 0) {
                    statusBar.setText("No results found for '" + keyword + "'.");
                } else {
                    statusBar.setText("Found  " + drivers.size() + " driver row(s)  ·  "
                            + trucks.size() + " truck(s)  ·  "
                            + trips.size()  + " trip(s)  for '" + keyword + "'");
                }

                searchButton.setEnabled(true);
                searchButton.setText("Search");

                // Auto-switch to the tab with results
                if (!drivers.isEmpty()) resultTabs.setSelectedIndex(0);
                else if (!trucks.isEmpty()) resultTabs.setSelectedIndex(1);
                else if (!trips.isEmpty())  resultTabs.setSelectedIndex(2);
            }
        };
        worker.execute();
    }

    // ── Populate tables ──────────────────────────────────────────────────────
    private void populateDrivers(List<Object[]> rows) {
        driverModel.setRowCount(0);
        for (Object[] row : rows) {
            driverModel.addRow(row);
        }

        if (!rows.isEmpty()) {
            // Fill summary card with first driver's data
            Object[] first = rows.get(0);
            lblDriverName.setText("<html><b style='font-size:13'>" + first[1] + "</b></html>");
            lblDriverStatus.setText("<html>" + first[4] + "</html>");
            lblCurrentTrip.setText("<html>Trip #" + first[5] + " — " + first[6] + "</html>");
            lblTruckPlate.setText("<html>" + first[9] + "</html>");
            lblTotalCommission.setText("<html><b>" + first[10] + "</b></html>");
            driverSummaryPanel.setVisible(true);
        } else {
            driverSummaryPanel.setVisible(false);
        }
    }

    private void populateTrucks(List<Object[]> rows) {
        truckModel.setRowCount(0);
        for (Object[] row : rows) {
            truckModel.addRow(row);
        }
    }

    private void populateTrips(List<Object[]> rows) {
        tripModel.setRowCount(0);
        for (Object[] row : rows) {
            tripModel.addRow(row);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  HELPERS — styled table, summary cards
    // ════════════════════════════════════════════════════════════════════════
    private JTable buildStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(
                    javax.swing.table.TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (isRowSelected(row)) {
                    c.setBackground(new Color(79, 139, 255, 80));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(row % 2 == 0 ? BG_CARD : BG_ROW_ALT);
                    c.setForeground(TEXT_PRIMARY);
                }
                return c;
            }
        };

        table.setBackground(BG_CARD);
        table.setForeground(TEXT_PRIMARY);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(new Color(79, 139, 255, 80));
        table.setSelectionForeground(Color.WHITE);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(40, 47, 70));
        header.setForeground(ACCENT_BLUE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_BLUE));

        // Centre all columns
        DefaultTableCellRenderer centre = new DefaultTableCellRenderer();
        centre.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < model.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centre);
        }
        return table;
    }

    /** A label that shows title + value stacked vertically. */
    private JLabel makeSummaryLabel(String title, String value, Color accentColor) {
        JLabel lbl = new JLabel(
            "<html><center><span style='color:#7888aa;font-size:10'>" + title +
            "</span><br><b style='font-size:13'>" + value + "</b></center></html>",
            SwingConstants.CENTER);
        lbl.setForeground(TEXT_PRIMARY);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return lbl;
    }

    /** Wrap a summary label inside a styled card panel. */
    private JPanel wrapSummaryCard(JLabel label, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, accent),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        new EmptyBorder(10, 12, 10, 12))));
        card.add(label, BorderLayout.CENTER);
        return card;
    }

    /** Apply dark scroll-pane styling to all JScrollPanes in the panel. */
    private void styleScrollPane(JPanel panel) {
        for (Component c : panel.getComponents()) {
            if (c instanceof JScrollPane sp) {
                sp.getViewport().setBackground(BG_CARD);
                sp.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
                sp.getVerticalScrollBar().setBackground(BG_DARK);
                sp.getHorizontalScrollBar().setBackground(BG_DARK);
            }
        }
    }
}
