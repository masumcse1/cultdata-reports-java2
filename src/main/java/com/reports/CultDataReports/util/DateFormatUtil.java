package com.reports.CultDataReports.util;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateFormatUtil {

    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String formatDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        return LocalDate.parse(dateStr, INPUT_FORMATTER).format(OUTPUT_FORMATTER);
    }

    public static LocalDate parseInputDate(String dateStr) {
        return LocalDate.parse(dateStr, INPUT_FORMATTER);
    }

    public static String formatToOutput(LocalDate date) {
        return date.format(OUTPUT_FORMATTER);
    }
}
