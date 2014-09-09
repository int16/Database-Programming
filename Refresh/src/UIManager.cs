using System;

public class UIManager {

	public UIManager() {
	}

	public void Show(string text) {
		Console.WriteLine(text);
	}

	public void NextLine() {
		Console.WriteLine();
	}

	public string ReadString() {
		return ReadString(null);
	}

	public string ReadString(string prompt) {
		if (prompt != null) Console.Write(prompt);
		string result = Console.ReadLine().Trim();
		while (result == "") {
			if (prompt != null) Console.Write(prompt);
			result = Console.ReadLine().Trim();
		}

		return result;
	}

	public string ReadString(Func<string, bool> validator, string error) {
		string result = ReadString();
		while (!validator(result)) {
			Console.WriteLine(error);
			result = ReadString();
		}
		return result;
	}

	public string ReadString(string prompt, Func<string, bool> validator, string error) {
		string result = ReadString(prompt);
		while (!validator(result)) {
			Console.WriteLine(error);
			result = ReadString(prompt);
		}
		return result;
	}

	public int ReadInt() {
		string input = ReadString();
		int result;
		while (!int.TryParse(input, out result)) {
			Console.WriteLine("'" + input + "' is not a valid integer.");
			input = ReadString();
		}
		return result;
	}

	public int ReadInt(string prompt) {
		string input = ReadString(prompt);
		int result;
		while (!int.TryParse(input, out result)) {
			Console.WriteLine("'" + input + "' is not a valid integer.");
			input = ReadString(prompt);
		}
		return result;
	}

	public int ReadInt(Func<int, bool> validator, string error) {
		string input = ReadString();
		int result;
		bool p = true, v = true;
		while (!(p = int.TryParse(input, out result)) || !(v = validator(result))) {
			if (!p) Console.WriteLine("'" + input + "' is not a valid integer.");
			else if (!v) Console.WriteLine(error);
			input = ReadString();
		}
		return result;
	}

	public int ReadInt(string prompt, Func<int, bool> validator, string error) {
		string input = ReadString(prompt);
		int result;
		bool p = true, v = true;
		while (!(p = int.TryParse(input, out result)) || !(v = validator(result))) {
			if (!p) Console.WriteLine("'" + input + "' is not a valid integer.");
			else if (!v) Console.WriteLine(error);
			input = ReadString(prompt);
		}
		return result;
	}

}
