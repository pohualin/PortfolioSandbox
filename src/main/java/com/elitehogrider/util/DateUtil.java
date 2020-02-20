package com.elitehogrider.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Calendar midnight() {
        LocalDate now = LocalDate.now();
        return localDateToCalendar(now);
    }

    public static Calendar parseDateString(String dateString) {
        LocalDate localDate = LocalDate.parse(dateString);
        return localDateToCalendar(localDate);
    }

    public static Calendar localDateToCalendar(LocalDate localDate) {
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}
