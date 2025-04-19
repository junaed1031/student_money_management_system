package com.student.moneymanagement.ui;

import com.student.moneymanagement.dao.IncomeDAO;
import com.student.moneymanagement.model.Income;
import com.student.moneymanagement.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class IncomeManagementFrame extends JFrame {
    private User user;
    private IncomeDAO incomeDAO;
    private JTable incomeTable;
    private DefaultTableModel tableModel;

    public IncomeManagementFrame(User user) {
        this.user = user;
        this.incomeDAO = new IncomeDAO();

        setTitle("Income Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        // Income Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Amount", "Category", "Date", "Description"}, 0);
        incomeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(incomeTable);
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

        // Load Income Data
        loadIncomeData();

        // Add Button Action
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new IncomeFrame(user).setVisible(true);
                dispose();
            }
        });

        // Update Button Action
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = incomeTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int incomeId = (int) tableModel.getValueAt(selectedRow, 0);
                    double amount = (double) tableModel.getValueAt(selectedRow, 1);
                    String category = (String) tableModel.getValueAt(selectedRow, 2);
                    String description = (String) tableModel.getValueAt(selectedRow, 4);

                    Income income = new Income();
                    income.setIncomeId(incomeId);
                    income.setUserId(user.getUserId());
                    income.setAmount(amount);
                    income.setCategory(category);
                    income.setDescription(description);

                    if (incomeDAO.updateIncome(income)) {
                        JOptionPane.showMessageDialog(null, "Income updated successfully!");
                        loadIncomeData();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to update income!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an income to update!");
                }
            }
        });

        // Delete Button Action
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = incomeTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int incomeId = (int) tableModel.getValueAt(selectedRow, 0);

                    if (incomeDAO.deleteIncome(incomeId)) {
                        JOptionPane.showMessageDialog(null, "Income deleted successfully!");
                        loadIncomeData();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to delete income!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an income to delete!");
                }
            }
        });
    }

    private void loadIncomeData() {
        tableModel.setRowCount(0);
        List<Income> incomes = incomeDAO.getIncomeByUserId(user.getUserId());
        for (Income income : incomes) {
            tableModel.addRow(new Object[]{income.getIncomeId(), income.getAmount(), income.getCategory(), income.getDate(), income.getDescription()});
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
                new IncomeManagementFrame(testUser).setVisible(true);
            }
        });
    }
}