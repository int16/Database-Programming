package scores;

public class Round
{
    private int id;
    private String name;
    private String previousName;
    private Discipline discipline;
    private boolean official;
    private int maxDistance;
    private int rangeCount;
    private Range[] ranges = null;
    private int fieldTargetCount;
   
    
    public Round(){}
    
    public Round (int id, String name)
    {
	this.id = id;
	this.name = name;
    }
    
    public Range getRange(int idx)
    {
	return ranges[idx];
    }
    
    public void addRange(int rangeNumber, Range r)
    {
	ranges[rangeNumber] = r;
    }
    
    public String getPreviousName()
    {
        return previousName;
    }

    public void setPreviousName(String previousName)
    {
        this.previousName = previousName;
    }

    public Discipline getDiscipline()
    {
        return discipline;
    }

    public void setDiscipline(Discipline discipline)
    {
        this.discipline = discipline;
    }

    public boolean isOfficial()
    {
        return official;
    }

    public void setOfficial(boolean official)
    {
        this.official = official;
    }

    public int getMaxDistance()
    {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance)
    {
        this.maxDistance = maxDistance;
    }

    public int getRangeCount()
    {
        return rangeCount;
    }

    public void setRangeCount(int ranges)
    {
        this.rangeCount = ranges;
        this.ranges = new Range[ranges];
    }

    public int getFieldTargetCount()
    {
        return fieldTargetCount;
    }

    public void setFieldTargetCount(int fieldTargetCount)
    {
        this.fieldTargetCount = fieldTargetCount;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }


    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
    
    public String toString()
    {
    return "Round"; 
	//return name;
    }
    
    
    public int getMaxPossibleScore()
    {
	int total = 0;
	for (int i = 0; i < rangeCount; i++)
	{
	    total += ranges[i].getEnds()* ranges[i].getArrowsPerEnd();
	}
	return total;
    }
}
