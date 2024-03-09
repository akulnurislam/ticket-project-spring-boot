package com.akul.ticket.util;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    @NonNull
    public static String timestamp() {
        OffsetDateTime timestamp = OffsetDateTime.now(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        return timestamp.format(formatter);
    }

    @Nullable
    public static Time parseTime(String timeString) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        try {
            java.util.Date date = sdf.parse(timeString);
            return new Time(date.getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    @NonNull
    public static String formatTime(Time time) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(time);
    }
}
