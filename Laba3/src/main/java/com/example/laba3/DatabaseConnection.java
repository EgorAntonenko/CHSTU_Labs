package com.example.laba3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Змініть назву бази даних на ту, яку ви використовуєте
	private static final String URL = "jdbc:mysql://localhost:3306/trading_base?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
    private static final String USER = "yegor"; // Змініть, якщо ім'я користувача інше
    private static final String PASSWORD = "yegor0605"; // Додайте пароль, якщо він заданий у XAMPP
    
    public static Connection initializeDatabase() throws SQLException, ClassNotFoundException {
        // Завантажуємо драйвер MySQL
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        // Підключаємося до бази даних із заданими параметрами
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
