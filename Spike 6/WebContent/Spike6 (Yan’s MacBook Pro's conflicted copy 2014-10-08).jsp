<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="db.Database" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Spike 6</title>
</head>
<body>
	<% Database database = new Database("localhost", "archery_db", "root", "021190");
	String[] archers = database.getTableColumns("archer");
	for (String archer : archers) { %>
	<h1><%= archer %></h1><br/>
	<%} %>

</body>
</html>