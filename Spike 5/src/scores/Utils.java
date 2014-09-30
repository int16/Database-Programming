package scores;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils
{

    public static final int DISC_TARGET=1;

    public String getCurrentDateFormatted()
    {
	DateFormat format = DateFormat.getDateInstance();	
	return format.format(new Date(System.currentTimeMillis()));		
    }

    public static int getCurrentYear()
    {
	SimpleDateFormat formatNowYear = new SimpleDateFormat("yyyy");

	java.util.Date nowDate = new java.util.Date();
	String currentYear = formatNowYear.format(nowDate); // = '2006'
	return Integer.parseInt(currentYear);
    }
    
    public static int getAge(int birthyear)
    {
	SimpleDateFormat formatNowYear = new SimpleDateFormat("yyyy");

	java.util.Date nowDate = new java.util.Date();
	int currentYear = Integer.parseInt(formatNowYear.format(nowDate));
	return currentYear - birthyear;
    }

    public static Date getDefaultEndDate()
    {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
	String date = "2099/12/31";
	java.util.Date endDate = null;
	try {
	    endDate = formatter.parse(date);
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return endDate;
    }
}
