package com.managedormitory.utils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    //convert SQLDate -> LocalDate
    public static java.time.LocalDate getLDateFromSDate(final Date sDate) {
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
        return LocalDate.parse(dateString, formatter);
    }

    //convert java.util.Date -> java.time.LocalDate
    public static LocalDate getLDateFromUtilDate(java.util.Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    // convert LocalDate -> Date
    public static java.util.Date getDateFromLDate(LocalDate localDate){
        return java.util.Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
