package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import exceptions.NoSuchEntryException;
import exceptions.TooManyEntriesException;

import java.util.ArrayList;

import scores.Archer;
import scores.Category;
import scores.Discipline;
import scores.Equipment;
import scores.Rating;
import scores.Utils;

public class ArcherData 
{
    DataAccess access;

    public ArcherData()
    {
	access = new DataAccess();
    }

    //	public Archer getArcherById(int id) throws ClassNotFoundException, SQLException, NoSuchEntryException
    //	{
    //		Connection conn = access.getConnection();
    //		Statement stmt = conn.createStatement();
    //		ResultSet rs = stmt.executeQuery("Select * from archer where id="+id);
    //		Archer a = resultSetToArcher(rs);
    //
    //		a.setDiscipline(getDisciplineForId(a.getDiscipline().getId()));
    //	
    //		a.setDefaultEquipment(getEquipmentForId(a.getDefaultEquipment().getId()));
    //		return a;
    //	}

    private Archer resultSetToArcher(ResultSet set) throws SQLException, NoSuchEntryException
    {
	if (set.next())
	{
	    Archer a = new Archer();
	    a.setFirstName(set.getString("given_name"));
	    a.setLastName(set.getString("surname"));
	    a.setId(set.getInt("id"));
	    a.setArchived(set.getBoolean("archived"));
	    a.setBirthYear(set.getInt("birthyear"));
	    a.setClub(set.getString("club"));
	    a.setFinYear(set.getInt("fin_year"));
	    a.setGender(set.getString("gender").charAt(0));
	    //ad.setInitial(set.getString("initial").charAt(0));
	    a.setJoinDate(set.getDate("join_date"));
	    a.setNotes(set.getString("notes"));
	    a.setStatus(set.getString("status").charAt(0));
	    a.setTitle(set.getString("title"));
	    return a;
	}
	else 
	    throw new NoSuchEntryException("No archer found.");


    }
    private Equipment getEquipmentForId(int id) throws ClassNotFoundException, SQLException, NoSuchEntryException
    {
	Connection conn = access.getConnection();
	Statement stmt = conn.createStatement();

	ResultSet rs = stmt.executeQuery("Select id, name, bow_type from equipment where id="+id);
	if(!rs.next())
	    throw new NoSuchEntryException("No equipment found for id.");
	Equipment e = new Equipment();
	e.setId(rs.getInt("id"));
	e.setName(rs.getString("name"));
	e.setEquipDesc(rs.getString("bow_type"));
	return e;
    }

    private Discipline getDisciplineForId(int id) throws ClassNotFoundException, SQLException, NoSuchEntryException
    {
	Connection conn = access.getConnection();
	Statement stmt = conn.createStatement();

	ResultSet rs = stmt.executeQuery("Select name, id, handicapped from discipline where id="+id);
	if(!rs.next())
	    throw new NoSuchEntryException("Can't find discipline by id.");
	Discipline d = new Discipline();
	d.setName(rs.getString("name"));
	d.setHandicapped(rs.getBoolean("handicapped"));
	d.setId(rs.getInt("id"));
	return d;
    }

    public Archer[] getListOfArchers(boolean includeArchived) throws ClassNotFoundException, SQLException 
    {
	ArrayList<Archer> list = new ArrayList<Archer>();
	Archer a;
	String sql = "SELECT archer.id, given_name, surname, "
			+ "gender, birthyear, status, club, equipment.id, equipment.name, equipment.bow_type, "
			+ "discipline.id, discipline.name, discipline.handicapped "
			+ "FROM archer JOIN equipment ON archer.default_equipment_id=equipment.id "
			+ "JOIN discipline ON archer.default_discipline_id=discipline.id WHERE archived=?"
			+ "	ORDER BY surname";
	
	PreparedStatement pstmt = access.getArcherListingStmt(sql);
	
	pstmt.setBoolean(1, includeArchived);
	ResultSet rs = pstmt.executeQuery();
	while(rs.next())
	{
	    a = new Archer();
	    a.setId(rs.getInt("id"));
	    a.setFirstName(rs.getString("given_name"));
	    a.setLastName(rs.getString("surname"));
	    a.setGender(rs.getString("gender").charAt(0));
	    a.setBirthYear(rs.getInt("birthyear"));
	    a.setStatus(rs.getString("status").charAt(0));
	    a.setClub(rs.getString("club"));
	    Equipment e = new Equipment();
	    e.setId(rs.getInt("equipment.id"));
	    e.setName(rs.getString("equipment.name"));
	    e.setEquipDesc(rs.getString("equipment.bow_type"));
	    a.setDefaultEquipment(e);
	    Discipline d = new Discipline();
	    d.setId(rs.getInt("discipline.id"));
	    d.setName(rs.getString("discipline.name"));
	    d.setHandicapped(rs.getBoolean("discipline.handicapped"));
	    a.setDefaultDiscipline(d);
	    list.add(a);
	}
	Archer[] archers = new Archer[list.size()];
	for (int i = 0; i < archers.length; i++)
	{
	    archers[i] = list.get(i);

	}
	return archers;
    }

    public void setCategoryForArcher(Archer a) throws ClassNotFoundException, SQLException, NoSuchEntryException
    {
	Connection conn = access.getConnection();
	Statement stmt = conn.createStatement();
	int age = Utils.getAge(a.getBirthYear());
	ResultSet rs = stmt.executeQuery("Select name, id, gender, from_age, to_age, level "
		+ " from category where gender='"+a.getGender()+"' AND "+age+" BETWEEN from_age and to_age AND valid_until > NOW()");
	if(!rs.next())
	    throw new NoSuchEntryException("No category found for archer.");
	Category cat = new Category();
	cat.setId(rs.getInt("id"));
	cat.setName(rs.getString("name"));
	cat.setGender(rs.getString("gender").charAt(0));
	cat.setFrom_age(rs.getInt("from_age"));
	cat.setTo_age(rs.getInt("to_age"));
	cat.setLevel(rs.getInt("level"));
	a.setCat(cat);
    }
    
    public void getCurrentRatingForArcher(Archer a) throws ClassNotFoundException, SQLException, TooManyEntriesException, NoSuchEntryException
    {
	Connection conn = access.getConnection();
	Statement stmt = conn.createStatement();
	String sql = "SELECT rating, score_id, comment, valid_from, valid_until from rating "
		+ "WHERE archer_id ="+a.getId()+" AND equip_id="+a.getCurrentEquipment().getId()
		+ " AND discipline_id="+a.getCurrentDiscipline().getId()+" AND valid_until>NOW()";
	System.out.println(sql);
	ResultSet rs = stmt.executeQuery(sql);
	if(!rs.next())
	    throw new NoSuchEntryException("Archer has no current rating for this equipment.");
	Rating r = new Rating(rs.getInt("rating"));
	r.setComment(rs.getString("comment"));
	r.setDiscipline(a.getDefaultDiscipline());
	r.setEquipment(a.getCurrentEquipment());
	r.setFromDate(rs.getTimestamp("valid_from"));
	r.setUntilDate(rs.getTimestamp("valid_until"));
	a.setRating(r);
	if(rs.next())
	    throw new TooManyEntriesException("More than one current rating! Problems in rating table!");
    }

 
}
