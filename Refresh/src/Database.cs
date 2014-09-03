using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Data.SqlTypes;

using MySql.Data.MySqlClient;
using MySql.Data.Types;

public class Database {

	private MySqlConnection connection;
	private DataSet data;

	private Dictionary<string, MySqlDataAdapter> adapters = new Dictionary<string, MySqlDataAdapter>();

	public Database(string name, string host, string username, string password) {
		string url = "SERVER=" + host + ";DATABASE=" + name + ";USER ID=" + username + ";PWD=" + password + ";allow zero datetime=true;";
		connection = new MySqlConnection(url);
		data = new DataSet();
	}

	public void Connect() {
		try {
			if (connection != null) {
				if (connection.State == ConnectionState.Closed) connection.Open();
				else Console.WriteLine("Error! Connection already open!");
			}
		} catch (MySqlException e) {
			Console.WriteLine(e.Message);
		}
	}

	public void Disconnect() {
		try {
			if (connection != null) {
				if (connection.State == ConnectionState.Open) connection.Close();
				else Console.WriteLine("Error! Connection already closed!");
			}
		} catch (MySqlException e) {
			Console.WriteLine(e.Message);
		}
	}

	private List<string> GetTableNames() {
		List<string> result = new List<string>();
		DataTable data = connection.GetSchema("Tables");
		foreach (DataRow row in data.Rows) {
			result.Add(row["TABLE_NAME"].ToString());
		}
		return result;
	}

	public void Populate() {
		Connect();
		List<string> tables = GetTableNames();
		for (int i = 0; i < tables.Count; i++) {
			adapters.Add(tables[i], CreateAdapter("SELECT * FROM " + tables[i]));
			try {
				adapters[tables[i]].Fill(data, tables[i]);
			} catch (FormatException e) {
				Console.WriteLine(e.Message);
				continue;
			}
		}
		Disconnect();
	}

	public MySqlCommand CreateCommand(string sql) {
		return new MySqlCommand(sql, connection);
	}

	public MySqlDataAdapter CreateAdapter(string sql) {
		return new MySqlDataAdapter(sql, connection);
	}

	public List<string> Find(string keywords, string table) {
		List<string> results = new List<string>();
		string[] search = new string[1];
		if (keywords.Contains(" ")) keywords.Split(keywords.ToCharArray(), StringSplitOptions.RemoveEmptyEntries);
		else search[0] = keywords;
		DataTable t = data.Tables[table];
		if (t == null) return null; // Invalid table name specified
		Connect();
		foreach (DataRow row in t.Rows) {
			foreach (DataColumn col in t.Columns) {
				string value = row[col].ToString();
				foreach (string s in search) {
					if (value.Contains(s)) {
						string result = "";
						foreach (DataColumn c in t.Columns) {
							result += row[c].ToString() + " ";
						}
						results.Add(result.Trim());
						break;
					}
				}
			}
		}
		Disconnect();
		return results;
	}

	public List<string> FindExact(string exact, string table) {
		List<string> results = new List<string>();
		DataTable t = data.Tables[table];
		if (t == null) return null; // Invalid table name specified
		Connect();
		foreach (DataRow row in t.Rows) {
			foreach (DataColumn col in t.Columns) {
				string value = row[col].ToString();
				if (value.Equals(exact)) {
					string result = "";
					foreach (DataColumn c in t.Columns) {
						result += row[c].ToString() + " ";
					}
					results.Add(result.Trim());
					break;
				}
			}
		}
		Disconnect();
		return results;
	}

	public DataRow FindRowByKey(string key, string table) {
		DataTable t = data.Tables[table];
		if (t == null) return null; // Invalid table name specified
		Connect();
		foreach (DataRow row in t.Rows) {
			foreach (DataColumn col in t.Columns) {
				string value = row[col].ToString();
				if (value.Equals(key)) {
					Disconnect();
					return row;
				}
			}
		}
		Disconnect();
		return null;
	}

	public bool Delete(string key, string table) {
		DataRow row = FindRowByKey(key, table);
		if (row == null) return false;
		row.Delete();
		adapters[table].DeleteCommand = new MySqlCommandBuilder(adapters[table]).GetDeleteCommand();
		adapters[table].Update(data, table);
		return true;
	}

}