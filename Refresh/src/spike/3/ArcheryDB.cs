using System;
using System.Linq;
using System.Data.Linq;
using System.Data.Linq.Mapping;

public class ArcheryDB : DataContext {

	public ArcheryDB() : base("SERVER=localhost;DATABASE=archery_db;USER ID=root;PWD=021190;allow zero datetime=true;") {
	}

}

