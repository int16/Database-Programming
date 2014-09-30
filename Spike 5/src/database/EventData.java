package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Locale;

import scores.Event;
import scores.MultiEvent;

public class EventData {
	private DataAccess access = null;

	public EventData() {
		access = new DataAccess();
	}

	public void storeNewMultiEvent(MultiEvent me) throws SQLException, ClassNotFoundException {
		Connection conn = access.getConnection();
		conn.setAutoCommit(false);
		Statement stmt = conn.createStatement();
		try {
			stmt.executeUpdate("INSERT INTO multieventcompetition" + " (comp_name, period_start, period_end) VALUES ('" + me.getName() + "', '" + (new java.sql.Date(me.getStart().getTime())) + "', '" + (new java.sql.Date(me.getEnd().getTime())) + "');");
		} catch (SQLException ex) {
			conn.rollback();
			conn.close();
			stmt.close();
			return;
		}
		// This gets the foreign key for the next inserts
		ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID() from multieventcompetition");
		rs.next();
		int multiEventId = rs.getInt(1);

		try {
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO multieventrule " + "(multieventcomp_id, round_id, calc_rules,  events_included) " + "VALUES (?, ?, ?, ?);");
			int noRules = me.getRules();
			for (int i = 0; i < noRules; i++) {
				pstmt.setInt(1, multiEventId);
				pstmt.setInt(2, me.getRule(i).getRoundId());
				pstmt.setString(3, me.getRule(i).getCalcRules());
				pstmt.setInt(4, me.getRule(i).getNoEvents());
				pstmt.executeUpdate();
			}
			conn.commit();
		} catch (SQLException ex) {
			conn.rollback();
		}
		conn.close();
		stmt.close();
	}

	/*	public void storeNewEvent(Event e) throws SQLException, ClassNotFoundException {
			Connection conn = access.getConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();

			int multiEventId = 0;
			if (e.getMultiEvent() != null) multiEventId = e.getMultiEvent().getId();

			String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(e.getEventTime());
			
			stmt.executeUpdate("INSERT INTO event" + " (eventname, venue, round_id, multieventcomp_id, time_shot) " + "VALUES ('" + e.getName() + "', '" + e.getVenue() + "', " + e.getRound().getId() + ", " + multiEventId + ", '" + dateString + "');");

			conn.commit();
			stmt.close();
			conn.close();
		}
	*/

	public void storeNewEvent(Event e) throws SQLException, ClassNotFoundException {
		Connection conn = access.getConnection();
		conn.setAutoCommit(false);
		Statement stmt = conn.createStatement();

		int multiEventId = 0;
		if (e.getMultiEvent() != null) multiEventId = e.getMultiEvent().getId();

		String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(e.getEventTime());

		try {
			stmt.executeUpdate("INSERT INTO event" + " (eventname, venue, round_id, multieventcomp_id, time_shot) " + "VALUES ('" + e.getName() + "', '" + e.getVenue() + "', " + e.getRound().getId() + ", " + multiEventId + ", '" + dateString + "');");
			conn.commit();
		} catch (SQLException ex) {
			conn.rollback();
		}
		stmt.close();
		conn.close();
	}
}
