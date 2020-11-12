package com.managedormitory.utils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    //convert SQLDate -> LocalDate
    public static java.time.LocalDate getLDateFromSDate(final java.sql.Date sDate) {
        if (sDate == null) {
            return null;
        }
        return sDate.toLocalDate();
    }

    //convert LocalDate -> SQLDate
    public static Date getSDateFromLDate(LocalDate ldate) {
        if (ldate == null) {
            return null;
        }
        return java.sql.Date.valueOf(ldate);
    }

    //convert String -> LocalDate
    public static LocalDate getLDateFromString(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);
        return localDateTime.toLocalDate();
    }
}
