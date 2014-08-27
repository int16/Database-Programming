using System;
using System.Data.SqlClient;
using MySql.Data.MySqlClient;


public class QueryFactory
{

	private static string GetQueryStringFromCommand(MySqlCommand theCommand)
	{
		string query = theCommand.CommandText;

		foreach (MySqlParameter p in theCommand.Parameters)
		{
			query = query.Replace(p.ParameterName, p.Value.ToString());
		}
		return query;
	}

	public static string GetInsertQueryString()
	{
		string insertString =
			"INSERT INTO archer "
			+ "(archived, fin_year, gender, surname, given_name, birthyear, default_equipment_id, default_discipline_id) "
			+ "values "
			+ "(@archived, @fin_year, '@gender', '@surname', '@given_name', @birthyear, @default_equipment_id, @defualt_discipline_id);";
		MySqlCommand command = new MySqlCommand(insertString);
		command.Parameters.AddWithValue ("@archived", InputDataValidator.ReadInteger("Enter an intger value for archived column: "));
		command.Parameters.AddWithValue ("@fin_year", InputDataValidator.ReadYear("Enter a year value for finished year column: "));
		command.Parameters.AddWithValue ("@gender", InputDataValidator.ReadGender("Enter a gender value for gender column: "));
		command.Parameters.AddWithValue ("@surname", InputDataValidator.ReadString ("Enter a surname for the surname column: "));
		command.Parameters.AddWithValue("@given_name", InputDataValidator.ReadString("Enter a first name for the given_name column: "));
		command.Parameters.AddWithValue("@birthyear", InputDataValidator.ReadYear("Enter a year value for birth year column: "));
		command.Parameters.AddWithValue("@default_equipment_id", InputDataValidator.ReadInteger("Enter an intger value for default equipment column: "));
		command.Parameters.AddWithValue("@defualt_discipline_id", InputDataValidator.ReadInteger("Enter an intger value for default discipline column: "));
		return GetQueryStringFromCommand(command);
	}


	public static string GetUpdateQueryString()
	{
		string updateString =
			"UPDATE archer "
			+ "SET archived=@archived, "
			+ "fin_year=@fin_year, "
			+ "gender='@gender', "
			+ "birthyear=@birthyear, "
			+ "default_equipment_id=@default_equipment_id, "
			+ "default_discipline_id=@defualt_discipline_id "
			+ "WHERE given_name='@setGivenName';";
		MySqlCommand command = new MySqlCommand(updateString);
		command.Parameters.AddWithValue ("@archived", InputDataValidator.ReadInteger("Enter an intger value for archived column: "));
		command.Parameters.AddWithValue ("@fin_year", InputDataValidator.ReadYear("Enter a year value for finished year column: "));
		command.Parameters.AddWithValue ("@gender", InputDataValidator.ReadGender("Enter a gender value for gender column: "));
		command.Parameters.AddWithValue("@birthyear", InputDataValidator.ReadYear("Enter a year value for birth year column: "));
		command.Parameters.AddWithValue("@default_equipment_id", InputDataValidator.ReadInteger("Enter an intger value for default equipment column: "));
		command.Parameters.AddWithValue("@defualt_discipline_id", InputDataValidator.ReadInteger("Enter an intger value for default discipline column: "));
		command.Parameters.AddWithValue("@setGivenName", InputDataValidator.ReadString("Enter given name of entry to update: "));
		return GetQueryStringFromCommand(command);
	}

	public static string GetSelectString()
	{
		string selectString =
			"SELECT * FROM archer WHERE given_name = '@setGivenName' AND surname = '@setSurName';";
		MySqlCommand command = new MySqlCommand(selectString);
		command.Parameters.AddWithValue("@setGivenName", InputDataValidator.ReadString("Enter the first name of the archer you wish to locate: "));
		command.Parameters.AddWithValue("@setSurName", InputDataValidator.ReadString("Enter the last name of the archer you wish to locate: "));
		return GetQueryStringFromCommand(command);	
	}

	public static string GetDeleteString()
	{
		string selectString =
			"DELETE FROM archer WHERE given_name = '@setGivenName' AND surname = '@setSurName';";
		MySqlCommand command = new MySqlCommand(selectString);
		command.Parameters.AddWithValue("@setGivenName", InputDataValidator.ReadString("Enter the first name of the archer you wish to locate: "));
		command.Parameters.AddWithValue("@setSurName", InputDataValidator.ReadString("Enter the last name of the archer you wish to locate: "));
		return GetQueryStringFromCommand(command);	
	}
}

