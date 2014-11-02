package scores;

public class Equipment 
{
	private int id;
	private String name;
	private String equipDesc;
	
	
	public int getId()
	{
	    return id;
	}
	public void setId(int equipId)
	{
	    this.id = equipId;
	}
	public String getName()
	{
	    return name;
	}
	public void setName(String equipName)
	{
	    this.name = equipName;
	}
	public String getEquipDesc()
	{
	    return equipDesc;
	}
	public void setEquipDesc(String equipDesc)
	{
	    this.equipDesc = equipDesc;
	}
	
	
	public String toString()
	{
	    return this.equipDesc;
	}
	
	
}
