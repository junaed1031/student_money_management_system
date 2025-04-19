package com.student.moneymanagement.ui;

import com.student.moneymanagement.dao.ExpenseDAO;
import com.student.moneymanagement.dao.IncomeDAO;
import com.student.moneymanagement.dao.SavingsDAO;
import com.student.moneymanagement.model.User;
import com.student.moneymanagement.util.NotificationManager;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private User user;
    private JLabel welcomeLabel;
    private JLabel incomeLabel;
    private JLabel expenseLabel;
    private JLabel savingsLabel;
    private JLabel remainderLabel; // Label for remainder money

    public DashboardFrame(User user) {
        this.user = user;

        setTitle("Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        add(panel);

        welcomeLabel = new JLabel("Welcome, " + user.getUsername() + "!", JLabel.CENTER);
        incomeLabel = new JLabel("Total Income: $0.00", JLabel.CENTER);
        expenseLabel = new JLabel("Total Expenses: $0.00", JLabel.CENTER);
        remainderLabel = new JLabel("Remaining Money: $0.00", JLabel.CENTER); // New label
        savingsLabel = new JLabel("Total Savings: $0.00", JLabel.CENTER);


        JPanel summaryPanel = new JPanel(new GridLayout(5, 1)); // Adjusted for 5 rows
        summaryPanel.add(welcomeLabel);
        summaryPanel.add(incomeLabel);
        summaryPanel.add(expenseLabel);
        summaryPanel.add(remainderLabel); // Add remainder label
        summaryPanel.add(savingsLabel);


        panel.add(summaryPanel, BorderLayout.NORTH);
        panel.add(createExpenseChartPanel(), BorderLayout.CENTER);

        setUpMenuBar();

        // Fetch and display initial data
        refreshDashboardData();

        // Check notifications when the dashboard loads
        NotificationManager notificationManager = new NotificationManager(user);
        notificationManager.checkNotifications();
    }

    /**
     * Refreshes the dashboard data by fetching the latest income, expenses, and savings
     * from the database and updating the respective labels.
     */
    private void refreshDashboardData() {
        // DAO instances
        IncomeDAO incomeDAO = new IncomeDAO();
        ExpenseDAO expenseDAO = new ExpenseDAO();
        SavingsDAO savingsDAO = new SavingsDAO();

        // Fetch data
        double totalIncome = incomeDAO.getIncomeByUserId(user.getUserId())
                .stream().mapToDouble(income -> income.getAmount()).sum();

        double totalExpenses = expenseDAO.getExpensesByUserId(user.getUserId())
                .stream().mapToDouble(expense -> expense.getAmount()).sum();

        double totalSavings = savingsDAO.getSavingsByUserId(user.getUserId())
                .stream().mapToDouble(savings -> savings.getCurrentAmount()).sum();

        // Calculate remainder
        double remainder = totalIncome - totalExpenses;

        // Update labels
        incomeLabel.setText("Total Income: $" + String.format("%.2f", totalIncome));
        expenseLabel.setText("Total Expenses: $" + String.format("%.2f", totalExpenses));
        remainderLabel.setText("Remaining Money: $" + String.format("%.2f", remainder)); // Update remainder label
        savingsLabel.setText("Total Savings: $" + String.format("%.2f", totalSavings));
    }

    private JPanel createExpenseChartPanel() {
        JPanel chartPanel = new JPanel();
        chartPanel.add(new JLabel("Expense Chart Placeholder")); // Replace with actual chart logic
        return chartPanel;
    }

    private void setUpMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Menu");
        JMenuItem incomeItem = new JMenuItem("Manage Income");
        JMenuItem expenseItem = new JMenuItem("Manage Expenses");
        JMenuItem savingsItem = new JMenuItem("Manage Savings");
        JMenuItem reportItem = new JMenuItem("Generate Report");

        incomeItem.addActionListener(e -> {
            // Open Income Management Frame and refresh dashboard after closing
            IncomeManagementFrame incomeFrame = new IncomeManagementFrame(user);
            incomeFrame.setVisible(true);
            incomeFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    refreshDashboardData(); // Refresh data after income changes
                }
            });
        });

        expenseItem.addActionListener(e -> {
            // Open Expense Management Frame and refresh dashboard after closing
            ExpenseManagementFrame expenseFrame = new ExpenseManagementFrame(user);
            expenseFrame.setVisible(true);
            expenseFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    refreshDashboardData(); // Refresh data after expense changes
                }
            });
        });

        savingsItem.addActionListener(e -> {
            // Open Savings Frame and refresh dashboard after closing
            SavingsFrame savingsFrame = new SavingsFrame(user);
            savingsFrame.setVisible(true);
            savingsFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    refreshDashboardData(); // Refresh data after savings changes
                }
            });
        });

        reportItem.addActionListener(e -> new ReportFrame(user).setVisible(true));

        menu.add(incomeItem);
        menu.add(expenseItem);
        menu.add(savingsItem);
        menu.add(reportItem);

        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        // Dummy user for testing
        User testUser = new User();
        testUser.setUserId(1);
        testUser.setUsername("TestUser");

        SwingUtilities.invokeLater(() -> new DashboardFrame(testUser).setVisible(true));
    }
}