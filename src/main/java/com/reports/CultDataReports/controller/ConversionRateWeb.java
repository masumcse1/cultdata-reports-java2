package com.reports.CultDataReports.controller;

import com.reports.CultDataReports.dto.Client6Dto;
import com.reports.CultDataReports.dto.ConversionDTO;
import com.reports.CultDataReports.dto.ConversionRateSearchDTO;
import com.reports.CultDataReports.dto.DistributionManagerDTO;
import com.reports.CultDataReports.service.IConversionRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.reports.CultDataReports.util.BigDecimalUtil.formatOneDigitDecimal;
import static com.reports.CultDataReports.util.BigDecimalUtil.formatTwoDigitDecimal;

@RestController
@RequestMapping("/conversionrate/api")
public class ConversionRateWeb {

    @Autowired
    private IConversionRateService conversionRateService;

    private static final Logger logger = LoggerFactory.getLogger(ConversionRateWeb.class);

    @GetMapping("/distribution-managers")
    public ResponseEntity<List<DistributionManagerDTO>> getDistributionManagers(
            @RequestParam(defaultValue = "true") Boolean onlyMapped) {
        logger.info("ConversionRateWeb Fetching distribution managers request --start");
        List<DistributionManagerDTO> managers = conversionRateService.fetchDistributionManagers(onlyMapped);
        return ResponseEntity.ok(managers);
    }

    @PostMapping("/conversionrate-result")
    public ResponseEntity<ConversionRateResponse> searchConversionRates(
            @RequestBody ConversionRateSearchDTO dto) {
        logger.info("ConversionRate-result request --start");

        Integer totalBookingValue = 0;

        List<ConversionDTO> conversionRates = dto.getClientId().isEmpty() ?
                conversionRateService.getConversionForAllClients(dto) :
                conversionRateService.getConversionRatesByClient(dto);

        if(conversionRates != null && !conversionRates.isEmpty() ){
            totalBookingValue = dto.getClientId().isEmpty() ?
                    conversionRateService.getTotalBookingValueByDm(dto):
                    conversionRateService.getTotalBookingValueByClient(dto);
        }

        Map<String, Object> totalConversion = conversionRates != null && !conversionRates.isEmpty() ?
                getTotalValue(conversionRates,totalBookingValue) : new HashMap<>();

        ConversionRateResponse response = new ConversionRateResponse(conversionRates, totalConversion);

        logger.info("ConversionRate-result request --end");
        return ResponseEntity.ok(response);
    }

    private Map<String, Object> getTotalValue(List<ConversionDTO> conversionRates ,Integer totalBookingValue) {
        long totalNoClientCount = conversionRates.stream()
                .map(ConversionDTO::getClient)
                .filter(Objects::nonNull)
                .map(Client6Dto::getId)
                .filter(Objects::nonNull)
                .distinct()
                .count();

        Integer totalNoOfAvailabilitySearch = conversionRates.stream()
                .filter(inv -> inv.getNoOfAvailabilitySearch() != null)
                .mapToInt(ConversionDTO::getNoOfAvailabilitySearch)
                .sum();

        double totalResponseTime = conversionRates.stream()
                .map(ConversionDTO::getResponseTime)
                .filter(rt -> rt != 0.0f)
                .mapToDouble(rt -> rt)
                .sum();

        long count = conversionRates.stream()
                .map(ConversionDTO::getResponseTime)
                .filter(rt -> rt != 0.0f)
                .count();

        double averageResponseTime = count != 0 ? totalResponseTime / count : 0.0;

        Integer totalNoOfUniqueVisitors = conversionRates.stream()
                .filter(inv -> inv.getNoOfUniqueVisitors() != null)
                .mapToInt(ConversionDTO::getNoOfUniqueVisitors)
                .sum();

        Integer totalBookingForConversion = conversionRates.stream()
                .filter(inv -> inv.getNumberOfBookings() != null)
                .mapToInt(ConversionDTO::getNumberOfBookings)
                .sum();

        double totalUniqueVisitorRate = totalNoOfUniqueVisitors != 0
                ? (double) totalNoOfAvailabilitySearch / totalNoOfUniqueVisitors
                : 0.0;

        double totalConversionRate = totalNoOfUniqueVisitors != 0
                ? ((double) totalBookingForConversion / totalNoOfUniqueVisitors) * 100
                : 0.0;

        Boolean isConverionMissingDueToResponseTime = false;

        if(!totalBookingValue.equals(totalBookingForConversion)){
            isConverionMissingDueToResponseTime = true;
        }

        Map<String, Object> totalValue = new HashMap<>();
        totalValue.put("totalNoClientCount", totalNoClientCount);
        totalValue.put("totalNoOfAvailabilitySearch", totalNoOfAvailabilitySearch);
        totalValue.put("averageResponseTime", formatOneDigitDecimal(averageResponseTime));
        totalValue.put("totalNoOfUniqueVisitors", totalNoOfUniqueVisitors);
        totalValue.put("totalBooking", totalBookingForConversion);
        totalValue.put("totalUniqueVisitorRate", formatTwoDigitDecimal(totalUniqueVisitorRate));
        totalValue.put("totalConversionRate", String.valueOf(formatOneDigitDecimal(totalConversionRate)) + "%");
        totalValue.put("converisondatamissing", isConverionMissingDueToResponseTime);

        return totalValue;
    }

    record ConversionRateResponse(List<ConversionDTO> conversionRates, Map<String, Object> totalConversion) {}
}