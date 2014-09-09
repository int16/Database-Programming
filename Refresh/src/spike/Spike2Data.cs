using System;

public class Spike2Data {

	private static string menu = 
		"Please specify one of the following options:\n" +
		"[find]   - Select a row from the archer table\n" +
		"[list]   - Display foreign key mapping in DataSet\n" +
		"[insert] - Insert DataSet table\n" +
		"[update] - Update a row in the archer table\n" +
		"[delete] - Delete a row from the archer table\n" +
		"[quit]   - Terminate this program\n";

	private Spike2Data() {
	}

	public static string Menu {
		get { return menu; }
	}
}

