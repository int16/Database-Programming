<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="db.Database, java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="css/style.css" />
	<link href='http://fonts.googleapis.com/css?family=Lato' rel='stylesheet' type='text/css' />
	<title>Spike 6</title>
</head>
<body>
<% 	final int PAGE_SIZE = 10;
	int p = 1;
	try {
		p = Integer.parseInt(request.getParameter("page"));
	} catch (NumberFormatException e) {
	}
	if (p < 1) p = 1;
	Database database = new Database("localhost", "archery_db", "root", "021190");
	String sql = "select archer.surname, archer.given_name, round.name, roundscore.day_shot, roundscore.total, roundscore.rating from archer " + //
					"join round on archer.id = round.id " + //
					"join roundscore on archer.id = roundscore.archer_id";
	List<String[]> archers = database.execute(sql); %>
	<table>
	<tr>
<% 	int pp = p - 1;
	if (pp < 1) pp = 1; %>
		<td class="button" colspan="6"><a href="?page=<%=pp%>">Previous</a></td>
	</tr>
	<th>Surname</th>
	<th>Given Name</th>
	<th>Round Name</th>
	<th>Day Shot <%= p %></th>
	<th>Total</th>
	<th>Rating</th>
<%	for (int i = p * PAGE_SIZE - 1; i < p * PAGE_SIZE + PAGE_SIZE - 1; i++) {
		if (i > archers.size() - 1) break;
		String[] archer = archers.get(i); %>
		<tr>
<%		for (int j = 0; j < archer.length; j++) { %>
			<td><%= archer[j] %></td>
<%		} %>
		</tr>
<%	} %>
	<tr>
<% 	int np = p + 1;
	if (np > archers.size() / PAGE_SIZE) np = archers.size() / PAGE_SIZE; %>
		<td class="button" colspan="6"><a href="?page=<%=np%>">Next</a></td>
	</tr>
	</table>
	#Dahai
</body>
</html>