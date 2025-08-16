package com.reports.CultDataReports.service;


import com.reports.CultDataReports.dto.DistributionManagerDTO;
import com.reports.CultDataReports.dto.InvoiceDto;

import java.util.List;

public interface IInvoiceSearchService {
    List<InvoiceDto> invoiceSearchByDistributionManagers(List<String> ids, String from, String to);

     List<DistributionManagerDTO> fetchDistributionManagers(Boolean onlyMapped);
}
