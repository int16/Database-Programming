package scores;

public class Award
{
    private Range range;
    private Round round;
    private String[] end;
    private AwardType awardType;
    private int endNo;
    
 


    public Award (Round r, Range ra)
    {
	round = r;
	range = ra;
    }
    
    public void setEnd(String[] end)
    {
	this.end = end;
    }
    
    public String[] getEnd()
    {
	return this.end;
    }
    
    public Range getRange()
    {
        return range;
    }

    public void setRange(Range range)
    {
        this.range = range;
    }

    public Round getRound()
    {
        return round;
    }

    public void setRound(Round round)
    {
        this.round = round;
    }

    public AwardType getAwardType()
    {
        return awardType;
    }

    public void setAwardType(AwardType awardType)
    {
        this.awardType = awardType;
    }
    
    public int getEndNo()
    {
        return endNo;
    }

    public void setEndNo(int endNo)
    {
        this.endNo = endNo;
    }

}
