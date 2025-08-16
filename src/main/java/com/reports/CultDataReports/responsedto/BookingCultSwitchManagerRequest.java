package com.reports.CultDataReports.responsedto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingCultSwitchManagerRequest {


    private String fromDate;

    private String toDate;

    private String channel;

    private List<Integer> cultSwitchDistributionManagerIds;

}