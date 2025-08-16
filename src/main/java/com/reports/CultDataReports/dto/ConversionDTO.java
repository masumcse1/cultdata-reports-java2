package com.reports.CultDataReports.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConversionDTO {
    private Integer id;
    private Client6Dto client;
    private Channel3 channel;
    private LocalDateTime date;
    private Integer noOfAvailabilitySearch;
    private Integer noOfUniqueVisitors;
    private double responseTime;


    //Calculate field

    private String period;
    private Integer numberOfBookings;
    private String uniqueVisitorRate;
    private String conversionRate;

    public ConversionDTO() {
    }
}