package com.pomodoro.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {


    private static final Calendar CALENDAR = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    public static Date getCurrentDateUtc() {
        return CALENDAR.getTime();

    }

}
