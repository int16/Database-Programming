using System;

public class Archer
{
	public int id;
	public string last_updated;
	public bool archived;
	public int fin_year;
	public char gender;
	public string surname, given_name;
	public char initial;
	public string join_date;
	public int birthyear;
	public char status;
	public string club, notes, title;
	public int id_number, default_equipment_id, default_discipline_id;

	public override string ToString() {
		return id + " " + last_updated + " " + archived + " " + fin_year + " " +
			gender + " " + surname + " " + given_name + " " + initial + " " + join_date + " " +
			birthyear + " " + status + " " + club + " " + notes + " " + title + " " + id_number + " " +
			default_equipment_id + " " + default_discipline_id;
	}

}