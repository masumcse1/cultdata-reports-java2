package com.reports.CultDataReports.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("MMM-yyyy", Locale.ENGLISH);

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    public static String formatWithPattern(LocalDate date, String datePattern) {
        if (datePattern == null) {
            return date.format(dateFormatter);
        }else {
            return date.format( DateTimeFormatter.ofPattern(datePattern));
        }

    }

    // Format LocalDate to String
    public static String format(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        return date.format(dateFormatter);
    }

    // Parse String to LocalDate
    public static LocalDate parse(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            throw new IllegalArgumentException("Date string cannot be null or empty");
        }
        try {
            return LocalDate.parse(dateString, dateFormatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date string: " + dateString, e);
        }
    }

    // Format LocalDateTime to String
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("DateTime cannot be null");
        }
        return dateTime.format(dateTimeFormatter);
    }

    // Format LocalDateTime to String
    public static String formatDateTime(LocalDateTime dateTime,String dateTimeFormatter) {
        if (dateTime == null) {
            throw new IllegalArgumentException("DateTime cannot be null");
        }
        DateTimeFormatter dateTimefmt= DateTimeFormatter.ofPattern(dateTimeFormatter);
        return dateTime.format(dateTimefmt);
    }

    // Parse String to LocalDateTime
    public static LocalDateTime parseDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            throw new IllegalArgumentException("DateTime string cannot be null or empty");
        }
        try {
            return LocalDateTime.parse(dateTimeString, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid datetime string: " + dateTimeString, e);
        }
    }


    public static String monthFormat(String yyyyMmDate) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM").parse(yyyyMmDate);
        return MONTH_FORMAT.format(date);
    }
}
