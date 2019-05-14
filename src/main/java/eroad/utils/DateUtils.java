package eroad.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Alon Kodner
 */
public class DateUtils {
    private final static String INPUT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final static String OUTPUT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private final static String UTC = "UTC";

    /**
     * @param date The date to process
     * @return The matching UTC date in seconds
     */
    public static Long getUTCSeconds(String date) {
        DateFormat dateFormatter = new SimpleDateFormat(INPUT_DATE_FORMAT);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(UTC));

        try {
            Date formattedDate = dateFormatter.parse(date);
            if (formattedDate != null) {
                return formattedDate.getTime() / 1000;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param timeStamp  timestamp in seconds
     * @param timeZoneId the requested timezone
     * @return the local date as string
     */
    public static String getLocalDate(long timeStamp, String timeZoneId) {
        DateFormat dateFormatter = new SimpleDateFormat(OUTPUT_DATE_FORMAT);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(timeZoneId));

        long millisecondTimeStamp = (timeStamp) * 1000; //Converting back to milliseconds
        Date localDate = new Date(millisecondTimeStamp);

        return dateFormatter.format(localDate);
    }
}