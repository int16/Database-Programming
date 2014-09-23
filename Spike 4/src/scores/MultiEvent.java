package scores;

import java.util.Date;

public class MultiEvent
{
    private String name;
    private Date start;
    private Date end;
    private MultieventRule[] rules;
    private int ruleCounter = 0;
    private int id;
    
    
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public MultiEvent(int noRules)
    {
	rules = new MultieventRule[noRules];
    }
    
    public MultiEvent()
    {
	
    }
    
    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }

    public Date getStart()
    {
        return start;
    }


    public void setStart(Date start)
    {
        this.start = start;
    }


    public Date getEnd()
    {
        return end;
    }


    public void setEnd(Date end)
    {
        this.end = end;
    }


    public int getRules()
    {
        return ruleCounter;
    }
    
    public MultieventRule getRule(int idx)
    {
	return rules[idx];
    }


    public void addRule (int roundId, String calcRules, int noEvents)
    {
	rules[ruleCounter++] = new MultieventRule(roundId, calcRules, noEvents);
    }
    
    public String toString()
    {
	return name;
    }

 
    public class MultieventRule
    {
	private int roundId;
	private String calcRules;
	private int noEvents;
	
	MultieventRule(int roundId, String calcRules, int noEvents)
	{
	    this.roundId = roundId;
	    this.calcRules = calcRules;
	    this.noEvents = noEvents;
	}
	
	
	public int getRoundId()
	{
	    return roundId;
	}
	
	public String getCalcRules()
	{
	    return calcRules;
	}
	
	public int getNoEvents()
	{
	    return noEvents;
	}
	
    }
    
    
}
