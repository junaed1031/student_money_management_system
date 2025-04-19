package com.student.moneymanagement.dao;

import com.student.moneymanagement.model.Expense;
import com.student.moneymanagement.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    public boolean addExpense(Expense expense) {
        String query = "INSERT INTO Expenses (user_id, amount, category, date, description) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, expense.getUserId());
            preparedStatement.setDouble(2, expense.getAmount());
            preparedStatement.setString(3, expense.getCategory());
            preparedStatement.setDate(4, new java.sql.Date(expense.getDate().getTime()));
            preparedStatement.setString(5, expense.getDescription());
            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Expense> getExpensesByUserId(int userId) {
        List<Expense> expenseList = new ArrayList<>();
        String query = "SELECT * FROM Expenses WHERE user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Expense expense = new Expense();
                expense.setExpenseId(resultSet.getInt("expense_id"));
                expense.setUserId(resultSet.getInt("user_id"));
                expense.setAmount(resultSet.getDouble("amount"));
                expense.setCategory(resultSet.getString("category"));
                expense.setDate(resultSet.getDate("date"));
                expense.setDescription(resultSet.getString("description"));
                expenseList.add(expense);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenseList;
    }

    public boolean updateExpense(Expense expense) {
        String query = "UPDATE Expenses SET amount = ?, category = ?, date = ?, description = ? WHERE expense_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, expense.getAmount());
            preparedStatement.setString(2, expense.getCategory());
            preparedStatement.setDate(3, new java.sql.Date(expense.getDate().getTime()));
            preparedStatement.setString(4, expense.getDescription());
            preparedStatement.setInt(5, expense.getExpenseId());
            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteExpense(int expenseId) {
        String query = "DELETE FROM Expenses WHERE expense_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, expenseId);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}