package eroad.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;

/**
 * @author Alon Kodner
 */
@RunWith(SpringRunner.class)
public class DateUtilsTest {
    @Test
    public void getUTCDateTest() {
        String dateString = "2013-07-10 02:52:49";
        ZonedDateTime expectedUTCDate = ZonedDateTime.of(LocalDateTime.parse(dateString, DateUtils.INPUT_DATE_FORMATTER), ZoneId.of("UTC"));
        assertEquals(0, expectedUTCDate.toLocalDateTime().compareTo(DateUtils.getUTCDate(dateString, DateUtils.INPUT_DATE_FORMATTER).toLocalDateTime()));
    }

    @Test
    public void getZonedDateFromUTCDateTest() {
        String dateString = "2013-07-10 02:52:49";
        ZonedDateTime expectedUTCDate = ZonedDateTime.of(LocalDateTime.parse(dateString, DateUtils.INPUT_DATE_FORMATTER), ZoneId.of("UTC"));
        ZonedDateTime zonedDateFromUTCDate = DateUtils.getZonedDateFromUTCDate(expectedUTCDate, ZoneId.of("Pacific/Auckland"));
        assertEquals("2013-07-10T14:52:49", zonedDateFromUTCDate.format(DateUtils.OUTPUT_DATE_FORMATTER));
    }
}