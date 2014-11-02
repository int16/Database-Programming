package scores;

import java.util.Date;

public class Rating
{
    private int rating;
    private Date fromDate;
    private Date untilDate;
    private Equipment equipment;
    private Discipline discipline;
    private String comment;
    


    public Rating (int r)
    {
	this.rating = r;
    }

    public Date getFromDate()
    {
        return fromDate;
    }

    public void setFromDate(Date fromDate)
    {
        this.fromDate = fromDate;
    }

    public Equipment getEquipment()
    {
        return equipment;
    }

    public void setEquipment(Equipment equipment)
    {
        this.equipment = equipment;
    }

    public Discipline getDiscipline()
    {
        return discipline;
    }

    public void setDiscipline(Discipline discipline)
    {
        this.discipline = discipline;
    }

    public int getRating()
    {
        return rating;
    }
    
    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }
    
    public Date getUntilDate()
    {
        return untilDate;
    }

    public void setUntilDate(Date untilDate)
    {
        this.untilDate = untilDate;
    }
}
