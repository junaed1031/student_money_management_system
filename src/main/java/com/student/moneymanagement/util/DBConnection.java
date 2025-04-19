package com.student.moneymanagement.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/student_money_management"; // Replace 'testdb' with your database name
    private static final String USER = "root"; // Default username for XAMPP
    private static final String PASSWORD = ""; // Default password for XAMPP (empty)

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}