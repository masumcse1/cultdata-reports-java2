package com.reports.CultDataReports.service;

import com.reports.CultDataReports.exception.ReportException;
import com.reports.CultDataReports.responsedto.CampaignMessageDto;

import java.time.LocalDate;
import java.util.List;

public interface ICampaignMessageReportService {

    public List<CampaignMessageDto> getMessageSentData(String token, Integer propertyId, Integer campaignId, String campaignName,Boolean hasPermission,  String sentFromDate, String sentTomDate) throws ReportException;

}
