package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataAccess {
	private Connection connection;

	private String url = "jdbc:mysql://localhost:3306/archery_db";
	private PreparedStatement archerListingpstmt = null;

	public Connection getConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				connection = DriverManager.getConnection(url, "root", "021190");
			}
		} catch (SQLException e) {
			System.err.println("No database connection - wrong connect string?");
			e.printStackTrace();
			System.exit(-1);
		}
		return connection;
	}

	public PreparedStatement getArcherListingStmt(String archerListingSql) throws ClassNotFoundException, SQLException {
		if (archerListingpstmt == null) {
			archerListingpstmt = getConnection().prepareStatement(archerListingSql);
		}
		return archerListingpstmt;
	}

}
