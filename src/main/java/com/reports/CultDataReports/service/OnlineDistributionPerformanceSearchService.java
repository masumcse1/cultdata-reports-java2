package com.reports.CultDataReports.service;


import com.reports.CultDataReports.dto.Client6Dto;
import com.reports.CultDataReports.dto.DistributionManagerDTO;
import com.reports.CultDataReports.dto.OnlineDistributionPerformanceSearchRequest;
import com.reports.CultDataReports.dto.ReportDto;
import com.reports.CultDataReports.exception.ReportException;
import com.reports.CultDataReports.integraion.CultDataRestClient;
import com.reports.CultDataReports.integraion.CultDataRestClientForOdpr;
import com.reports.CultDataReports.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OnlineDistributionPerformanceSearchService implements IOnlineDistributionPerformanceSearchService {

    private static final Logger logger = LoggerFactory.getLogger(OnlineDistributionPerformanceSearchService.class);

    private final CultDataRestClient cultDataClient;

    private final CultDataRestClientForOdpr cultDataRestClientForOdpr;
    @Override
    public List<ReportDto> getLatestReportsByDmID(OnlineDistributionPerformanceSearchRequest dto) {

        List<ReportDto> reportResultDtos = new ArrayList<>();
        try {

            for (Integer dmID : dto.getDistributionManagers()) {
                DistributionManagerDTO distributionManager = cultDataRestClientForOdpr.getDistributionManagersById(Integer.valueOf(dmID));
                List<ReportDto> latestOdprReports = cultDataRestClientForOdpr.getLatestOdprReportsByDmID(Integer.valueOf(dmID));
                latestOdprReports.forEach(report -> {
                    report.setDmId(distributionManager.getId());
                    report.setDmName(distributionManager.getName());
                    try {
                        report.setMonth(DateUtil.monthFormat(report.getMonth()));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                });
                reportResultDtos.addAll(latestOdprReports);
            }
        }catch (ReportException e){

        }

        return reportResultDtos;
    }


    @Override
    public List<ReportDto> getLatestReportsByClientID(OnlineDistributionPerformanceSearchRequest dto) {
        List<ReportDto> latestOdprReports = new ArrayList<>();
        try{
            Client6Dto client = cultDataRestClientForOdpr.getClientById(dto.getClient());
            DistributionManagerDTO distributionManager = cultDataRestClientForOdpr.getDistributionManagersById(client.getDistributionManagerIdCultSwitch());
            latestOdprReports = cultDataRestClientForOdpr.getLatestOdprReportsByClientID(dto.getClient());
            latestOdprReports.forEach(report -> {
                report.setDmId(distributionManager.getId());
                report.setDmName(distributionManager.getName());
                try {
                    report.setMonth(DateUtil.monthFormat(report.getMonth()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            });

        }catch (ReportException e){

        }
        return latestOdprReports;
    }




    public List<DistributionManagerDTO> fetchDistributionManagers(Boolean onlyMapped) {
        List<DistributionManagerDTO> managers = cultDataClient.getCacheDistributionManagers(onlyMapped);
        logger.info("fetchDistributionManagers retrieved: count = {}", managers.size());
        return managers;
    }


}