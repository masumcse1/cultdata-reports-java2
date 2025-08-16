package com.reports.CultDataReports.util;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.time.ZonedDateTime;


public class DateTimeFormatUtil {

    private static final DateTimeFormatter OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);

    /*
        input  : 2025-06-27T07:49:59.162+00:00
        output : 27 Jun 2025 07:49:59 GMT
     */
    public static String formatToOutput(String dateTimeVaue) {
        if (dateTimeVaue == null || dateTimeVaue.isEmpty()) return dateTimeVaue;

        try {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeVaue);
            return OUTPUT_FORMATTER.format(zonedDateTime);
        } catch (Exception e) {
            // Log warning here if needed
            return dateTimeVaue;
        }
    }
}