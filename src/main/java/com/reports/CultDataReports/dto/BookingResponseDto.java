package com.reports.CultDataReports.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookingResponseDto {

    private List<BookingDto> data;
    private Integer totalRecords;

}
