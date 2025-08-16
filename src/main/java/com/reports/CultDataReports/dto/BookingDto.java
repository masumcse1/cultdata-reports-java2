package com.reports.CultDataReports.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Integer id;
    private String channelBookingId;
    private Client6Dto client;
    private Channel3 channel;
    private LocalDateTime bookingDate;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private Integer numberOfRooms;
    private Integer numberOfNightsLos;
    private Integer numberOfRoomNights;
    private boolean isCancelled;
    private LocalDateTime cancellationDate;
    private Number gbv;
    private String currency;

    // getters and setters
}