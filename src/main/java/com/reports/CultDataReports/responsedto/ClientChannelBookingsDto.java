package com.reports.CultDataReports.responsedto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientChannelBookingsDto {

    private String clientId;
    private String channelId;
    private String startDate;
    private String endDate;
    private BigDecimal sumGbv;
    private BigDecimal cancellationValue;
    private BigDecimal sumNbv;
    private String currency;
    private Integer numberOfBookings;
    private Integer numberOfTransactions;
    private Integer numberOfCancellations;

}