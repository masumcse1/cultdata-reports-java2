package com.reports.CultDataReports.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.List;

@Data
public class OnlineDistributionPerformanceSearchDTO {
    @Min(value = 0, message = "Client ID must be a  number")
    private Integer client;
    private List<String> distributionManagers;

}

