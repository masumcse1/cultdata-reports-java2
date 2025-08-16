package com.reports.CultDataReports.service;

import com.reports.CultDataReports.exception.ReportException;
import com.reports.CultDataReports.integraion.SupplierMessageApiRestClient;
import com.reports.CultDataReports.responsedto.CampaignMessageDto;
import com.reports.CultDataReports.util.DateFormatUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CampaignMessageReportService implements  ICampaignMessageReportService {

    private static final Logger logger = LoggerFactory.getLogger(CampaignMessageReportService.class);

    private final SupplierMessageApiRestClient supplierMessageApiRestClient;

    public List<CampaignMessageDto> getMessageSentData(String token, Integer propertyId, Integer campaignId, String campaignName,Boolean hasPermission,  String sentFromDate, String sentTomDate) throws ReportException {

        String startDate = DateFormatUtil.formatDate(sentFromDate);
        String endDate = DateFormatUtil.formatDate(sentTomDate);

        List<CampaignMessageDto> campaignMessageList = supplierMessageApiRestClient.getMessageSentData(token, campaignId,campaignName,hasPermission, startDate, endDate);

        if(propertyId != null){
            List<CampaignMessageDto> campaignMessageListWithProperty = campaignMessageList.stream().filter(m -> m.getPropertyId().equals(propertyId)).toList();
            return campaignMessageListWithProperty;
        }

        return campaignMessageList;
    }



}
