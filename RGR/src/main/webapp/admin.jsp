<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>SQL Query</title>
</head>
<body>
<h1>Database Viewer для адміна</h1>
    <form action="/Lab3_3/CrudServlet" method="post">
        <label>SQL Query:</label>
        <input type="text" name="query" placeholder="Наприклад, SELECT * FROM student;">
        <button type="submit">Виконати</button>
    </form>
</body>
</html>