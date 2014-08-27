using System;
using System.Data.SqlClient;
using MySql.Data.MySqlClient;
using System.Linq;

public class InputDataValidator
{
	public static string ReadString(string prompt)
	{
		Console.Write (prompt);
		return Console.ReadLine ();
	}

	public static int ReadIntegerRange(int min, int max)
	{
		int result = ReadInteger ("Enter a number between " + min + " and " + max + ": ");
		while (!(result >= min && result <= max))
		{
			Console.WriteLine ("The number you have provided is outside the specified range.");
			result = ReadInteger ("Enter a number between " + min + " and " + max + ": ");
		}
		return result;
	}

	public static int ReadInteger(string prompt)
	{
		string userInput = ReadString (prompt);
		int num;
		while (!int.TryParse(userInput, out num))
		{
			Console.WriteLine(userInput + " is not a valid Integer value.");
			userInput = ReadString (prompt);
		}
		return int.Parse (userInput);
	}

	public static int ReadYear(string prompt)
	{ 
		Console.Write (prompt);
		return ReadIntegerRange (1900, DateTime.Now.Year);
	}

	public static string ReadGender(string prompt)
	{
		string result = ReadString (prompt);
		while (!(result.ToUpper () == "M" || result.ToUpper () == "F"))
		{
			Console.WriteLine (result.ToUpper() + " is not a valid gender type.");
			result = ReadString (prompt);
		}
		return result.ToUpper ();
	}
}

