package com.reports.CultDataReports.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.List;


@Data
public class ConversionRateSearchDTO {

    private String clientId;

    private List<String> distributionManagers;

    private String channelId;

    private String fromDate;

    private String toDate;

    private boolean excludeTestProperties;
}
