package com.example.lab3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandLineApp {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
             Connection conn = DatabaseConnection.initializeDatabase()) {

            System.out.println("Введіть SQL команду або 'exit' для виходу:");

            while (true) {
                System.out.print("> "); 
                String command = scanner.nextLine();

                if ("exit".equalsIgnoreCase(command)) {
                    System.out.println("Програма завершена.");
                    break;
                }

                try {
                    if (command.toLowerCase().startsWith("insert")) {
                        handleInsertCommand(command, conn);
                    } else if (command.toLowerCase().startsWith("delete")) {
                        handleDeleteCommand(command, conn);
                    } else if (command.toLowerCase().startsWith("update")) {
                        handleUpdateCommand(command, conn);
                    } else if (command.toLowerCase().startsWith("read")) {
                        handleReadCommand(command, conn);
                    } else {
                        System.out.println("Команда не підтримується.");
                    }
                } catch (Exception e) {
                    System.out.println("Помилка при виконанні команди: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleInsertCommand(String command, Connection conn) {
        Pattern pattern = Pattern.compile("insert\\s+(\\w+)\\s*\\((.*)\\)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(command);

        if (matcher.matches()) {
            String tableName = matcher.group(1);
            String fieldsAndValues = matcher.group(2);

            String[] fieldValuePairs = fieldsAndValues.split(",\\s*");
            StringBuilder fields = new StringBuilder();
            StringBuilder placeholders = new StringBuilder();

            for (String pair : fieldValuePairs) {
                String[] fieldValue = pair.split("=");
                fields.append(fieldValue[0].trim()).append(",");
                placeholders.append("?,");
            }

            String sql = "INSERT INTO " + tableName + " (" + fields.substring(0, fields.length() - 1) +
                         ") VALUES (" + placeholders.substring(0, placeholders.length() - 1) + ")";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (int i = 0; i < fieldValuePairs.length; i++) {
                    String value = fieldValuePairs[i].split("=")[1].trim();

                    if (value.startsWith("'") && value.endsWith("'")) {
                        value = value.substring(1, value.length() - 1); 
                        pstmt.setString(i + 1, value);
                    } else if (value.contains(".")) { 
                        pstmt.setDouble(i + 1, Double.parseDouble(value)); // Якщо значення - десяткове число
                    } else {
                        pstmt.setInt(i + 1, Integer.parseInt(value)); // Якщо значення - ціле число
                    }
                }
                pstmt.executeUpdate();
                System.out.println("Дані успішно вставлено у таблицю " + tableName);
            } catch (SQLException e) {
                System.out.println("Помилка виконання INSERT команди: " + e.getMessage());
            }
        } else {
            System.out.println("Команда вставки має невірний формат.");
        }
    }

    private static void handleDeleteCommand(String command, Connection conn) {
        Pattern pattern = Pattern.compile("delete\\s+(\\w+)\\s*\\((.*)\\)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(command);

        if (matcher.matches()) {
            String tableName = matcher.group(1);
            String condition = matcher.group(2);

            String[] conditionParts = condition.split("=");
            if (conditionParts.length != 2) {
                System.out.println("Команда видалення має невірний формат.");
                return;
            }

            String field = conditionParts[0].trim();
            String value = conditionParts[1].trim().replace("'", "");

            String sql = "DELETE FROM " + tableName + " WHERE " + field + " = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, value);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Запис успішно видалено з таблиці " + tableName);
                } else {
                    System.out.println("Запис не знайдено у таблиці " + tableName);
                }
            } catch (SQLException e) {
                System.out.println("Помилка виконання DELETE команди: " + e.getMessage());
            }
        } else {
            System.out.println("Команда видалення має невірний формат.");
        }
    }

    private static void handleReadCommand(String command, Connection conn) {
        Pattern pattern = Pattern.compile("read\\s+(\\w+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(command);

        if (matcher.matches()) {
            String tableName = matcher.group(1);
            String sql = "SELECT * FROM " + tableName;

            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                int columns = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    for (int i = 1; i <= columns; i++) {
                        System.out.print(rs.getString(i) + "\t");
                    }
                    System.out.println();
                }
            } catch (SQLException e) {
                System.out.println("Помилка виконання READ команди: " + e.getMessage());
            }
        } else {
            System.out.println("Команда read має невірний формат.");
        }
    }

    private static void handleUpdateCommand(String command, Connection conn) {
        Pattern pattern = Pattern.compile("update\\s+(\\w+)\\s+set\\s+(.*)\\s+where\\s+(.*)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(command);

        if (matcher.matches()) {
            String tableName = matcher.group(1);
            String setClause = matcher.group(2);
            String whereClause = matcher.group(3);

            String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE " + whereClause;

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.executeUpdate();
                System.out.println("Дані успішно оновлено у таблиці " + tableName);
            } catch (SQLException e) {
                System.out.println("Помилка виконання UPDATE команди: " + e.getMessage());
            }
        } else {
            System.out.println("Команда update має невірний формат.");
        }
    }
}
