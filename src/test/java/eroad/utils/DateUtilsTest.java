package eroad.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Alon Kodner
 */
@RunWith(SpringRunner.class)
public class DateUtilsTest {
    @Test
    public void getUTCSecondsTest() {
        Long utcSeconds = DateUtils.getUTCSeconds("2013-07-10 02:52:49");
        assertNotNull(utcSeconds);
        assertEquals(1373424769L, (long) utcSeconds);
        utcSeconds = DateUtils.getUTCSeconds("2019-03-03 23:18:55");
        assertNotNull(utcSeconds);
        assertEquals(1551655135, (long) utcSeconds);
    }

    @Test
    public void LocalDateTest() {
        assertEquals("2013-07-10T15:09:29", DateUtils.getLocalDate(1373425769L, "Pacific/Auckland"));
        assertEquals("2019-03-02T23:25:49", DateUtils.getLocalDate(1551565549L, "Europe/Berlin"));
    }
}