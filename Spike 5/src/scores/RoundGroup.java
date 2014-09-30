package scores;

public class RoundGroup implements Comparable
{
    private int level;
    private int group;
    private Round round = null;
    
    
    
    
    @Override
    public int compareTo(Object other)
    {
	int otherlevel = ((RoundGroup)other).level;
	if(this.level > otherlevel)
	    return 1;
	if(this.level > otherlevel)
	    return -1;
	return 0;
    }


    public int getLevel()
    {
        return level;
    }


    public void setLevel(int level)
    {
        this.level = level;
    }


    public int getGroup()
    {
        return group;
    }




    public void setGroup(int group)
    {
        this.group = group;
    }




    public Round getRound()
    {
        return round;
    }




    public void setRound(Round round)
    {
        this.round = round;
    } 
}
