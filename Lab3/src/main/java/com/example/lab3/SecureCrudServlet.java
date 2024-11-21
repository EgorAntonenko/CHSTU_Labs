package com.example.lab3;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/SecureCrudServlet")
public class SecureCrudServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String command = request.getParameter("command");
        String table = request.getParameter("table");
        String data = request.getParameter("data"); 
        
        // Побудова SQL-запиту
        String sql = "";
        if ("insert".equalsIgnoreCase(command)) {
            sql = "INSERT INTO " + table + " SET " + data;
        } else if ("delete".equalsIgnoreCase(command)) {
            sql = "DELETE FROM " + table + " WHERE " + data;
        }
        
        try (Connection conn = DatabaseConnection.initializeDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            int result = pstmt.executeUpdate();
            response.getWriter().println("Запит виконано, змінено " + result + " рядків.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
