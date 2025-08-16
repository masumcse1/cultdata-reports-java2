package com.reports.CultDataReports.controller;


import com.reports.CultDataReports.exception.ReportException;
import com.reports.CultDataReports.integraion.PropertyRestClient;
import com.reports.CultDataReports.integraion.TokenServiceRestClient;
import com.reports.CultDataReports.responsedto.AccessTokenSupplier;
import com.reports.CultDataReports.responsedto.CampaignMessageDto;
import com.reports.CultDataReports.responsedto.MessageFeedback;
import com.reports.CultDataReports.responsedto.PropertyDto;
import com.reports.CultDataReports.service.ICampaignMessageReportService;
import com.reports.CultDataReports.util.DateTimeFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/campaignmessage/api")
public class CampaignMessageReportWeb {

    private static final String MESSAGE_STATUS_OPEN = "Open";

    @Autowired
    private ICampaignMessageReportService iCampaignMessageReportService;

    @Autowired
    private TokenServiceRestClient tokenServiceRestClient;

    @Autowired
    private PropertyRestClient propertyRestClient;

    private static final Logger logger = LoggerFactory.getLogger(CampaignMessageReportWeb.class);

    @PostMapping("/campaignmessage-result")
    public ResponseEntity<CampaignMessageReportResponse> searchCampaignMessages(
            @RequestBody CampaignMessageReportRequest request) {

        logger.info("Campaign message search request --start");

        AccessTokenSupplier tokenFromSuppliers = tokenServiceRestClient.getTokenFromSuppliers();

        try {
            List<CampaignMessageDto> campaignMessages = iCampaignMessageReportService.getMessageSentData(tokenFromSuppliers.getAccessToken(),
                    request.propertyId(),
                    request.campaignId(),
                    request.campaignName(),
                    request.hasPermission(),
                    request.sentFromDate(),
                    request.sentToDate()
            );

            List<CampaignMessageDto> messages = prepareMessage(campaignMessages);

            CampaignReportSummary campaignReportSummary = getCampaignReportSummary(messages);

            logger.info("Campaign message search request --end");
            return ResponseEntity.ok(new CampaignMessageReportResponse(messages, campaignReportSummary));

        } catch (Exception e) {
            logger.error("Error during campaign message search", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CampaignMessageReportResponse(Collections.emptyList(), new CampaignReportSummary(0, 0, 0, 0.0, 0.0)));
        }
    }

    private static CampaignReportSummary getCampaignReportSummary(List<CampaignMessageDto> messages) {
        long totalSent = messages.size();
        long totalOpened = messages.stream().filter(m -> MESSAGE_STATUS_OPEN.equalsIgnoreCase(m.getStatus()))
                .count();
        long totalPermissions = messages.stream().filter(CampaignMessageDto::isPermission).count();

        double openRate = totalSent == 0 ? 0.0 : (double) totalOpened / totalSent * 100;
        double permissionRate = totalSent == 0 ? 0.0 : (double) totalPermissions / totalSent * 100;

        CampaignReportSummary summary = new CampaignReportSummary(
                totalSent,
                totalOpened,
                totalPermissions,
                openRate,
                permissionRate
        );
        return summary;
    }

    private List<CampaignMessageDto>  prepareMessage(List<CampaignMessageDto> messages) {

        List<Integer> propertyIds = messages.stream().map( m -> m.getPropertyId()).toList();
        List<PropertyDto> propertiesIds = new ArrayList<>();
        try{
             propertiesIds = propertyRestClient.getPropertiesByIds(propertyIds);
        }catch (ReportException exception){
            logger.error(" Campaign API no working properly "+exception.getMessage());
        }

        logger.info("propertiesByIds-----------"+propertiesIds.size());

        for (CampaignMessageDto msg : messages) {

            if (msg.getPropertyId() != null) {
                propertiesIds.stream()
                        .filter(p -> msg.getPropertyId().equals(p.getPropertyId()))
                        .findFirst()
                        .ifPresent(p -> msg.setPropertyName(p.getPropertyName()));
            }

            if (msg.getSentAt() != null) {
                msg.setSentAt(DateTimeFormatUtil.formatToOutput(msg.getSentAt()));
                msg.setPermissionTimestamp(DateTimeFormatUtil.formatToOutput(msg.getPermissionTimestamp()));
                msg.setDismissTopicTimestamp(DateTimeFormatUtil.formatToOutput(msg.getDismissTopicTimestamp()));
                msg.setUnsubscribeTimestamp(DateTimeFormatUtil.formatToOutput(msg.getUnsubscribeTimestamp()));
            }

            // Filter to keep only the latest feedback
            List<MessageFeedback> feedbacks = msg.getMessageFeedbacks();
            if (feedbacks != null && !feedbacks.isEmpty()) {
                Optional<MessageFeedback> latestFeedback = feedbacks.stream()
                        .filter(m -> m.getFollowUpDate() != null && !m.getFollowUpDate().isEmpty())
                        .max(Comparator.comparing(m -> m.getFollowUpDate().substring(0, 10))); // Compare only yyyy-MM-dd

                // Replace the list with just the latest feedback (or empty if none found)
                msg.setMessageFeedbacks(
                        latestFeedback.map(Collections::singletonList)
                                .orElse(Collections.emptyList())
                );
            }



        }




        return messages;
    }


    public record CampaignMessageReportRequest(
            Integer propertyId,
            Integer campaignId,
            String campaignName,
            String sentFromDate,
            String sentToDate,
            Boolean hasPermission
    ) {}

    public record CampaignMessageReportResponse(
            List<CampaignMessageDto> messages,
            CampaignReportSummary summary
    ) {}

    public record CampaignReportSummary(
            long totalSent,
            long totalOpened,
            long totalPermissions,
            double openRate,
            double permissionRate
    ) {}
}
