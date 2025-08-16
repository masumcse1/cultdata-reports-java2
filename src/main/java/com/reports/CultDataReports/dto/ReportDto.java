package com.reports.CultDataReports.dto;


import lombok.Data;

@Data
public class ReportDto {
    private Integer id;
    private Client6Dto client;
    private String month;
    private String pdf;
    private Integer dmId;
    private String dmName;


    public ReportDto() {
    }

}