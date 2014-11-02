package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import scores.Archer;
import scores.Award;
import scores.Range;
import scores.Rating;
import scores.Round;
import scores.RoundGroup;
import scores.Score;
import scores.Utils;
import exceptions.NotEnoughOfficialRoundsException;

public class ScoringData {

	private DataAccess access = null;
	private Connection transactionalConnection = null;

	public ScoringData() {
		access = new DataAccess();

	}

	public void startTransaction() throws ClassNotFoundException, SQLException {
		transactionalConnection = access.getConnection();
		transactionalConnection.setAutoCommit(false);
	}

	// public void rollbackTransaction() throws SQLException
	// {
	// transactionalConnection.rollback();
	// transactionalConnection = null;
	// }

	public RoundGroup getRoundGroupforRound(Round round) throws ClassNotFoundException, SQLException {
		RoundGroup group = null;
		Connection conn = access.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT roundgroup, round_id, level" + " FROM roundgroup r1" + "  WHERE valid_from < NOW() AND valid_until > NOW()" + " AND round_id=" + round.getId() + " AND level=(SELECT MIN(level) from roundgroup r2 where r1.roundgroup = r2.roundgroup );");
		if (rs.next()) {
			group = new RoundGroup();
			group.setGroup(rs.getInt("roundgroup"));
			group.setLevel(rs.getInt("level"));
			group.setRound(round);
		}
		return group;
	}

	public void storeNewScoreInTransaction(Score score) throws ClassNotFoundException, SQLException {
		transactionalConnection.setAutoCommit(false);
		Statement stmt = transactionalConnection.createStatement();
		String rangeString = "";
		String rangeValues = "";
		String eventString = "";
		String eventValue = "";
		for (int i = 0; i < score.getRound().getRangeCount(); i++) {
			rangeString += "range" + (i + 1) + "score, ";
			rangeValues += score.getRangeScore(i) + ", ";
		}
		if (score.getEvent() != null) {
			eventString += "event_id, ";
			eventValue += score.getEvent().getId() + ",";
		}
		String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(score.getRoundShotTime());

		String sql = "INSERT INTO roundscore ( archer_id, day_shot, venue, " + eventString + "eventlevel, round_id, equip_id, category_id, " + rangeString + " rating, dnf, comment, " + "total, totalX, total10) " + "VALUES (" + score.getArcher().getId() + ", '" + dateString + "', '" + score.getVenue() + "'," + eventValue + "'" + score.getEventLevel().charAt(0) + "', " + score.getRound().getId() + ", " + score.getArcher().getCurrentEquipment().getId() + ", " + score.getArcher().getCurrentCat().getId() + ", " + rangeValues + score.getRating() + ", " + score.getDNF() + ", '" + score.getComment() + "', " + score.getTotal() + ", " + score.getTotalXs() + ", " + score.getTotal10s() + ")";
		System.out.println(sql);
		stmt.executeUpdate(sql);
		// This gets the foreign key for the next inserts
		ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID() from multieventcompetition");
		rs.next();
		score.setId(rs.getInt(1));
		PreparedStatement pstmt = transactionalConnection.prepareStatement("INSERT INTO end (roundscore_id, range_no, " + "range_id, end_no, end_total, arrow1, arrow2, arrow3, arrow4, arrow5, arrow6) " + "VALUES (?,?,?,?,?,?,?,?,?,?,?)");
		Range r;
		int total;
		for (int i = 0; i < score.getRound().getRangeCount(); i++) {
			r = score.getRound().getRange(0);
			pstmt.setInt(1, score.getId()); // round id from db
			pstmt.setInt(2, i + 1); // range no
			pstmt.setInt(3, r.getId()); // range id
			for (int j = 0; j < r.getEnds(); j++) {
				total = 0;
				String[] end = score.getEnd(i, j);
				pstmt.setInt(4, j + 1); // end no
				for (int k = 0; k < end.length; k++) {
					pstmt.setString(6 + k, end[k]);
					try {
						total += Integer.parseInt(end[k]);
					} catch (NumberFormatException nfe) {
						// we can ignore all other cases ("" and "M" don't add to the score
						if (end[k].equalsIgnoreCase("x")) total += 10;
					}
				}
				pstmt.setInt(5, total); // end_total
				pstmt.executeUpdate();
			}
		}

	}

	public Score selectScore(int archerId, int eventId) throws ClassNotFoundException, SQLException {
		transactionalConnection.setAutoCommit(false);
		Statement stmt = transactionalConnection.createStatement();
		String sql = "SELECT round.ranges, end.range_no, end.arrow1, end.arrow2, end.arrow3, end.arrow4, end.arrow5, end.arrow6, round_range.ends, round_range.arrows_per_end FROM roundscore JOIN end ON roundscore.id=end.roundscore_id JOIN round ON round.id=roundscore.round_id JOIN round_range ON (round.range1_id=round_range.id OR round.range2_id=round_range.id OR round.range3_id=round_range.id OR round.range4_id=round_range.id) WHERE roundscore.archer_id=" + archerId + " and roundscore.event_id=" + eventId + " order by roundscore.id, end.range_no asc";
		System.out.println(sql);
		ResultSet rs = stmt.executeQuery(sql);
		String[][] rangeData = null;
		int ranges = 0;
		Score result = new Score();
		boolean init = false, rangeInit = false;
		Round round = new Round();
		int row = 0;
		List<List<String[]>> scores = new ArrayList<List<String[]>>();
		while (rs.next()) {
			row++;
			ranges = rs.getInt(1);
			if (!init) {
				round.setRangeCount(ranges);
				init = true;
			}
			int r = rs.getInt(2);
			rangeInit = false;
			List<String[]> score = new ArrayList<String[]>();
			while (rs.next()) {
				row++;
				boolean data = rs.getInt(2) == r;
				if (!data) break;
				if (!rangeInit) {
					System.out.println("initialised range");
					Range range = new Range();
					range.setEnds(rs.getInt(9));
					range.setArrowsPerEnd(rs.getInt(10));
					round.addRange(r - 1, range);
					rangeInit = true;
				}
				score.add(new String[] { rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8) });
			}
			System.out.println("Row: " + row);
			scores.add(score);
			// scores.add(new String[] { rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),
			// rs.getString(7), rs.getString(8) });
		}
		result.initialise(round, null, false);
		for (int i = 0; i < scores.size(); i++) {
			List<String[]> score = scores.get(i);
			rangeData = new String[score.size()][6]; // TODO: Get rid of hard code
			for (int j = 0; j < score.size(); j++) {
				rangeData[j] = score.get(j);
			}
			result.processRangeScore(rangeData);
		}

		return result;
	}

	public void processPerfectAllGoldInTransaction(Score score) throws SQLException {
		ArrayList<Award> awards = score.getAwards();
		ArrayList<Award> newAwards = new ArrayList<Award>();
		ResultSet rs;
		int rowCount = -1;

		PreparedStatement pstmt = transactionalConnection.prepareStatement("SELECT Count(*) FROM perfectallgold p " + "	JOIN round_range rr on p.range_id=rr.id " + "	WHERE p.awardtype_id=? AND p.archer_id=? AND p.discipline_id=? " + "	AND p.equipment_id=? AND rr.distance=? and p.face_id=?");

		// check the existing awards
		for (Award award : awards) {
			pstmt.setInt(1, award.getAwardType().getId());
			pstmt.setInt(2, score.getArcher().getId());
			pstmt.setInt(3, score.getRound().getDiscipline().getId());
			pstmt.setInt(4, score.getArcher().getCurrentEquipment().getId());
			pstmt.setInt(5, award.getRange().getDistance());
			pstmt.setInt(6, award.getRange().getFace().getId());
			rs = pstmt.executeQuery();
			rs.next();
			rowCount = rs.getInt(1);
			if (rowCount == 0) // is there a perfect ten / all gold for this already?
			newAwards.add(award);

		}
		// change score's awards to the actual awards
		score.setAwards(newAwards);
		pstmt = transactionalConnection.prepareStatement("INSERT INTO perfectallgold (awardtype_id, round_id, archer_id, " + "discipline_id, equipment_id, range_id, face_id, score_id, end_no, " + "earned_date, award_conferred, title, eventlevel) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
		for (Award award : newAwards) {
			pstmt.setInt(1, award.getAwardType().getId());// awardtype_id,
			pstmt.setInt(2, score.getRound().getId());// round_id,
			pstmt.setInt(3, score.getArcher().getId());// archer_id
			pstmt.setInt(4, score.getRound().getDiscipline().getId());// discipline_id,
			pstmt.setInt(5, score.getArcher().getCurrentEquipment().getId());// equipment_id,
			pstmt.setInt(6, award.getRange().getId());// range_id,
			pstmt.setInt(7, award.getRange().getFace().getId());// face_id,
			pstmt.setInt(8, score.getId());// score_id,
			pstmt.setInt(9, award.getEndNo());// end_no,
			String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(score.getRoundShotTime());
			pstmt.setString(10, dateString);// earned_date,
			pstmt.setBoolean(11, false);// award_conferred,
			String title;
			if (award.getAwardType().getId() == 1) title = "All Gold - ";
			else title = "Perfect Score - ";
			title += award.getRange().getDistance() + "m, " + award.getRange().getFace().getDescription() + " face";
			pstmt.setString(12, title);// title,
			pstmt.setString(13, score.getEventLevel().substring(0, 1));// eventlevel
			System.out.println(pstmt.toString());
			pstmt.executeUpdate();
		}
	}

	public Rating checkInitialRating(Archer archer) throws NotEnoughOfficialRoundsException {
		Connection conn;
		try {
			conn = access.getConnection();
			conn.setAutoCommit(false);
			Statement stmt = transactionalConnection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) from roundscore s join round r on r.id=s.round_id " + "WHERE s.archer_id=" + archer.getId() + " AND s.equip_id=" + archer.getCurrentEquipment().getId() + " AND r.official=true");

			rs.next();
			int noScores = rs.getInt(1);
			if (noScores < 5) throw new NotEnoughOfficialRoundsException("Only " + rs.getInt(1) + " official round(s) shot. Need 5.");
			rs = stmt.executeQuery("SELECT AVG(rating) from roundscore s join round r on r.id=s.round_id " + "WHERE s.archer_id=" + archer.getId() + " AND s.equip_id=" + archer.getCurrentEquipment().getId() + " AND r.official=true");
			rs.next();
			// the example here implies rounding, not truncating
			// http://www.archery.org.au/FAQs/Records-Awards-Classifications/How-to-Determine-Rating-Handicap
			Rating newRating = new Rating(Math.round(rs.getFloat(1)));
			newRating.setComment("Initial rating, based on " + noScores + " scores.");
			newRating.setEquipment(archer.getCurrentEquipment());
			newRating.setDiscipline(archer.getCurrentDiscipline());

			rs = stmt.executeQuery("SELECT MAX(day_shot) from roundscore s join round r on r.id=s.round_id " + "WHERE archer_id=" + archer.getId() + " AND equip_id=" + archer.getCurrentEquipment().getId() + " AND official=true");
			rs.next();
			newRating.setFromDate(rs.getTimestamp(1));
			newRating.setUntilDate(Utils.getDefaultEndDate());
			return newRating;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateScore(Score score) {

	}

	public void storeInitialRatingInTransaction(Score score, Rating r) {
		Archer archer = score.getArcher();
		// make it clear this is a new rating, not subject to change by current score
		archer.setNewRating(r);
		String fromDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(r.getFromDate());
		String untilDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(r.getUntilDate());

		try {
			transactionalConnection = access.getConnection();
			Statement stmt = transactionalConnection.createStatement();
			String sql = "INSERT INTO rating (archer_id, rating, score_id, comment, valid_from, valid_until) " + "VALUES (" + r.getRating() + ", " + score.getId() + ", '" + r.getComment() + "', '" + fromDateString + "', '" + untilDateString + "')";
			System.out.println(sql);
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	public void storeNewRatingInTransaction(Score score) {
		Archer archer = score.getArcher();
		try {
			transactionalConnection = access.getConnection();
			Statement stmt = transactionalConnection.createStatement();

			Rating newRating = archer.getNewRating();
			String fromDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(newRating.getFromDate());
			String untilDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(newRating.getUntilDate());
			String sql = "INSERT INTO rating (archer_id, rating, score_id, comment, valid_from, valid_until) " + "VALUES (" + archer.getId() + ", " + newRating.getRating() + ", " + score.getId() + ", '" + newRating.getComment() + "', '" + fromDateString + "', '" + untilDateString + "')";
			System.out.println(sql);
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void endTransaction() throws SQLException {
		try {
			this.transactionalConnection.commit();
		} catch (SQLException e) {
			this.transactionalConnection.rollback();
		}
		this.transactionalConnection = null;
	}

}
