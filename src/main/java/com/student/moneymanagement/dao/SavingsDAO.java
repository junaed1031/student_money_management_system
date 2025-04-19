package com.student.moneymanagement.dao;

import com.student.moneymanagement.model.Savings;
import com.student.moneymanagement.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SavingsDAO {

    public boolean addSavings(Savings savings) {
        String query = "INSERT INTO Savings (user_id, goal_amount, current_amount, description) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, savings.getUserId());
            preparedStatement.setDouble(2, savings.getGoalAmount());
            preparedStatement.setDouble(3, savings.getCurrentAmount());
            preparedStatement.setString(4, savings.getDescription());
            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Savings> getSavingsByUserId(int userId) {
        List<Savings> savingsList = new ArrayList<>();
        String query = "SELECT * FROM Savings WHERE user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Savings savings = new Savings();
                savings.setSavingsId(resultSet.getInt("savings_id"));
                savings.setUserId(resultSet.getInt("user_id"));
                savings.setGoalAmount(resultSet.getDouble("goal_amount"));
                savings.setCurrentAmount(resultSet.getDouble("current_amount"));
                savings.setDescription(resultSet.getString("description"));
                savingsList.add(savings);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return savingsList;
    }
}