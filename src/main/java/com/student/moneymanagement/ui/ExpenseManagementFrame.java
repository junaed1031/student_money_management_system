package com.student.moneymanagement.ui;

import com.student.moneymanagement.dao.ExpenseDAO;
import com.student.moneymanagement.model.Expense;
import com.student.moneymanagement.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ExpenseManagementFrame extends JFrame {
    private User user;
    private ExpenseDAO expenseDAO;
    private JTable expenseTable;
    private DefaultTableModel tableModel;

    public ExpenseManagementFrame(User user) {
        this.user = user;
        this.expenseDAO = new ExpenseDAO();

        setTitle("Expense Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        // Expense Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Amount", "Category", "Date", "Description"}, 0);
        expenseTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(expenseTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Load Expense Data
        loadExpenseData();

        // Add Button Action
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ExpenseFrame(user).setVisible(true);
                dispose();
            }
        });

        // Update Button Action
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = expenseTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int expenseId = (int) tableModel.getValueAt(selectedRow, 0);
                    double amount = (double) tableModel.getValueAt(selectedRow, 1);
                    String category = (String) tableModel.getValueAt(selectedRow, 2);
                    String description = (String) tableModel.getValueAt(selectedRow, 4);

                    Expense expense = new Expense();
                    expense.setExpenseId(expenseId);
                    expense.setUserId(user.getUserId());
                    expense.setAmount(amount);
                    expense.setCategory(category);
                    expense.setDescription(description);

                    if (expenseDAO.updateExpense(expense)) {
                        JOptionPane.showMessageDialog(null, "Expense updated successfully!");
                        loadExpenseData();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to update expense!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an expense to update!");
                }
            }
        });

        // Delete Button Action
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = expenseTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int expenseId = (int) tableModel.getValueAt(selectedRow, 0);

                    if (expenseDAO.deleteExpense(expenseId)) {
                        JOptionPane.showMessageDialog(null, "Expense deleted successfully!");
                        loadExpenseData();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to delete expense!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an expense to delete!");
                }
            }
        });
    }

    private void loadExpenseData() {
        tableModel.setRowCount(0);
        List<Expense> expenses = expenseDAO.getExpensesByUserId(user.getUserId());
        for (Expense expense : expenses) {
            tableModel.addRow(new Object[]{expense.getExpenseId(), expense.getAmount(), expense.getCategory(), expense.getDate(), expense.getDescription()});
        }
    }

    public static void main(String[] args) {
        // Dummy user for testing
        User testUser = new User();
        testUser.setUserId(1);
        testUser.setUsername("TestUser");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ExpenseManagementFrame(testUser).setVisible(true);
            }
        });
    }
}