<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.Connection, java.sql.Statement, java.sql.ResultSet, java.sql.ResultSetMetaData" %>
<%@ page import="com.example.firstboot.DatabaseConnection" %>
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>Database Viewer</title>
</head>
<body>
    <h1>Database Viewer</h1>
    <form method="post">
        <label for="table">Оберіть таблицю:</label>
        <select name="table" id="table">
            <option value="moons">Moons</option>
            <option value="planets">Planets</option>
            <option value="spacecrafts">Spacecrafts</option>
			<option value="stars">Stars</option>
        </select>
        <button type="submit">Показати</button>
    </form>

    <%
        String table = request.getParameter("table");
        if (table != null) {
            table = table.trim();
            if (!table.matches("^[a-zA-Z0-9_]+$")) {
                out.println("<p style='color:red;'>Неприпустима назва таблиці.</p>");
            } else {
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    Statement stmt = conn.createStatement();
                    String query = "SELECT * FROM " + table;
                    ResultSet rs = stmt.executeQuery(query);

                    out.println("<table border='1'>");
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();

                    // Print table header
                    out.println("<tr>");
                    for (int i = 1; i <= columnCount; i++) {
                        out.println("<th>" + rsmd.getColumnName(i) + "</th>");
                    }
                    out.println("</tr>");

                    // Print table rows
                    while (rs.next()) {
                        out.println("<tr>");
                        for (int i = 1; i <= columnCount; i++) {
                            out.println("<td>" + rs.getString(i) + "</td>");
                        }
                        out.println("</tr>");
                    }
                    out.println("</table>");

                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (Exception e) {
                    out.println("<p style='color:red;'>Помилка при виконанні запиту: " + e.getMessage() + "</p>");
                }
            }
        }
    %>
</body>
</html>
