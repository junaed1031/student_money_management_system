package com.student.moneymanagement.dao;

import com.student.moneymanagement.model.Income;
import com.student.moneymanagement.util.DBConnection;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IncomeDAO {

    public boolean addIncome(@NotNull Income income) {
        String query = "INSERT INTO Income (user_id, amount, category, date, description) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, income.getUserId());
            preparedStatement.setDouble(2, income.getAmount());
            preparedStatement.setString(3, income.getCategory());
            preparedStatement.setDate(4, new java.sql.Date(income.getDate().getTime()));
            preparedStatement.setString(5, income.getDescription());
            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Income> getIncomeByUserId(int userId) {
        List<Income> incomeList = new ArrayList<>();
        String query = "SELECT * FROM Income WHERE user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Income income = new Income();
                income.setIncomeId(resultSet.getInt("income_id"));
                income.setUserId(resultSet.getInt("user_id"));
                income.setAmount(resultSet.getDouble("amount"));
                income.setCategory(resultSet.getString("category"));
                income.setDate(resultSet.getDate("date"));
                income.setDescription(resultSet.getString("description"));
                incomeList.add(income);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return incomeList;
    }

    public boolean updateIncome(Income income) {
        String query = "UPDATE Income SET amount = ?, category = ?, date = ?, description = ? WHERE income_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, income.getAmount());
            preparedStatement.setString(2, income.getCategory());
            preparedStatement.setDate(3, new java.sql.Date(income.getDate().getTime()));
            preparedStatement.setString(4, income.getDescription());
            preparedStatement.setInt(5, income.getIncomeId());
            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteIncome(int incomeId) {
        String query = "DELETE FROM Income WHERE income_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, incomeId);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}