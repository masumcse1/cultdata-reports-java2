package com.reports.CultDataReports.service;

import com.reports.CultDataReports.dto.ConversionDTO;
import com.reports.CultDataReports.dto.ConversionRateSearchDTO;
import com.reports.CultDataReports.dto.DistributionManagerDTO;

import java.util.List;

public interface IConversionRateService {

    List<ConversionDTO> getConversionRatesByClient(ConversionRateSearchDTO conversionModel);

    public List<ConversionDTO> getConversionForAllClients(ConversionRateSearchDTO conversionModel);

    public List<DistributionManagerDTO> fetchDistributionManagers(Boolean onlyMapped);

    Integer getTotalBookingValueByDm(ConversionRateSearchDTO conversionModel);

    Integer getTotalBookingValueByClient(ConversionRateSearchDTO conversionModel);

}