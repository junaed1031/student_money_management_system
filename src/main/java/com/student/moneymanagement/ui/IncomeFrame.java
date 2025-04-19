package com.student.moneymanagement.ui;

import com.student.moneymanagement.dao.IncomeDAO;
import com.student.moneymanagement.model.Income;
import com.student.moneymanagement.model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class IncomeFrame extends JFrame {
    private User user;
    private JTextField amountField;
    private JTextField categoryField;
    private JTextField descriptionField;
    private JButton addButton;
    private IncomeDAO incomeDAO;

    public IncomeFrame(User user) {
        this.user = user;
        incomeDAO = new IncomeDAO();

        setTitle("Add Income");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(10, 20, 80, 25);
        panel.add(amountLabel);

        amountField = new JTextField(20);
        amountField.setBounds(100, 20, 165, 25);
        panel.add(amountField);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setBounds(10, 50, 80, 25);
        panel.add(categoryLabel);

        categoryField = new JTextField(20);
        categoryField.setBounds(100, 50, 165, 25);
        panel.add(categoryField);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setBounds(10, 80, 80, 25);
        panel.add(descriptionLabel);

        descriptionField = new JTextField(20);
        descriptionField.setBounds(100, 80, 165, 25);
        panel.add(descriptionField);

        addButton = new JButton("Add");
        addButton.setBounds(10, 110, 80, 25);
        panel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double amount = Double.parseDouble(amountField.getText());
                String category = categoryField.getText();
                String description = descriptionField.getText();

                Income income = new Income();
                income.setUserId(user.getUserId());
                income.setAmount(amount);
                income.setCategory(category);
                income.setDate(new Date());
                income.setDescription(description);

                if (incomeDAO.addIncome(income)) {
                    JOptionPane.showMessageDialog(null, "Income added successfully!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add income!");
                }
            }
        });
    }

    public static void main(String[] args) {
        // Dummy user for testing
        User testUser = new User();
        testUser.setUserId(1);
        testUser.setUsername("TestUser");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new IncomeFrame(testUser).setVisible(true);
            }
        });
    }
}