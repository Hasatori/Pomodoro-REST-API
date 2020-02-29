package com.pomodoro.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {




    public static Date getCurrentDateUtc() {
        return getUTCCalendarInstance().getTime();

    }

    private static Calendar getUTCCalendarInstance(){
   return     Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    }
}
