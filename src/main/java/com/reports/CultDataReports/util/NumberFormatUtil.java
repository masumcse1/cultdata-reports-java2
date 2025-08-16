package com.reports.CultDataReports.util;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatUtil {

    /**
     * Formats a double value into European-style number formatting.
     * Example: 2070.38 → "2.070,38"
     */
    public static String formatAsEuropeanNumber(double number) {
        NumberFormat formatter = NumberFormat.getInstance(Locale.GERMANY);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(number);
    }

}
