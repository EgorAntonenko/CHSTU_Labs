
<%@ page import="java.sql.*" %>
<%@ page import="com.example.firstboot.DatabaseConnection" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="javax.servlet.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <h2>Login</h2>
    <form method="post" action="index.jsp">
        <label for="login">Login:</label>
        <input type="text" id="login" name="login" required><br><br>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required><br><br>
        <button type="submit">Submit</button>
    </form>

    <%
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if (login != null && password != null) {
            try {
                // Assuming DatabaseConnection provides a method to get the connection
                Connection conn = DatabaseConnection.getConnection();
                String query = "SELECT role FROM USERS WHERE login = ? AND password = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, login);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                	String userRole = rs.getString("role");
                	if ("user".equals(userRole)) {
                        response.sendRedirect("user.jsp");
                	} else if ("admin".equals(userRole)) {
                        response.sendRedirect("admin.jsp");
                    }
                } else {
                    out.println("<p style='color:red;'>Invalid login or password.</p>");
                }

                rs.close();
                stmt.close();
                conn.close();
            } catch (Exception e) {
                
    java.io.StringWriter sw = new java.io.StringWriter();
    java.io.PrintWriter pw = new java.io.PrintWriter(sw);
    e.printStackTrace(pw);
    out.println("<pre>" + sw.toString() + "</pre>");
    
                out.println("<p style='color:red;'>An error occurred. Please try again later.</p>");
            }
        }
    %>
</body>
</html>
