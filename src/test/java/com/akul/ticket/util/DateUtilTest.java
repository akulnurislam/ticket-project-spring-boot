package com.akul.ticket.util;

import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

public class DateUtilTest {

    @Test
    public void testTimestamp() {
        var timestamp = DateUtil.timestamp();
        assertNotNull(timestamp);
        assertTrue(timestamp.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}Z"));
    }

    @Test
    public void testParseTime() {
        var timeString = "12:00 PM";
        var time = DateUtil.parseTime(timeString);
        assertNotNull(time);

        timeString = "invalid time";
        time = DateUtil.parseTime(timeString);
        assertNull(time);
    }

    @Test
    public void testFormatTime() throws Exception {
        var sdf = new SimpleDateFormat("hh:mm a");
        var date = sdf.parse("12:00 PM");
        var time = new Time(date.getTime());

        var formattedTime = DateUtil.formatTime(time);
        assertEquals("12:00 PM", formattedTime);
    }
}
