using System;
using System.Collections.Generic;

public class TestProgram {

	public static void Main(string[] args) {
		Database database = new Database("archery_db", "localhost", "root", "021190");
		database.Populate();

		List<string> results = database.FindExact("M", "archer");
		if (results.Count == 0) Console.WriteLine("No results found.");
		foreach (string s in results) {
			Console.WriteLine(s);
		}

	}
}
