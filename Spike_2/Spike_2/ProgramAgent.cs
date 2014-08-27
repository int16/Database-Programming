using System;
using System.Data;
using System.Data.Common;
using System.Data.SqlClient;
using System.Data.SqlTypes;
using MySql.Data.MySqlClient;
using MySql.Data.Types;
using System.Collections.Generic;

public class ProgramAgent
{
	bool _isRunning;
	string _menuOption;
	DBHandler _theHandler;

	public ProgramAgent (DBHandler theHandler)
	{
		_isRunning = true;
		_theHandler = theHandler;
	}

	public bool IsRunning
	{
		get { return _isRunning; }
		set { _isRunning = value; }
	}

	public void RunMenuForDBHandler()
	{
		Console.WriteLine ("");
		Console.WriteLine ("Please specify one of the following options: ");
		Console.WriteLine ("[find]   - Select a row from the archer table ");
		Console.WriteLine ("[list]   - Display foreign key mapping in DataSet");
		Console.WriteLine ("[insert] - Insert DataSet table");
		Console.WriteLine ("[update] - Update a row in the archer table   ");
		Console.WriteLine ("[delete] - Delete a row from the archer table ");
		Console.WriteLine ("[quit]   - Terminate this program             ");
		_menuOption = InputDataValidator.ReadString ("--> ");
		ProcessMenuOptionForDBHandler ();
	}

	private void ProcessMenuOptionForDBHandler()
	{
		switch (_menuOption.ToLower ()) {
		case "list":
			{
				Console.WriteLine ("");
				foreach (string rowSet in _theHandler.List()) {
					Console.WriteLine (rowSet);
				}
				break;
			}
		case "find":
			{
				Console.WriteLine ("");
				foreach (string rowSet in _theHandler.Select()) {
					Console.WriteLine (rowSet);
				}
				break;
			}
		case "insert":
			{
				Console.WriteLine ("");
				_theHandler.
				Insert ();
				break;
			}
		case "update":
			{
				Console.WriteLine ("");
				_theHandler.Update ();
				break;
			}
		case "delete":
			{
				Console.WriteLine ("");
				_theHandler.Delete ();
				break;
			}
		case "quit":
			{
				Console.WriteLine ("");
				_isRunning = false;
				break;
			}
		default:
			{
				Console.WriteLine ("");
				Console.WriteLine ("You have not specified a valid option.");
				break;
			}
		}
	}
}

