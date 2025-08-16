package com.reports.CultDataReports.service;

import com.reports.CultDataReports.dto.DistributionManagerDTO;
import com.reports.CultDataReports.dto.InvoiceDto;
import com.reports.CultDataReports.integraion.CultDataRestClient;
import com.reports.CultDataReports.integraion.CurrencyConversionRestClient;
import com.reports.CultDataReports.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;

@RequiredArgsConstructor
@Service
public class InvoiceSearchService implements IInvoiceSearchService {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceSearchService.class);

    private final CultDataRestClient cultDataClient;
    private final CurrencyConversionRestClient currencyConversionRestClient;

    @Value("${currencyConversion.targetCurrency}")
    private String targetCurrency;

    @Override
    public List<InvoiceDto> invoiceSearchByDistributionManagers(List<String> ids, String from, String to) {
        List<InvoiceDto> allInvoices = new ArrayList<>();
        List<DistributionManagerDTO> distributionManagers= fetchDistributionManagers(true);

        for (String id : ids) {
            List<InvoiceDto> invoices = cultDataClient.getInvoicesByDistributionManagerIdCultSwitch(id, from, to);
            List<DistributionManagerDTO> managerDTOS = distributionManagers.stream().filter(m -> m.getId().equals(Integer.valueOf(id))).toList();
            invoices.stream()
                    .map(InvoiceDto::getClient)
                    .forEach(client -> client.setDistributionManagerName(managerDTOS.getFirst().getName()));
            allInvoices.addAll(invoices);
        }

        logger.info("Invoices retrieved: total count = {}", allInvoices.size());

        for (InvoiceDto invoice : allInvoices) {

            if (targetCurrency.equals(invoice.getCurrency())) {
                invoice.setAmountInEur(invoice.getTotalNet());
                invoice.setConvertedCurrency(targetCurrency);
            } else {
                try {
                    Double converted = currencyConversionRestClient.convert(
                            invoice.getCurrency(),
                            invoice.getTotalNet(),
                            DateUtil.formatDateTime(invoice.getGeneratedDate())
                    );
                    invoice.setAmountInEur(converted);
                    invoice.setConvertedCurrency(targetCurrency);
                } catch (Exception e) {
                    logger.info("Currency conversion failed  {}", invoice.getId());
                    invoice.setAmountInEur(null);
                }
            }

        }

        allInvoices.sort(Comparator.comparing(InvoiceDto::getGeneratedDate));
        return allInvoices;
    }

    public List<DistributionManagerDTO> fetchDistributionManagers(Boolean onlyMapped) {
        // List<DistributionManagerDTO> managers = cultDataClient.getDistributionManagers(onlyMapped);
        List<DistributionManagerDTO> managers = cultDataClient.getCacheDistributionManagers(onlyMapped);
        logger.info("fetchDistributionManagers retrieved: count = {}", managers.size());
        return managers;
    }
}
