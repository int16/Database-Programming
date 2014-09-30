package exceptions;

public class TooManyEntriesException extends Exception
{
    public TooManyEntriesException(String msg)
    {
	super(msg);
    }
}
