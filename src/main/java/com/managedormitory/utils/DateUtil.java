package com.managedormitory.utils;

import java.sql.Date;
import java.time.LocalDate;

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
}
