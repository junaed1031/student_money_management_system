package com.student.moneymanagement.util;

import com.student.moneymanagement.dao.ExpenseDAO;
import com.student.moneymanagement.dao.SavingsDAO;
import com.student.moneymanagement.model.Expense;
import com.student.moneymanagement.model.Savings;
import com.student.moneymanagement.model.User;

import javax.swing.*;
import java.util.List;

public class NotificationManager {
    private User user;
    private ExpenseDAO expenseDAO;
    private SavingsDAO savingsDAO;

    public NotificationManager(User user) {
        this.user = user;
        this.expenseDAO = new ExpenseDAO();
        this.savingsDAO = new SavingsDAO();
    }

    public void checkNotifications() {
        List<Expense> expenses = expenseDAO.getExpensesByUserId(user.getUserId());
        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double budgetLimit = 1000.00; // Example budget limit
        if (totalExpenses > budgetLimit) {
            JOptionPane.showMessageDialog(null, "Alert: You have exceeded your budget limit of $" + budgetLimit + "!");
        }

        List<Savings> savingsList = savingsDAO.getSavingsByUserId(user.getUserId());
        for (Savings savings : savingsList) {
            if (savings.getCurrentAmount() >= savings.getGoalAmount()) {
                JOptionPane.showMessageDialog(null, "Congratulations! You have reached your savings goal of $" + savings.getGoalAmount() + "!");
            }
        }
    }
}