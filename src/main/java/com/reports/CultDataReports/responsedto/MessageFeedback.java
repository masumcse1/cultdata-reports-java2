package com.reports.CultDataReports.responsedto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageFeedback {
    private Integer id;
    private Integer messageId;
    private Integer userId;
    private String comment;
    private String status;
    private String followUpDate;
    private String nextFollowUpDate;
    private String lastUpdate;
}