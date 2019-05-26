package eroad.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Alon Kodner
 */
public class DateUtils {
    private final static String INPUT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final static String OUTPUT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public final static DateTimeFormatter INPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern(INPUT_DATE_FORMAT);
    public final static DateTimeFormatter OUTPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern(OUTPUT_DATE_FORMAT);

    public static ZonedDateTime getUTCDate(String date, DateTimeFormatter formatter) {
        LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
        return ZonedDateTime.of(localDateTime, ZoneOffset.UTC);
    }

    public static ZonedDateTime getZonedDateFromUTCDate(ZonedDateTime utcDate, ZoneId timeZoneId) {
        return utcDate.withZoneSameInstant(timeZoneId);
    }
}