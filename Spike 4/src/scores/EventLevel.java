package scores;

public class EventLevel
{
    private char level;
    private String description;
    
    public EventLevel(char level, String desc)
    {
	this.level = level;
	this.description = desc;

    }

    public char getLevel()
    {
        return level;
    }

    public String getDescription()
    {
        return description;
    }
    
    public String getDisplayableString()
    {
	return description;
    }
}
