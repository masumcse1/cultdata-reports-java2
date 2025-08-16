package com.reports.CultDataReports.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignMessageDto {
    private Integer id;
    private Integer propertyId;
    private String propertyName;
    private String name;
    private String topic;
    private String subject;
    private String toAddress;
    private String toName;
    private String fromAddress;
    private String fromName;
    private String replyAddress;
    private String replyName;
    private String domain;
    private String provider;
    private String body;
    private String scheduledFor;
    private boolean permission;
    private boolean dismissTopic;
    private boolean unsubscribe;
    private String details;
    private String ewsId;
    private String dismissTopicIPv4;
    private String permissionIPv4;
    private String unsubscribeIPv4;
    private String permissionTimestamp;
    private String dismissTopicTimestamp;
    private String unsubscribeTimestamp;
    private String status;
    private String sentAt;
    private Integer campaignId;
    private List<MessageFeedback> messageFeedbacks;

}