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
		_password = "021190";
		string connectionString = "Server=" + _serverName + ";Database=" + _databaseName + ";User ID=" + _user + ";Pwd=" + _password + ";";
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
			Console.WriteLine(e.Message);
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
		
	public List<string> Select(string queryString)
	{
		OpenConnection ();
		MySqlCommand command = new MySqlCommand (queryString, _connection);
		List<String> result = new List<String>();
		try
		{
			int numberOfRecords = 0;
			MySqlDataReader reader = command.ExecuteReader();
			while (reader.Read())
			{
				string rowResult = "";
				for (int i = 0; i < reader.FieldCount; i++)
				{
					rowResult += reader[i].ToString() +  " ";
				}
				result.Add(rowResult);
				numberOfRecords++;
			}
			Console.WriteLine(numberOfRecords + " results returned.");
			reader.Close();
			CloseConnection();
			return result;
		}
		catch (Exception ex)
		{
			Console.WriteLine(ex.Message);
			CloseConnection();
			return null;
		}
	}
}

