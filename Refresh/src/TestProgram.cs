#define DEBUG

using System;
using System.Data;
using System.Collections.Generic;

public class TestProgram {


	private Database database;

	private TestProgram() {
		database = new Database("archery_db", "localhost", "root", "021190");
		database.Populate();
		database.ExecuteNonQuery("ALTER TABLE multieventrule ADD PRIMARY KEY (multieventcomp_id, round_id)");
		database.CreateRelation("relate", "multieventcompetition", "id", "multieventrule", "multieventcomp_id");
	}

	private void GetInfo() {
		Console.WriteLine("Tables");
		Console.WriteLine("======");
		foreach (string table in database.Tables) {
			Console.WriteLine(table);
		}
		Console.WriteLine("\n");

		Console.WriteLine("Columns");
		Console.WriteLine("=======");
		foreach (string s in database.GetColumns("face")) {
			Console.WriteLine(s);
		}
	}

	private void Search() {
		List<string> results = database.Find("2013", "archer");
		if (results.Count == 0) Console.WriteLine("No results found.");
		foreach (string s in results) {
			Console.WriteLine(s);
		}
	}

	private void Rows() {
		List<DataRow> rows = database.FindRows("FITA", "face");
		foreach (DataRow row in rows) {
			for (int i = 0; i < row.ItemArray.Length; i++) {
				Console.Write(row[i] + " ");
			}
			Console.WriteLine();
		}
	}

	private void Delete() {
		database.Delete("120", "archer");
	}

	private void List() {
		List<string> list = database.List("face");
		foreach (string s in list) {
			Console.WriteLine(s);
		}
	}

	private void Relation() {
		List<DataRow[]> rows = database.GetChildRows("multieventcompetition", "relate");
		foreach (DataRow[] r in rows) {
			foreach (DataRow row in r) {
				for (int i = 0; i < row.ItemArray.Length; i++) {
					Console.Write(row[i] + " ");
				}
				Console.WriteLine();
			}
		}
	}

	public static void Main(string[] args) {
		TestProgram test = new TestProgram();
		test.Relation();
	}
}
