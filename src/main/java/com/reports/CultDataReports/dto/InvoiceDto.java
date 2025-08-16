package com.reports.CultDataReports.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class InvoiceDto {
    private Integer id;
    private String invoiceName;
    private Client6Dto client;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime generatedDate;
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private Double totalNet;
    private String currency;
    private Double amountInEur;
    private String convertedCurrency;

}