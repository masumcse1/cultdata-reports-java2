package com.reports.CultDataReports.responsedto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageContentSupplier {
    private List<CampaignMessageDto> content;
    private List<CampaignMessageDto> result;
    private int totalPages;
    private int totalElements;
    private boolean last;
    private int number;
    private int size;
    private int numberOfElements;
    private boolean first;
    private boolean empty;
}