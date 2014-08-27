using System;
using System.Data.Common;
using System.Data.SqlClient;
using System.Data.SqlTypes;
using MySql.Data.MySqlClient;
using MySql.Data.Types;
using System.Collections.Generic;

public class DBHandler
{
	private MySqlConnection _connection;
	private string _serverName;
	private string _databaseName;
	private string _user;
	private string _password;

	public DBHandler ()
	{
		_serverName = "localhost";
		_databaseName = "archery_db";
		_user = "root";
		_password = "password";
		string connectionString = "Server=" + _serverName + ";Port=3306;Database=" + _databaseName + ";User ID=" + _user + ";Pwd=" + _password + ";";
		_connection = new MySqlConnection (connectionString);

	}

	private void OpenConnection()
	{
		try
		{
			if (_connection != null && _connection.State ==  System.Data.ConnectionState.Closed) 
			{
				_connection.Open ();
			}

		}
		catch (MySqlException e) 
		{
			Console.WriteLine("Connection Error!\n" + e.Message);
		}
	}
		
	private void CloseConnection()
	{
		try
		{
			if (_connection != null && _connection.State ==  System.Data.ConnectionState.Open) 
			{
				_connection.Close ();
			}
		}
		catch (MySqlException e) 
		{
			Console.WriteLine(e.Message);
		}
	}

	public List<string> List()
	{
		List<String> result = new List<String>();
		string queryString = "SELECT * FROM archer LIMIT 10;";
		OpenConnection ();
		MySqlCommand command = new MySqlCommand(queryString, _connection);
		try
		{

			int numberOfRecords = 0;
			MySqlDataReader reader = command.ExecuteReader();
			while (reader.Read())
			{
				string rowResult = "*";
				for (int i = 0; i < reader.FieldCount; i++)
				{
					rowResult += " " + reader[i].ToString();
				}
				result.Add(rowResult);
				numberOfRecords++;
			}
			Console.WriteLine(numberOfRecords + " results returned.");
			reader.Close();
			CloseConnection();
			return result;
		}
		catch (Exception e)
		{
			Console.WriteLine(e.Message);
			CloseConnection();
			return null;
		}
	}

	public void ExecuteNonQuery(string nonQueryString)
	{
		OpenConnection();
		Console.WriteLine(
			"The query: " + nonQueryString);
		MySqlCommand command = new MySqlCommand (nonQueryString, _connection);
		try
		{
			int numberOfRecords = command.ExecuteNonQuery();
			Console.WriteLine(
				numberOfRecords + " row(s) affected.");
			CloseConnection();
		}
		catch (Exception e)
		{
			Console.WriteLine(e.Message);
			CloseConnection();
		}
	}
		
	public List<Archer> Select(string queryString)
	{
		OpenConnection ();
		MySqlCommand command = new MySqlCommand (queryString, _connection);
		List<Archer> results = new List<Archer> ();
		MySqlDataReader reader = command.ExecuteReader();

		while (reader.Read())
		{
			if (reader.FieldCount != 17)
			{
				Console.WriteLine ("Error! Incorrect data!");
				return null;
			}
			Archer archer = new Archer ();

			archer.id = int.Parse(reader[0].ToString());
			archer.last_updated = reader[1].ToString();
			archer.archived = bool.Parse(reader[2].ToString());
			archer.fin_year = int.Parse(reader[3].ToString());
			archer.gender = char.Parse(reader[4].ToString());
			archer.surname = reader[5].ToString();
			archer.given_name = reader[6].ToString();
			archer.initial = reader[7].ToString().Length > 0 ? char.Parse(reader[7].ToString()) : ' ';
			archer.join_date = reader[8].ToString();
			archer.birthyear = int.Parse(reader[9].ToString());
			archer.status = char.Parse(reader[10].ToString());
			archer.club = reader[11].ToString();
			archer.notes = reader[12].ToString();
			archer.title = reader[13].ToString();
			archer.id_number = int.Parse(reader[14].ToString());
			archer.default_equipment_id = int.Parse(reader[15].ToString());
			archer.default_discipline_id = int.Parse(reader[16].ToString());

			results.Add (archer);
		}

		Console.WriteLine(results.Capacity + " results returned.\n");

		reader.Close();
		CloseConnection();
		return results;
	}
}

