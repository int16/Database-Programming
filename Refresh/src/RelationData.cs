using System;

public class RelationData {

	private string name, ptable, pcol, ctable, ccol;

	public RelationData(string name, string ptable, string pcol, string ctable, string ccol) {
		this.name = name;
		this.ptable = ptable;
		this.pcol = pcol;
		this.ctable = ctable;
		this.ccol = ccol;
	}

	public string Name {
		get { return name; }
	}

	public string ParentTable {
		get { return ptable; }
	}

	public string ParentColumn {
		get { return pcol; }
	}

	public string ChildTable {
		get { return ctable; }
	}

	public string ChildColumn {
		get { return ccol; }
	}
}

