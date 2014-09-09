using System;
using System.Data;
using System.Collections.Generic;

using Data = Spike2Data;

public class Spike2 {

	private Database database;
	private UIManager ui;


	public Spike2() {
		database = new Database("archery_db", "localhost", "root", "021190");
		database.ExecuteNonQuery("ALTER TABLE multieventrule ADD PRIMARY KEY (multieventcomp_id, round_id)");
		database.CreateRelation("comprule", "multieventcompetition", "id", "multieventrule", "multieventcomp_id");

		ui = new UIManager();
	}

	public void run() {
		bool running = true;
		while (running) {
			ui.Show(Data.Menu);
			string input = ui.ReadString("> ").ToLower();
			switch (input) {
				case "list":
					ui.NextLine();
					foreach (string s in RelationList()) {
						ui.Show(s);
					}
					break;
				case "find":
					ui.Show("Which table would you like to select from?");
					string table = ui.ReadString("> ").ToLower();
					List<string> results = SelectTable(table);
					if (results == null) {
						ui.Show("Please enter either 'Competition' or 'Rule'.");
						break;
					}
					ui.NextLine();
					if (results.Count > 0) {
						foreach (string s in results) {
							ui.Show(s);
						}
					} else {
						ui.Show("No data could be found.\nPlease check your input and try again.");
					}
					break;
				case "insert":
					bool insert = Insert(ui.ReadString("Specify the table to insert into: ").ToLower());
					if (!insert) ui.Show("Please enter either 'Competition' or 'Rule'.");
					break;
				case "update":
					bool update = Update(ui.ReadString("Specify the table you wish to update: ").ToLower());
					if (!update) ui.Show("Please enter either 'Competition' or 'Rule'.");
					break;
				case "delete":
					bool delete = Delete(ui.ReadString("Specify the table you wish to delete from: ").ToLower());
					if (!delete) ui.Show("Please enter either 'Competition' or 'Rule'.");
					break;
				case "quit":
					running = false;
					break;
				default:
					Console.WriteLine("\n'{0}' is not a valid option!", input);
					break;
			}
			ui.NextLine();
		}
	}

	public List<string> RelationList() {
		List<string> results = new List<string>();
		foreach (DataRow row in database.Rows("multieventcompetition")) {
			string rowResult = "Competition ID: " + row["id"].ToString() + " Round ID's: ";
			foreach (DataRow cRow in row.GetChildRows("comprule")) {
				rowResult += cRow["round_id"] + " ";
			}
			results.Add(rowResult);
		}
		return results;
	}

	public List<string> SelectTable(string table) {
		if (table == "competition") {
			string comp = ui.ReadString("Enter competition name: ");
			return database.Find(comp, "multieventcompetition");
		} else if (table == "rule") {
			int ruleCompId = ui.ReadInt("Enter the competition ID that applies to the rule you wish to select: ");
			int ruleRoundId = ui.ReadInt("Enter the round ID that applies to the rule you wish to select: "); 
			List<string> results = new List<string>();
			foreach (DataRow row in database.Rows("multieventrule")) {
				if (ruleCompId == int.Parse(row["multieventcomp_id"].ToString())) {
					if (ruleRoundId == int.Parse(row["round_id"].ToString())) {
						string rowResult = "";
						rowResult += "Competition ID for rule: " + row["multieventcomp_id"].ToString() + ". Round ID: " + row["round_id"].ToString();
						results.Add(rowResult);
					}
				}
				return results;
			}
		}
		return null;
	}

	public bool Insert(string table) {
		if (table == "competition") {
			Dictionary<string, object> row = new Dictionary<string, object>();
			row["comp_name"] = ui.ReadString("Competition title: ");
			row["period_start"] = DateTime.Now.ToString("yyyy-MM-dd");
			row["period_end"] = DateTime.Now.ToString("yyyy-MM-dd");
			database.InsertRow(row, "multieventcompetition");
		} else if (table == "rule") {
			Dictionary<string, object> row = new Dictionary<string, object>();
			row["multieventcomp_id"] = ui.ReadInt("Enter the competition ID: ");
			row["round_id"] = ui.ReadInt("Enter the round ID: ");
			row["calc_rules"] = ui.ReadString("Enter the rule type: ", input => input.ToLower() == "max" || input.ToLower() == "min" || input.ToLower() == "avg", "Please enter either MIN, MAX, or AVG.");
			row["events_included"] = ui.ReadInt("Enter the events included: ");
			database.InsertRow(row, "multieventrule");
		} else {
			return false;
		}
		return true;
	}

	public bool Update(string table) {
		if (table == "competition") {
			string name = ui.ReadString("Enter the competition name to update: ");
			Dictionary<string, object> row = new Dictionary<string, object>();
			object id = null;
			foreach (DataRow r in database.Rows("multieventcompetition")) {
				if (r["comp_name"].ToString() == name) {
					id = r["id"];
					row["comp_name"] = ui.ReadString("Enter a new competition name: ");
					break;
				}
			}
			database.UpdateRow(id, row, "multieventcompetition");
		} else if (table == "rule") {
			int rid = ui.ReadInt("Enter the competition ID to update rule for: ");
			Dictionary<string, object> row = new Dictionary<string, object>();
			object[] id = new object[2];
			foreach (DataRow r in database.Rows("multieventrule")) {
				if (int.Parse(r["multieventcomp_id"].ToString()) == rid) {
					id[0] = r["multieventcomp_id"];
					id[1] = r["round_id"];
					row["multieventcomp_id"] = ui.ReadString("Enter a new competition ID: ");
					break;
				}
			}
			database.UpdateRow(id, row, "multieventrule");
		} else {
			return false;
		}
		return true;
	}

	public bool Delete(string table) {
		if (table == "competition") {
			string name = ui.ReadString("Specify the name of the competition to delete: ");
			object id = null;
			foreach (DataRow r in database.Rows("multieventcompetition")) {
				if (r["comp_name"].ToString() == name) {
					id = r["id"];
					break;
				}
			}
			if (id == null) {
				Console.WriteLine("That competition doesn't exist!");
				Delete(table);
			}
			database.DeleteRow(id, "multieventcompetition");
		} else if (table == "rule") {
			int cid = ui.ReadInt("Enter the competition ID of the rule to delete: ");
			int rid = ui.ReadInt("Enter the round ID of the rule to delete: "); 
			database.DeleteRow(new object[] { cid, rid }, "multieventrule");
		} else {
			return false;
		}
		return true;
	}

	public static void Main(string[] args) {
		new Spike2().run();
	}
}
