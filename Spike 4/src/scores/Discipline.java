package scores;

public class Discipline
{
    private int id;
    private String name;
    private boolean handicapped;
    
    
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public boolean isHandicapped()
    {
        return handicapped;
    }
    public void setHandicapped(boolean handicapped)
    {
        this.handicapped = handicapped;
    }
    
    public String toString()
    {
	return this.name;
    }
}
