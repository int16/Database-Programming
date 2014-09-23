package scores;

public class End
{
    private Range range;
    private int total;
    private String [] arrowScores;
    
    public End (Range r)
    {
	this.range = r;
	this.arrowScores = new String[range.getArrowsPerEnd()];
    }
    
    
}
