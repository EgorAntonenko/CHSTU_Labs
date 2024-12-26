package com.example.firstboot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/cosmosdb?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
    private static final String USER = "Yegor"; 
    private static final String PASSWORD = "yegor0605"; 
    
    public static Connection initializeDatabase() throws SQLException, ClassNotFoundException {
        // Завантажуємо драйвер MySQL
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        // Підключаємося до бази даних із заданими параметрами
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        return initializeDatabase();
    }
}
