package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {

	private Connection con;
	private String database;
	private Map<String, String[]> tables = new HashMap<String, String[]>();

	public Database(String host, String database, String username, String password) {
		this.database = database;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
		String url = "jdbc:mysql://" + host + ":3306/" + database;
		try {
			con = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public PreparedStatement prepare(String sql) {
		try {
			return con.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void execute(PreparedStatement stmt) {
		try {
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String[] getTableColumns(String table) {
		if (!tables.containsKey(table)) {
			try {
				ResultSet rs = con.createStatement().executeQuery("select * from " + table + ";");
				ResultSetMetaData meta = rs.getMetaData();
				String[] columns = new String[meta.getColumnCount()];
				for (int i = 0; i < columns.length; i++) {
					columns[i] = meta.getColumnName(i + 1);
				}
				tables.put(table, columns);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return tables.get(table);
	}

	public List<String[]> execute(String sql) {
		List<String[]> list = new ArrayList<String[]>();
		try {
			if (sql.startsWith("insert") || sql.startsWith("update") || sql.startsWith("delete")) {
				int result = prepare(sql).executeUpdate(sql);
				if (result <= 0) System.out.println("Your query could not be executed!");
				return null;
			}
			ResultSet result = prepare(sql).executeQuery();
			ResultSetMetaData meta = result.getMetaData();
			int columns = meta.getColumnCount();
			while (result.next()) {
				String[] row = new String[columns];
				for (int i = 1; i < columns + 1; i++) {
					row[i - 1] = result.getString(i);
				}
				list.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public Connection get() {
		return con;
	}

	public void close() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return database;
	}
	
	public static boolean quotesRequired(String value) {
		if (value.equals("NULL")) return false;
		if (value.matches("-?\\d+(\\.\\d+)?")) return false;
		return true;
	}

}
