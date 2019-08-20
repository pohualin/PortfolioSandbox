package com.elitehogrider.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Calendar midnight() {
        LocalDateTime now = LocalDateTime.now();
        Instant startOfDay = now.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        Calendar midnight = Calendar.getInstance();
        midnight.setTime(Date.from(startOfDay));
        return midnight;
    }
}
