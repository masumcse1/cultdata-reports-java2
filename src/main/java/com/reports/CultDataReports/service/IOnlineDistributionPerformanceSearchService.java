package com.reports.CultDataReports.service;


import com.reports.CultDataReports.dto.*;

import java.util.List;

public interface IOnlineDistributionPerformanceSearchService {

    public List<ReportDto> getLatestReportsByDmID(OnlineDistributionPerformanceSearchRequest dto);
    public List<ReportDto> getLatestReportsByClientID(OnlineDistributionPerformanceSearchRequest dto);
    List<DistributionManagerDTO> fetchDistributionManagers(Boolean onlyMapped);

}