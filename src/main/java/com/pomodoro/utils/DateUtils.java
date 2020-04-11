package com.pomodoro.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {




    public static Date getCurrentDateUtc() {
        return getUTCCalendarInstance().getTime();

    }
    public static LocalDateTime getCurrentLocalDateTimeUtc() {
        return LocalDateTime.now(ZoneOffset.UTC);

    }
    private static Calendar getUTCCalendarInstance(){
   return     Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    }
}
