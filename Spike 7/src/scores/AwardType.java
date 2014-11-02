package scores;

public class AwardType
{
    private int id;
    private String description;
    private String issuer;
    private String comment;
   
    
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public String getIssuer()
    {
        return issuer;
    }
    public void setIssuer(String issuer)
    {
        this.issuer = issuer;
    }
    public String getComment()
    {
        return comment;
    }
    public void setComment(String comment)
    {
        this.comment = comment;
    }
    
}
