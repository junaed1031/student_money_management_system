package com.student.moneymanagement.ui;

import com.student.moneymanagement.dao.SavingsDAO;
import com.student.moneymanagement.model.Savings;
import com.student.moneymanagement.model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SavingsFrame extends JFrame {
    private User user;
    private JTextField goalAmountField;
    private JTextField currentAmountField;
    private JTextField descriptionField;
    private JButton addButton;
    private SavingsDAO savingsDAO;

    public SavingsFrame(User user) {
        this.user = user;
        savingsDAO = new SavingsDAO();

        setTitle("Add Savings");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        JLabel goalAmountLabel = new JLabel("Goal Amount:");
        goalAmountLabel.setBounds(10, 20, 100, 25);
        panel.add(goalAmountLabel);

        goalAmountField = new JTextField(20);
        goalAmountField.setBounds(120, 20, 150, 25);
        panel.add(goalAmountField);

        JLabel currentAmountLabel = new JLabel("Current Amount:");
        currentAmountLabel.setBounds(10, 50, 100, 25);
        panel.add(currentAmountLabel);

        currentAmountField = new JTextField(20);
        currentAmountField.setBounds(120, 50, 150, 25);
        panel.add(currentAmountField);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setBounds(10, 80, 100, 25);
        panel.add(descriptionLabel);

        descriptionField = new JTextField(20);
        descriptionField.setBounds(120, 80, 150, 25);
        panel.add(descriptionField);

        addButton = new JButton("Add");
        addButton.setBounds(10, 110, 80, 25);
        panel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double goalAmount = Double.parseDouble(goalAmountField.getText());
                double currentAmount = Double.parseDouble(currentAmountField.getText());
                String description = descriptionField.getText();

                Savings savings = new Savings();
                savings.setUserId(user.getUserId());
                savings.setGoalAmount(goalAmount);
                savings.setCurrentAmount(currentAmount);
                savings.setDescription(description);

                if (savingsDAO.addSavings(savings)) {
                    JOptionPane.showMessageDialog(null, "Savings added successfully!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add savings!");
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
                new SavingsFrame(testUser).setVisible(true);
            }
        });
    }
}