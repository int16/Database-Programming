package scores;

public class Range
{
    private int id;
    private int ends;
    private int arrowsPerEnd;
    private int maxArrowScore;
    private Face face;
    private int distance;
    
    
    
    public int getEnds()
    {
        return ends;
    }
    public void setEnds(int ends)
    {
        this.ends = ends;
    }
    public int getArrowsPerEnd()
    {
        return arrowsPerEnd;
    }
    public void setArrowsPerEnd(int arrowsPerEnd)
    {
        this.arrowsPerEnd = arrowsPerEnd;
    }
    public int getMaxArrowScore()
    {
        return maxArrowScore;
    }
    public void setMaxArrowScore(int maxArrowScore)
    {
        this.maxArrowScore = maxArrowScore;
    }
    public Face getFace()
    {
        return face;
    }
    public void setFace(Face face)
    {
        this.face = face;
    }
    public int getDistance()
    {
        return distance;
    }
    public void setDistance(int distance)
    {
        this.distance = distance;
    }
    
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
}
