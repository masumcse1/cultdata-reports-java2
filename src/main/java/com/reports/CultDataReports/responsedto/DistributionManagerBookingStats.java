package com.reports.CultDataReports.responsedto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DistributionManagerBookingStats {

    private String channelId;
    private String startDate;
    private String endDate;
    private Integer numberOfBookings;
    private Integer numberOfTransactions;
    private Integer numberOfCancellations;
}
