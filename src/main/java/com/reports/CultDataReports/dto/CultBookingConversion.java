package com.reports.CultDataReports.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CultBookingConversion {

    private Long id;

    private Integer clientId;

    private Integer channelId;

    private LocalDate date;

    private Integer searches;

    private Float responseTime;

    private Integer uniqueVisitors;

    private Integer bookings;

    private Float searchUniqueVisitorRate;

    private Float conversion;

}