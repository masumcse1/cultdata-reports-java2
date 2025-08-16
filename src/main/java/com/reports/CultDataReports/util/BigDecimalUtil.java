package com.reports.CultDataReports.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtil {

    public static double formatOneDigitDecimal(double value) {
        int decimalPlace = 1; // one digit decimal
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String formatTwoDigitDecimal(double value) {
        int decimalPlace = 2; // Two digit decimal
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        return String.format("%.2f", bd.doubleValue());  // if 1.2 -> 1.20
    }

}
