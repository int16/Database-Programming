package scores;

import java.util.Date;


public class Event
{
    private int id;
    private String name;
    private Date eventTime;
    private Round round;
    private String venue;
    private MultiEvent multiEvent = null;

    
    public int getId()
    {
        return id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public Date getEventTime()
    {
        return eventTime;
    }
    public void setEventTime(Date eventTime)
    {
        this.eventTime = eventTime;
    }
    public Round getRound()
    {
        return round;
    }
    public void setRound(Round round)
    {
        this.round = round;
    }
    public String getVenue()
    {
        return venue;
    }
    public void setVenue(String venue)
    {
        this.venue = venue;
    }
    public MultiEvent getMultiEvent()
    {
        return multiEvent;
    }
    public void setMultiEvent(MultiEvent multiEvent)
    {
        this.multiEvent = multiEvent;
    }
    public void setId(int i)
    {
	id = i;
	
    }

    public String toString()
    {
	return this.name;
    }
    
    
    
}
