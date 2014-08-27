using System;
using System.Data;
using System.Data.Common;
using System.Data.SqlClient;
using System.Data.SqlTypes;
using MySql.Data.MySqlClient;
using MySql.Data.Types;
using System.Collections.Generic;
using System.Data.SqlClient;
using MySql.Data.MySqlClient;

public class DBHandler
{
	private MySqlConnection _connection;
	private string _serverName;
	private string _databaseName;
	private string _user;
	private string _password;
	private DataSet _theDataSet;
	private DataRelation _theRelation;

	public DBHandler ()
	{
		_serverName = "localhost";
		_databaseName = "archery_db";
		_user = "root";
		_password = "021190";
		string connectionString = "Server=" + _serverName + ";Database=" + _databaseName + ";User ID=" + _user + ";Pwd=" + _password + ";";
		_connection = new MySqlConnection (connectionString);
		_theDataSet = new DataSet ();
		PopulateDataSetAndRelation ();
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

	private void PopulateDataSetAndRelation()
	{
		OpenConnection ();
		MySqlDataAdapter competitionAdapter = new MySqlDataAdapter("SELECT * FROM multieventcompetition", _connection);
		MySqlDataAdapter ruleAdapter = new MySqlDataAdapter("SELECT * FROM multieventrule", _connection);
		competitionAdapter.Fill(_theDataSet, "Competition");
		ruleAdapter.Fill(_theDataSet, "Rules");

		_theRelation = _theDataSet.Relations.Add("Events",
		_theDataSet.Tables["Competition"].Columns["id"],
		_theDataSet.Tables["Rules"].Columns["multieventcomp_id"]);
		DataColumn[] keyColumn = new DataColumn[2];

		keyColumn[0] = _theDataSet.Tables["Rules"].Columns["multieventcomp_id"];
		keyColumn[1] = _theDataSet.Tables["Rules"].Columns["round_id"];
		_theDataSet.Tables["Rules"].PrimaryKey = keyColumn;
	}

	public void Delete(string fromTable){
		OpenConnection ();
		switch (fromTable.ToLower())
		{
		case "competition":
			{
				string compToDelete = InputDataValidator.ReadString ("Enter competition name to delete: "); 
				MySqlDataAdapter adapter = new MySqlDataAdapter("SELECT * FROM multieventcompetition", _connection);
				adapter.DeleteCommand = new MySqlCommandBuilder(adapter).GetDeleteCommand();
				Console.WriteLine (adapter.DeleteCommand.CommandText);
				for (int i = 0; i < _theDataSet.Tables ["Competition"].Rows.Count; i++) 
				{
					if (_theDataSet.Tables ["Competition"].Rows[i] ["comp_name"].ToString() == compToDelete) 
					{
						int idOfComp = int.Parse(_theDataSet.Tables ["Competition"].Rows[i] ["id"].ToString());
						for (int j = 0; j < _theDataSet.Tables ["Rules"].Rows.Count; j++) 
						{
							if (int.Parse(_theDataSet.Tables ["Rules"].Rows[j] ["multieventcomp_id"].ToString()) == idOfComp)
							{
								Console.WriteLine("You can't delete this competition because it has child relationships in the Rules table.");
								break;
							}
							else
							{
								_theDataSet.Tables ["Competition"].Rows [i].Delete ();
								adapter.Update (_theDataSet, "Competition");
								break;
							}
						}
					}
					else
					{
						Console.WriteLine("The competition you have specified does not exist.");
					}
				}
				break;
			}
		case "rule":
			{
				int ruleCompId = InputDataValidator.ReadInteger ("Enter the competition ID that applies to the rule you wish to delete: ");
				int ruleRoundId = InputDataValidator.ReadInteger ("Enter the round ID that applies to the rule you wish to delete: "); 
				MySqlDataAdapter adapter = new MySqlDataAdapter("SELECT * FROM multieventrule", _connection);
				adapter.DeleteCommand = new MySqlCommand("DELETE FROM `multieventrule` WHERE ((`multieventcomp_id` = @p1) AND (`round_id` = @p2))");
				adapter.DeleteCommand.Parameters.AddWithValue ("@p1", ruleCompId);
				adapter.DeleteCommand.Parameters.AddWithValue ("@p2", ruleRoundId);
				for (int i = 0; i < _theDataSet.Tables ["Rules"].Rows.Count; i++) 
				{
					if ((int.Parse(_theDataSet.Tables ["Rules"].Rows[i] ["multieventcomp_id"].ToString()) == ruleCompId) && (int.Parse(_theDataSet.Tables ["Rules"].Rows[i] ["round_id"].ToString()) == ruleRoundId))
					{
						_theDataSet.Tables ["Rules"].Rows [i].Delete ();
						adapter.Update (_theDataSet, "Rules");
						break;
					}
					else
					{
						Console.WriteLine("The rule you have specified does not exist.");
					}
				}
				break;
			}
		default:
			{
				Console.WriteLine("You have specified an invalid table name.");
				break;
			}
		}
		CloseConnection ();
	}

	public void Update(){
		try
		{
			OpenConnection ();
			string compToUpdate = InputDataValidator.ReadString ("Eneter competition name to update: "); 
			MySqlDataAdapter adapter = new MySqlDataAdapter("SELECT * FROM multieventcompetition", _connection);
			adapter.UpdateCommand = new MySqlCommandBuilder(adapter).GetUpdateCommand();
			DataSet dataSet = new DataSet();
			adapter.Fill (dataSet, "Competition");
			int changes = 0;
			foreach (DataRow dR in dataSet.Tables["Competition"].Rows)
			{
				if (dR ["comp_name"].ToString() == compToUpdate) {
					dR["comp_name"] = InputDataValidator.ReadString ("Enter a new competition title: ");
					changes++;
					break;
				}
			}
			adapter.Update (dataSet, "Competition");
			Console.WriteLine(changes + " changes were made to the database.");
			CloseConnection ();
		}
		catch (SystemException ex) 
		{
			Console.WriteLine (ex + "DataSet could not be initialised properly.");
		}
	}

	public void Insert(){
		try
		{
			OpenConnection ();
			MySqlDataAdapter adapter = new MySqlDataAdapter("SELECT * FROM multieventcompetition", _connection);
			adapter.InsertCommand = new MySqlCommandBuilder(adapter).GetInsertCommand();
			DataSet dataSet = new DataSet();
			adapter.Fill (dataSet, "Competition");
			DataTable tblCompetition;
			tblCompetition = dataSet.Tables["Competition"];
			DataRow newCompetitionRow = tblCompetition.NewRow();
			newCompetitionRow ["comp_name"] = InputDataValidator.ReadString ("Enter a new competition title: ");
			newCompetitionRow ["period_start"] = DateTime.Now.ToString("yyyy-MM-dd");
			newCompetitionRow ["period_end"] = DateTime.Now.ToString("yyyy-MM-dd");
			dataSet.Tables ["Competition"].Rows.Add (newCompetitionRow);
			adapter.Update (dataSet, "Competition");
			CloseConnection ();
		}
		catch (SystemException ex) 
		{
			Console.WriteLine (ex + "Insert failed.");

		}
	}

	public List<string> Select()
	{
		List<String> result = new List<String>();
		try
		{
			OpenConnection ();
			MySqlDataAdapter mecAdapter = new MySqlDataAdapter(
				"SELECT * " +
				"FROM multieventcompetition", _connection);
			DataSet events = new DataSet();
			mecAdapter.Fill(events, "Competition");
			string compToFind = InputDataValidator.ReadString ("Enter competition name to find: ");
			foreach (DataRow pRow in events.Tables["Competition"].Rows)
			{
				if (compToFind == pRow["comp_name"].ToString())
				{
					string rowResult = "";
					rowResult += "Competition ID: " + pRow["id"].ToString() + " " + pRow["comp_name"].ToString();
					result.Add(rowResult);
					break;
				}
			}
			CloseConnection ();
			return result;
		}
		catch (SystemException ex) 
		{
			Console.WriteLine (ex + "DataSet could not be initialised properly.");
			return null;

		}
	}
		
	public List<string> List()
	{
		List<String> result = new List<String>();
		try
		{
			OpenConnection ();
			MySqlDataAdapter merAdapter = new MySqlDataAdapter(
				"SELECT * FROM multieventcompetition", _connection);
			MySqlDataAdapter mecAdapter = new MySqlDataAdapter(
				"SELECT * FROM multieventrule", _connection);

			DataSet events = new DataSet();

			mecAdapter.Fill(events, "Rules");
			merAdapter.Fill(events, "Competition");

			DataRelation relation = events.Relations.Add("Events",
				events.Tables["Competition"].Columns["id"],
				events.Tables["Rules"].Columns["multieventcomp_id"]);
	
			foreach (DataRow pRow in events.Tables["Competition"].Rows)
			{
				string rowResult = "";
				rowResult += "Competition ID: " + pRow["id"].ToString() +  " Round ID's: ";
				foreach (DataRow cRow in pRow.GetChildRows(relation))
				{
					rowResult += cRow["round_id"] + " ";
				}
				result.Add(rowResult);
			}
			CloseConnection ();
			return result;
		}
		catch (SystemException ex) 
		{
			Console.WriteLine (ex + "DataSet could not be initialised properly.");
			return null;

		}
	}
}

