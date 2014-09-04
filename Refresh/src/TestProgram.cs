using System;
using System.Data;
using System.Collections.Generic;


public class TestProgram {

	private Database database;

	private TestProgram() {
		database = new Database("archery_db", "localhost", "root", "021190");
		database.Populate();
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
		foreach (string s in database.GetColumns("archer")) {
			Console.WriteLine(s);
		}
		//foreach (string col in database.AllColumns["archer"]) {
		//		Console.WriteLine(col);
		//	}
	}

	private void Search() {
		List<string> results = database.FindExact("M", "archer");
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


	public static void Main(string[] args) {
		TestProgram test = new TestProgram();
		test.GetInfo();
	}
}
