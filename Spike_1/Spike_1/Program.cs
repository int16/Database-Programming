using System;
using System.Data.Common;
using System.Data.SqlClient;
using MySql.Data.MySqlClient;

namespace Spike_1
{
	class MainClass
	{
		public static void Main (string[] args)
		{
			ProgramAgent aAgent = new ProgramAgent (new DBHandler());
			while (aAgent.IsRunning) {
				aAgent.RunMenuForDBHandler ();
			}
		}
	}
}
