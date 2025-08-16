package com.reports.CultDataReports.dto;


import lombok.Data;
import java.util.List;

@Data
public class InvoiceSearchDTO {
    private List<String> distributionManagers;
    private String startGeneratedDate;
    private String endGeneratedDate;
}