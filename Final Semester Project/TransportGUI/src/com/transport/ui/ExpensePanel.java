package com.transport.ui;

import com.transport.dao.ExpenseDAO;
import com.transport.model.Expense;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ExpensePanel extends JPanel {
    private JTable table;
    private ExpenseDAO expenseDAO;

    public ExpensePanel() {
        expenseDAO = new ExpenseDAO();
        setLayout(new BorderLayout());

        String[] columns = { "ID", "Trip ID", "Type", "Amount" };
        List<Expense> expenses = expenseDAO.getAllExpenses();
        Object[][] data = new Object[expenses.size()][4];
        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.get(i);
            data[i][0] = e.getId();
            data[i][1] = e.getTripId();
            data[i][2] = e.getType();
            data[i][3] = e.getAmount();
        }
        table = new JTable(data, columns);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
