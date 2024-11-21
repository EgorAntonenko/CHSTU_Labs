package com.example.laba3;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/CrudServlet")
public class CrudServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Налаштування типу вмісту для HTML-виводу
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Отримання SQL-запиту від користувача
        String query = request.getParameter("query");

        try (Connection conn = DatabaseConnection.initializeDatabase();
             Statement stmt = conn.createStatement()) {
             
            if (query.toLowerCase().startsWith("select")) {
                // Виконання SELECT запиту
                ResultSet rs = stmt.executeQuery(query);
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                
                // Побудова таблиці для відображення результатів
                out.println("<table border='1'>");
                
                // Заголовки колонок
                out.println("<tr>");
                for (int i = 1; i <= columnCount; i++) {
                    out.println("<th>" + rsmd.getColumnName(i) + "</th>");
                }
                out.println("</tr>");
                
                // Рядки з даними
                while (rs.next()) {
                    out.println("<tr>");
                    for (int i = 1; i <= columnCount; i++) {
                        out.println("<td>" + rs.getString(i) + "</td>");
                    }
                    out.println("</tr>");
                }
                out.println("</table>");
                
            } else {
                // Виконання операцій INSERT, UPDATE, DELETE
                int result = stmt.executeUpdate(query);
                out.println("Запит виконано успішно, змінено " + result + " рядків.");
            }
        } catch (Exception e) {
            // Обробка помилок та їх виведення на сторінку
            out.println("Виникла помилка: " + e.getMessage());
            e.printStackTrace(out);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Перенаправлення GET-запитів на метод POST для спрощення логіки
        doPost(request, response);
    }
}
