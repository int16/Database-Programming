package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import scores.Archer;
import scores.AwardType;
import scores.Category;
import scores.Discipline;
import scores.Equipment;
import scores.Event;
import scores.EventLevel;
import scores.Face;
import scores.MultiEvent;
import scores.Range;
import scores.Round;
import scores.RoundGroup;

public class MiscellaneousData {
	private DataAccess access = null;

	public MiscellaneousData() {
		access = new DataAccess();

	}

	public Equipment[] getEquipmentListing() throws ClassNotFoundException, SQLException {
		Equipment[] equipList = null;
		Equipment equipment;
		Connection conn = access.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM equipment");
		rs.next();
		int noRows = rs.getInt(1);
		equipList = new Equipment[noRows];
		rs = stmt.executeQuery("SELECT id, name, bow_type FROM equipment");
		int i = 0;
		while (rs.next()) {
			equipment = new Equipment();
			equipList[i++] = equipment;
			equipment.setId(rs.getInt("id"));
			equipment.setName(rs.getString("name"));
			equipment.setEquipDesc(rs.getString("bow_type"));
		}
		return equipList; 
	}

	public ArrayList<EventLevel> getEventLevelListing() throws ClassNotFoundException, SQLException {
		ArrayList<EventLevel> equipList = new ArrayList<EventLevel>();
		EventLevel lvl;
		Connection conn = access.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT level, definition FROM eventlevel");
		while (rs.next()) {

			lvl = new EventLevel(rs.getString("level").charAt(0), rs.getString("definition"));
			equipList.add(lvl);

		}
		return equipList;
	}

	public Round[] getRoundListing(boolean includeOfficial, boolean includeInofficial) throws SQLException, ClassNotFoundException {
		String condition = "";
		if (includeOfficial && !includeInofficial) // nothing to fetch
		condition = " AND official=true";
		else if (!includeOfficial && includeInofficial) condition = " AND official=false";

		ArrayList<Round> rounds = new ArrayList<Round>();
		Round round;
		Connection conn = access.getConnection();
		Statement stmt = conn.createStatement();
		Date current = new Date(System.currentTimeMillis());
		ResultSet rs = stmt.executeQuery("SELECT id, name, discipline_id FROM round " + "WHERE valid_until > " + current + condition + ";");
		Discipline[] disc = getDisciplineListing();

		while (rs.next()) {
			round = new Round(rs.getInt("id"), rs.getString("name"));
			for (int i = 0; i < disc.length; i++) {
				if (rs.getInt("discipline_id") == disc[i].getId()) round.setDiscipline(disc[i]);
			}
			rounds.add(round);

		}
		Round[] roundArray = new Round[rounds.size()];
		for (int i = 0; i < roundArray.length; i++) {
			roundArray[i] = rounds.get(i);
		}
		return roundArray;
	}

	public MultiEvent[] getMultiEventListing() throws ClassNotFoundException, SQLException {

		MultiEvent me;
		int size = 10;
		MultiEvent[] mes = new MultiEvent[size];
		Connection conn = access.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT id, comp_name, period_start, period_end FROM multieventcompetition ORDER BY period_start desc");
		rs.setFetchSize(size);
		int i = 0;
		while (rs.next() && i < size) {

			me = new MultiEvent();
			me.setId(rs.getInt("id"));
			me.setName(rs.getString("comp_name"));
			me.setStart(rs.getDate("period_start"));
			me.setEnd(rs.getDate("period_end"));
			mes[i++] = me;

		}
		return mes;

	}

	public Event[] getLatestEventListing(int fetchSize) throws ClassNotFoundException, SQLException {

		Connection conn = access.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM event");
		rs.next();
		int rowCount = rs.getInt(1);
		if (rowCount < fetchSize) fetchSize = rowCount;
		Event e;
		Event[] es = new Event[fetchSize];

		rs = stmt.executeQuery("SELECT event.id, eventname, venue, round_id, time_shot, round.name" + " FROM event join round on round.id=event.round_id ORDER BY time_shot desc");
		rs.setFetchSize(fetchSize);
		int i = 0;
		while (rs.next() && i < fetchSize) {
			Round r = new Round(rs.getInt("round_id"), rs.getString("name"));
			e = new Event();
			e.setRound(r);
			e.setId(rs.getInt("id"));
			e.setName(rs.getString("eventname"));
			e.setVenue(rs.getString("venue"));
			e.setEventTime(rs.getTimestamp("time_shot"));
			es[i++] = e;

		}
		return es;
	}

	public Category getCategoryForArcher(Archer a) throws ClassNotFoundException, SQLException {
		Connection conn = access.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM event");
		rs.next();

		Category c = null;
		return c;
	}

	// public Archer getDetailForArcher(Archer a) throws ClassNotFoundException, SQLException
	// {
	// Connection conn = access.getConnection();
	// Statement stmt = conn.createStatement();
	// ResultSet rs = stmt.executeQuery("SELECT archived, fin_year, gender, join_date,"
	// + " initial, birthyear, status, club, notes, title, equipment.id, equipment.name"
	// + " bow_type, discipline.id, discipline.name, discipline.handicapped"
	// + "  FROM archer join equipment on equipment.id=archer.default_equipment_id"
	// + " join discipline on discipline.id=archer.default_discipline_id"
	// + "WHERE archer.id ="+a.getId());
	//
	// rs.next();
	// a.setArchived(rs.getBoolean("archived"));
	// a.setBirthYear(rs.getInt("birthyear"));
	// a.setGender(rs.getString("gender").charAt(0));
	// a.setJoinDate(rs.getDate("join_date"));
	// a.setClub(rs.getString("club"));
	// a.setFinYear(rs.getInt("fin_year"));
	// a.setInitial(rs.getString("initial").charAt(0));
	// a.setNotes(rs.getString("notes"));
	// a.setTitle(rs.getString("title"));
	// Discipline di = new Discipline();
	// di.setHandicapped(rs.getBoolean("discipline.handicapped"));
	// di.setId(rs.getInt("discipline.id"));
	// di.setName(rs.getString("discipline.name"));
	// a.setDefaultDiscipline(di);
	// Equipment e = new Equipment();
	// e.setEquipDesc(rs.getString("bow_type"));
	// e.setId(rs.getInt("equipment.id"));
	// e.setName(rs.getString("equipment.name"));
	// a.setDefaultEquipment(e);
	//
	// int age = Utils.getCurrentYear() - a.getBirthYear();
	// rs = stmt.executeQuery("SELECT id, name, from_age, to_age, gender FROM category"
	// + " WHERE gender='"+a.getGender()+"' AND from_age<="+age+" to_age>="+age);
	// Category cat = new Category();
	// rs.next();
	// cat.setFrom_age(rs.getInt("from_age"));
	// cat.setTo_age(rs.getInt("to_age"));
	// cat.setGender(rs.getString("gender").charAt(0));
	// cat.setId(rs.getInt("id"));
	// cat.setLevel(rs.getInt("level"));
	// cat.setName(rs.getString("name"));
	// a.setCategory(cat);
	//
	//
	// return a;
	// }

	// horrible piece of code
	public int[] getApplicableRounds(RoundGroup rg, Archer a) throws ClassNotFoundException, SQLException {
		Connection conn = access.getConnection();
		Statement stmt = conn.createStatement();
		// find the base rounds for this round group and person
		ResultSet rs = stmt.executeQuery("SELECT ai.id, max_distance, equivalentround" + "  FROM archeridentity ai JOIN equivalentround er on ai.id=er.archeridentity_id" + " WHERE roundgroup=" + rg.getGroup() + " AND category_id=" + a.getCurrentCat().getId() + " AND discipline_id=" + a.getCurrentDiscipline().getId() + " AND equipment_id=" + a.getCurrentEquipment().getId() + " AND er.valid_until > NOW() AND ai.valid_until > NOW()");

		// there are several base rounds only because of the darn FITA 720 and its alternative for compounders
		int equiv_rounds = 0;
		int[] equivalentround_ids = new int[10];
		while (rs.next()) {
			equivalentround_ids[equiv_rounds++] = rs.getInt("equivalentround");
		}

		// find out how many different rounds this archer is allowed to shoot as equivalents
		int noPossibleRounds = 0;
		for (int i = 0; i < equiv_rounds; i++) {
			rs = stmt.executeQuery("SELECT COUNT(round_id) FROM roundgroup r1" + " WHERE r1.roundgroup=" + rg.getGroup() + " AND level <=" + "(SELECT level FROM roundgroup r2 " + " WHERE r2.roundgroup=" + rg.getGroup() + " AND r2.round_id=" + equivalentround_ids[i] + " AND r2.roundgroup=r1.roundgroup)" + " AND valid_until > NOW()");

			rs.next();
			noPossibleRounds += rs.getInt(1);
		}

		int[] possibleRoundIds = new int[noPossibleRounds];
		// copy the base rounds that apply to this archer (by age and equipment)
		// the actual equivalent round should be in position 0 (except for FITA 720!):
		for (int i = 0; i < equiv_rounds; i++) {
			possibleRoundIds[i] = equivalentround_ids[i];
		}
		int noRounds = equiv_rounds;
		;
		// find the more difficult rounds that this archer is also allowed to shoot
		for (int i = 0; i < equiv_rounds; i++) {
			rs = stmt.executeQuery("SELECT round_id FROM roundgroup r1" + " WHERE r1.roundgroup=" + rg.getGroup() + " AND level <=" + "(SELECT level FROM roundgroup r2 " + " WHERE r2.roundgroup=" + rg.getGroup() + " AND r2.round_id=" + equivalentround_ids[i] + " AND r2.roundgroup=r1.roundgroup AND valid_until > NOW()) " + "AND valid_until > NOW() ORDER BY level");

			while (rs.next()) {
				// check duplicates
				boolean hasEntry = false;
				for (int k = 0; k < noRounds; k++) {
					if (rs.getInt("round_id") == possibleRoundIds[k]) {
						hasEntry = true;
						break;
					}
				}
				if (!hasEntry) possibleRoundIds[noRounds++] = rs.getInt("round_id");

			}
		}
		// truncate the list
		int[] rounds = new int[noRounds];
		for (int i = 0; i < noRounds; i++) {
			rounds[i] = possibleRoundIds[i];
		}
		return possibleRoundIds;
	}

	public void getDetailsForRound(Round r) throws SQLException, ClassNotFoundException {
		Connection conn = access.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * " + "  FROM round join discipline on round.discipline_id=discipline.id " + " WHERE round.id=" + r.getId());
		rs.next();

		r.setPreviousName(rs.getString("previous_name"));
		r.setFieldTargetCount(rs.getInt("field_target_count"));
		r.setMaxDistance(rs.getInt("max_distance"));
		r.setRangeCount(rs.getInt("ranges"));
		r.setOfficial(rs.getBoolean("official"));
		Discipline d = new Discipline();
		d.setHandicapped(rs.getBoolean("handicapped"));
		d.setId(rs.getInt("discipline_id"));
		d.setName("discipline.name");
		r.setDiscipline(d);

		PreparedStatement pstmt = conn.prepareStatement("SELECT r.id, r.distance, r.ends, r.arrows_per_end, " + "r.max_arrow_score, r.discipline_id, f.id, f.description, f.diameter, f.no_rings, " + "f.ring_width, f.inner_10, f.inner_10_width FROM round_range r " + "JOIN face f ON f.id=r.face_id WHERE r.id=?");
		Face face;
		Range range;

		ResultSet rs2;
		for (int i = 0; i < r.getRangeCount(); i++) {
			pstmt.setInt(1, rs.getInt("range" + (i + 1) + "_id"));
			rs2 = pstmt.executeQuery();
			rs2.next();
			range = new Range();
			range.setId(rs2.getInt("r.id"));
			range.setArrowsPerEnd(rs2.getInt("r.arrows_per_end"));
			range.setDistance(rs2.getInt("r.distance"));
			range.setEnds(rs2.getInt("r.ends"));
			range.setMaxArrowScore(rs2.getInt("r.max_arrow_score"));
			face = new Face();
			face.setDescription(rs2.getString("description"));
			face.setDiameter(rs2.getInt("diameter"));
			face.setId(rs2.getInt("f.id"));
			face.setInnerTen(rs2.getBoolean("inner_10"));
			face.setInnerTenWidth(rs2.getDouble("inner_10_width"));
			face.setNoRings(rs2.getInt("no_rings"));
			face.setRingWidth(rs2.getInt("ring_width"));
			range.setFace(face);
			r.addRange(i, range);
		}

	}

	public AwardType[] getAwardTypeForEndScores() throws ClassNotFoundException, SQLException {
		// ugly
		Connection conn = access.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT id, description, issued_by, comment " + "  FROM awardtype" + " WHERE id=1 or id=2 ORDER BY id asc");

		AwardType[] type = new AwardType[2];
		rs.next();
		type[0] = new AwardType();
		type[0].setId(rs.getInt("id"));
		type[0].setDescription(rs.getString("description"));
		type[0].setComment(rs.getString("comment"));
		type[0].setIssuer(rs.getString("issued_by"));
		rs.next();
		type[1] = new AwardType();
		type[1].setId(rs.getInt("id"));
		type[1].setDescription(rs.getString("description"));
		type[1].setComment(rs.getString("comment"));
		type[1].setIssuer(rs.getString("issued_by"));
		return type;
	}

	public Discipline[] getDisciplineListing() throws SQLException, ClassNotFoundException {
		Discipline[] discList = null;
		Discipline disc;
		Connection conn = access.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM discipline");
		rs.next();
		int noRows = rs.getInt(1);
		discList = new Discipline[noRows];
		rs = stmt.executeQuery("SELECT id, name, handicapped FROM discipline");
		int i = 0;
		while (rs.next()) {
			disc = new Discipline();
			discList[i++] = disc;
			disc.setId(rs.getInt("id"));
			disc.setName(rs.getString("name"));
			disc.setHandicapped(rs.getBoolean("handicapped"));
		}
		return discList;
	}

	public Category[] getCategoryListing() throws SQLException, ClassNotFoundException {
		Category[] cats = null;
		Category cat;
		Connection conn = access.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM category");
		rs.next();
		int noRows = rs.getInt(1);
		cats = new Category[noRows];
		rs = stmt.executeQuery("SELECT id, name, gender, from_age, to_age, level  FROM category");
		int i = 0;
		while (rs.next()) {
			cat = new Category();
			cats[i++] = cat;
			cat.setId(rs.getInt("id"));
			cat.setName(rs.getString("name"));
			cat.setFrom_age(rs.getInt("from_age"));
			cat.setTo_age(rs.getInt("to_age"));
			cat.setLevel(rs.getInt("level"));
			cat.setGender(rs.getString("gender").charAt(0));
		}

		return cats;
	}

}
