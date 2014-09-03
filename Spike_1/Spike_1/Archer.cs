using System;

public class Archer
{
	public int id;
	public string last_updated;
	public bool archived;
	public int fin_year;
	public char gender;
	public string surname, given_name, join_date;
	public char initial;
	public int birthyear;
	public int id_number, default_equipment_id, default_discipline_id;

	public override string ToString() {
		return id + " " + last_updated + " " + archived + " " + fin_year + " " +
			gender + " " + surname + " " + given_name + " " + join_date + " " + initial + " " +
			birthyear + " "  + id_number + " " +
			default_equipment_id + " " + default_discipline_id;
	}

}