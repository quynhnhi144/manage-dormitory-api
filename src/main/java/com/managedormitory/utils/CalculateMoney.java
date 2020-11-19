package com.managedormitory.utils;

import com.managedormitory.models.dto.powerbill.PowerBillDetail;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class CalculateMoney {

    private CalculateMoney() {
    }

    public static float calculatePowerBill(PowerBillDetail powerBillDetail) {
        System.out.println("cal:" + powerBillDetail);
        float pricePowerAKWH = powerBillDetail.getPriceList().getPrice();
        long numberOfPowerUsed = powerBillDetail.getNumberOfPowerEnd() - powerBillDetail.getNumberOfPowerBegin();
        float money = 0;
        if (numberOfPowerUsed <= LimitedPower.LOW_POWER) {
            money = pricePowerAKWH * numberOfPowerUsed;
        } else if (LimitedPower.LOW_POWER < numberOfPowerUsed && numberOfPowerUsed <= LimitedPower.MIDDLE_POWER) {
            money = pricePowerAKWH * numberOfPowerUsed * LimitedPower.MIDDLE_POWER_FINES;
        } else if (LimitedPower.MIDDLE_POWER < numberOfPowerUsed && numberOfPowerUsed <= LimitedPower.HIGH_POWER) {
            money = pricePowerAKWH * numberOfPowerUsed * LimitedPower.HIGH_POWER_FINES;
        } else if (numberOfPowerUsed > LimitedPower.HIGH_POWER) {
            money = pricePowerAKWH * numberOfPowerUsed * LimitedPower.HIGHER_POWER_FINES;
        }
        return money;
    }

    public static float calculatePowerBill(PowerBillDetail powerBillDetail, long numberOfPowerBegin, long numberOfPowerEnd) {
        float pricePowerAKWH = powerBillDetail.getPriceList().getPrice();
        long numberOfPowerUsed = numberOfPowerEnd - numberOfPowerBegin;
        float money = 0;
        if (numberOfPowerUsed <= LimitedPower.LOW_POWER) {
            money = pricePowerAKWH * numberOfPowerUsed;
        } else if (LimitedPower.LOW_POWER < numberOfPowerUsed && numberOfPowerUsed <= LimitedPower.MIDDLE_POWER) {
            money = pricePowerAKWH * numberOfPowerUsed * LimitedPower.MIDDLE_POWER_FINES;
        } else if (LimitedPower.MIDDLE_POWER < numberOfPowerUsed && numberOfPowerUsed <= LimitedPower.HIGH_POWER) {
            money = pricePowerAKWH * numberOfPowerUsed * LimitedPower.HIGH_POWER_FINES;
        } else if (numberOfPowerUsed > LimitedPower.HIGH_POWER) {
            money = pricePowerAKWH * numberOfPowerUsed * LimitedPower.HIGHER_POWER_FINES;
        }
        return money;
    }

    public static float calculateRemainingMoney(LocalDate currentDate, LocalDate endDate, float price) {
        Date dCurrentDate = DateUtil.getDateFromLDate(currentDate);
        Date dEndDate = DateUtil.getDateFromLDate(endDate);

        Calendar cCurrentDate = Calendar.getInstance();
        cCurrentDate.setTime(dCurrentDate);

        Calendar cEndDate = Calendar.getInstance();
        cEndDate.setTime(dEndDate);

        long daysDuration = ChronoUnit.DAYS.between(cEndDate.toInstant(), cCurrentDate.toInstant());

        return price / LimitedPower.NUMBER_OF_DATE * daysDuration;
    }
}
