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

	private List<string> tables = new List<string>();
	private List<RelationData> relations = new List<RelationData>();

	//private Dictionary<string, DataRelation> relations = new Dictionary<string, DataRelation>();
	private Dictionary<string, List<string>> columns = new Dictionary<string, List<string>>();
	private Dictionary<string, MySqlDataAdapter> adapters = new Dictionary<string, MySqlDataAdapter>();

	public Database(string name, string host, string username, string password) {
		string url = "SERVER=" + host + ";DATABASE=" + name + ";USER ID=" + username + ";PWD=" + password + ";allow zero datetime=true;";
		connection = new MySqlConnection(url);
		Populate();
	}

	public bool Connect() {
		try {
			if (connection != null) {
				if (connection.State == ConnectionState.Closed) {
					connection.Open();
					return true;
				}
			}
			return false;
		} catch (MySqlException e) {
			#if VERBOSE
				Console.WriteLine(e.Message);
			#endif
			return false;
		}
	}

	public bool Disconnect() {
		try {
			if (connection != null) {
				if (connection.State == ConnectionState.Open) {
					connection.Close();
					return true;
				}
			}
			return false;
		} catch (MySqlException e) {
			#if VERBOSE
				Console.WriteLine(e.Message);
			#endif
			return false;
		}
	}

	private List<string> GetTableNames() {
		List<string> results = new List<string>();
		bool connected = Connect();
		DataTable data = connection.GetSchema("Tables");
		foreach (DataRow row in data.Rows) {
			results.Add(row["TABLE_NAME"].ToString());
		}
		if (connected) Disconnect();
		return results;
	}

	public List<string> GetColumnNames(string table) {
		DataTable t = data.Tables[table];
		if (t == null) return null; // Invalid table name
		List<string> results = new List<string>();
		bool connected = Connect();
		foreach (DataColumn column in t.Columns) {
			results.Add(column.ColumnName);
		}
		if (connected) Disconnect();
		return results;
	}

	public void Populate() {
		data = new DataSet();
		bool connected = Connect();
		if (tables.Count == 0) tables = GetTableNames();
		adapters.Clear();
		for (int i = 0; i < tables.Count; i++) {
			adapters.Add(tables[i], CreateAdapter("SELECT * FROM " + tables[i]));
			try {
				adapters[tables[i]].Fill(data, tables[i]);
			} catch (FormatException e) {
				#if VERBOSE
					Console.WriteLine(e.Message);
				#endif
				continue;
			} catch (Exception e) {
				#if VERBOSE
					Console.WriteLine(e.Message);
				#endif
			}
		}
		foreach (RelationData relation in relations) {
			CreateRelation(relation.Name, relation.ParentTable, relation.ParentColumn, relation.ChildTable, relation.ChildColumn);
		}
		if (columns.Count == 0) {
			foreach (string table in tables) {
				columns.Add(table, GetColumnNames(table));
			}
		}
		if (connected) Disconnect();
	}

	public MySqlCommand CreateCommand(string sql) {
		return new MySqlCommand(sql, connection);
	}

	public MySqlDataAdapter CreateAdapter(string sql) {
		return new MySqlDataAdapter(sql, connection);
	}

	public DataRowCollection Rows(string table) {
		DataTable t = data.Tables[table];
		if (t == null) return null; // Invalid table name specified
		return t.Rows;
	}

	public void CreateRelation(string name, string ptable, string pcol, string ctable, string ccol) {
		DataTable parent = data.Tables[ptable];
		if (parent == null) return; // Invalid table name specified
		DataTable child = data.Tables[ctable];
		if (child == null) return; // Invalid table name specified
		DataRelation relation = new DataRelation(name, parent.Columns[pcol], child.Columns[ccol]);
		data.Relations.Add(relation);
		foreach (RelationData r in relations) {
			if (r.Name == name) return;
		}
		RelationData rel = new RelationData(name, ptable, pcol, ctable, ccol);
		relations.Add(rel);
	}

	public List<string> Find(string keywords, string table) {
		List<string> results = new List<string>();
		string[] search = new string[1];
		if (keywords.Contains(" ")) search = keywords.Split(new char[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
		else search[0] = keywords;
		DataTable t = data.Tables[table];
		if (t == null) return null; // Invalid table name specified
		bool connected = Connect();
		foreach (DataRow row in t.Rows) {
			foreach (DataColumn col in t.Columns) {
				string value = row[col].ToString();
				foreach (string s in search) {
					if (value.Contains(s)) {
						string result = "";
						foreach (DataColumn c in t.Columns) {
							result += row[c].ToString() + " ";
						}
						result = result.Trim();
						if (!results.Contains(result)) results.Add(result);
						break;
					}
				}
			}
		}
		if (connected) Disconnect();
		return results;
	}

	public List<string> FindExact(string exact, string table) {
		List<string> results = new List<string>();
		DataTable t = data.Tables[table];
		if (t == null) return null; // Invalid table name specified
		bool connected = Connect();
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
		if (connected) Disconnect();
		return results;
	}

	public DataRow FindRowByKey(object key, string table) {
		DataTable t = data.Tables[table];
		if (t == null) return null; // Invalid table name specified
		bool connected = Connect();
		foreach (DataRow row in t.Rows) {
			foreach (DataColumn col in t.Columns) {
				string value = row[col].ToString();
				if (value.Equals(key.ToString())) {
					if (connected) Disconnect();
					return row;
				}
			}
		}
		if (connected) Disconnect();
		return null;
	}

	public DataRow FindRowByKey(object[] keys, string table) {
		DataTable t = data.Tables[table];
		if (t == null) return null; // Invalid table name specified
		bool connected = Connect();
		foreach (DataRow row in t.Rows) {
			int amount = keys.Length;
			foreach (DataColumn col in t.Columns) {
				foreach (object key in keys) {
					string value = row[col].ToString();
					if (value.Equals(key.ToString())) {
						amount--;
						break;
					}
				}
				if (amount == 0) {
					if (connected) Disconnect();
					return row;
				}
			}
		}
		if (connected) Disconnect();
		return null;
	}

	public DataRow FindRow(string keywords, string table) {
		DataTable t = data.Tables[table];
		if (t == null) return null; // Invalid table name specified
		string[] search = new string[1];
		if (keywords.Contains(" ")) keywords.Split(keywords.ToCharArray(), StringSplitOptions.RemoveEmptyEntries);
		else search[0] = keywords;
		bool connected = Connect();
		foreach (DataRow row in t.Rows) {
			foreach (DataColumn col in t.Columns) {
				string value = row[col].ToString();
				foreach (string s in search) {
					if (value.Contains(s)) {
						Disconnect();
						return row;
					}
				}
			}
		}
		if (connected) Disconnect();
		return null;
	}

	public List<DataRow> FindRows(string keywords, string table) {
		List<DataRow> results = new List<DataRow>();
		DataTable t = data.Tables[table];
		if (t == null) return null; // Invalid table name specified
		string[] search = new string[1];
		if (keywords.Contains(" ")) keywords.Split(keywords.ToCharArray(), StringSplitOptions.RemoveEmptyEntries);
		else search[0] = keywords;
		bool connected = Connect();
		foreach (DataRow row in t.Rows) {
			foreach (DataColumn col in t.Columns) {
				string value = row[col].ToString();
				foreach (string s in search) {
					if (value.Contains(s)) {
						results.Add(row);
						break;
					}
				}
			}
		}
		if (connected) Disconnect();
		return results;
	}

	public bool Update(string key, string table) {
		return false;
	}

	public bool Delete(string key, string table) {
		DataRow row = FindRowByKey(key, table);
		if (row == null) return false;
		row.Delete();
		adapters[table].DeleteCommand = new MySqlCommandBuilder(adapters[table]).GetDeleteCommand();
		adapters[table].Update(data, table);
		return true;
	}

	public List<string> List(string table) {
		DataTable t = data.Tables[table];
		if (t == null) return null; // Invalid table name specified
		List<string> results = new List<string>();
		foreach (DataRow row in t.Rows) {
			string r = "";
			foreach (DataColumn col in t.Columns) {
				r += row[col] + " ";
			}
			results.Add(r.Trim());
		}
		return results;
	}

	public List<DataRow[]> GetChildRows(string table, string relation) {
		List<DataRow[]> results = new List<DataRow[]>();
		foreach (DataRow row in data.Tables[table].Rows) {
			DataRow[] rows = row.GetChildRows(relation);
			results.Add(rows);
		}
		return results;
	}

	public bool ExecuteNonQuery(string sql) {
		bool connected = Connect();
		MySqlCommand command = new MySqlCommand(sql, connection);
		try {
			command.ExecuteNonQuery();
		} catch (MySqlException e) {
			#if VERBOSE
				Console.WriteLine("Error! " + e.Message);
			#endif
			return false;
		}
		if (connected) Disconnect();
		return true;
	}

	public bool InsertRow(Dictionary<string, object> row, string table) {
		DataTable t = data.Tables[table];
		if (t == null) return false; // Invalid table name specified
		DataRow r = t.NewRow();
		foreach (KeyValuePair<string, object> entry in row) {
			r[entry.Key] = entry.Value;
		}
		// TODO: This can probably be removed
		adapters[table].InsertCommand = new MySqlCommandBuilder(adapters[table]).GetInsertCommand();
		data.Tables[table].Rows.Add(r);
		try {
			adapters[table].Update(t);
		} catch (Exception e) {
			#if VERBOSE
				Console.WriteLine(e.Message);
			#endif
		}
		Populate();
		return true;
	}

	public bool UpdateRow(object key, Dictionary<string, object> row, string table) {
		return UpdateRow(new object[] { key }, row, table);
	}

	public bool UpdateRow(object[] keys, Dictionary<string, object> row, string table) {
		DataTable t = data.Tables[table];
		if (t == null) return false; // Invalid table name specified
		DataRow r = FindRowByKey(keys, table);
		foreach (KeyValuePair<string, object> entry in row) {
			r[entry.Key] = entry.Value;
		}
		adapters[table].UpdateCommand = new MySqlCommandBuilder(adapters[table]).GetUpdateCommand();
		adapters[table].Update(t);
		Populate();
		return true;
	}

	public List<string> Tables {
		get { return tables; }
	}

	public List<string> GetColumns(string table) {
		return columns[table];
	}

	public Dictionary<string, List<string>> AllColumns {
		get { return columns; }
	}

}