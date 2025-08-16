package com.reports.CultDataReports.service;


import com.reports.CultDataReports.dto.*;
import com.reports.CultDataReports.exception.ReportException;
import com.reports.CultDataReports.integraion.CultDataBookingRestClient;
import com.reports.CultDataReports.integraion.CultDataRestClient;
import com.reports.CultDataReports.integraion.CultDataRestClientForConversion;
import com.reports.CultDataReports.responsedto.ClientChannelBookingsDto;
import com.reports.CultDataReports.responsedto.DistributionManagerBookingStats;
import com.reports.CultDataReports.util.DateFormatUtil;
import com.reports.CultDataReports.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.reports.CultDataReports.util.BigDecimalUtil.formatOneDigitDecimal;
import static com.reports.CultDataReports.util.BigDecimalUtil.formatTwoDigitDecimal;


@RequiredArgsConstructor
@Service
public class ConversionRateService implements IConversionRateService {

    private static final Logger logger = LoggerFactory.getLogger(ConversionRateService.class);

    private final CultDataRestClient cultDataClient;

    private final CultDataRestClientForConversion cultDataRestClientForConversion;

    private final CultDataBookingRestClient cultDataBookingRestClient;

    public List<ConversionDTO> getConversionForAllClients(ConversionRateSearchDTO conversionModel) {
        List<ConversionDTO>  conversionResultDTOS = new ArrayList<>();

        String startDate = DateFormatUtil.formatDate(conversionModel.getFromDate());
        String endDate = DateFormatUtil.formatDate(conversionModel.getToDate());

        List<ConversionDTO> conversionDtos = cultDataRestClientForConversion.getConversionsForAllClient(conversionModel.getDistributionManagers(),startDate,endDate,conversionModel.isExcludeTestProperties());

        List<Integer> uniqueClientIds = conversionDtos.stream().map(conversion -> conversion.getClient().getId()).distinct().collect(Collectors.toList());
        for (Integer clientID : uniqueClientIds) {

            List<ConversionDTO> conversionListByClient = conversionDtos.stream().filter(c -> Objects.equals(c.getClient().getId(), clientID)).collect(Collectors.toList());

            if (!conversionListByClient.isEmpty()) {
                conversionListByClient.forEach(dto -> dto.setResponseTime(dto.getResponseTime() * 1000)); // convert to miliseconds
                int availabilitySearchSum = conversionListByClient.stream().mapToInt(ConversionDTO::getNoOfAvailabilitySearch).sum();
                double responseTimeSum = (double) conversionListByClient.stream().mapToDouble(ConversionDTO::getResponseTime).sum();
                int uniqueVisitorSum = conversionListByClient.stream().mapToInt(ConversionDTO::getNoOfUniqueVisitors).sum();
                int totalBookingSum = conversionListByClient.stream().mapToInt(ConversionDTO::getNumberOfBookings).sum();

                if (availabilitySearchSum > 0 || uniqueVisitorSum > 0 || totalBookingSum > 0) {

                    ConversionDTO conversionDTO = new ConversionDTO();

                    Client6Dto clientDto = new Client6Dto();
                    clientDto.setId(clientID);
                    conversionDTO.setClient(clientDto);

                    conversionDTO.setChannel(new Channel3(Integer.valueOf(conversionModel.getChannelId())));
                    conversionDTO.setPeriod(conversionModel.getFromDate() + " - " + conversionModel.getToDate());
                    conversionDTO.setNoOfAvailabilitySearch(availabilitySearchSum);
                    conversionDTO.setResponseTime(calculateAverageResponseTime(responseTimeSum, conversionListByClient.size()));
                    conversionDTO.setNoOfUniqueVisitors(uniqueVisitorSum);
                    conversionDTO.setNumberOfBookings(totalBookingSum);
                    conversionDTO.setUniqueVisitorRate(calculateSearchVisitorRate(availabilitySearchSum, uniqueVisitorSum));
                    conversionDTO.setConversionRate(calculateConversionRate(totalBookingSum, uniqueVisitorSum));

                    conversionResultDTOS.add(conversionDTO);
                }

            }
        }

        return  conversionResultDTOS;
    }



    @Override
    public List<ConversionDTO> getConversionRatesByClient(ConversionRateSearchDTO conversionModel) {
        List<ConversionDTO> conversionDtos = null ;
        String startDate = DateFormatUtil.formatDate(conversionModel.getFromDate());
        String endDate = DateFormatUtil.formatDate(conversionModel.getToDate());

        try {

            conversionDtos = cultDataRestClientForConversion.getConversionsByClientID(conversionModel.getClientId(),startDate, endDate,conversionModel.isExcludeTestProperties());

            for (ConversionDTO conversionDTO : conversionDtos)
                if (conversionDTO.getNoOfAvailabilitySearch() > 0 || conversionDTO.getNoOfUniqueVisitors() > 0 || conversionDTO.getNumberOfBookings() > 0) {
                    conversionDTO.setPeriod(DateUtil.formatDateTime(conversionDTO.getDate(),"dd MMM yyyy"));
                    conversionDTO.setUniqueVisitorRate(calculateSearchVisitorRate(conversionDTO.getNoOfAvailabilitySearch(), conversionDTO.getNoOfUniqueVisitors()));
                    conversionDTO.setConversionRate(calculateConversionRate(conversionDTO.getNumberOfBookings(), conversionDTO.getNoOfUniqueVisitors()));
                    conversionDTO.setResponseTime(formatOneDigitDecimal(conversionDTO.getResponseTime()*1000)); // convert to milis
                }
        }catch (ReportException e){

        }

        return conversionDtos ;
    }

    public Integer getTotalBookingValueByDm(ConversionRateSearchDTO conversionModel){
        Integer totalBookingValue= 0;
        String startDate = DateFormatUtil.formatDate(conversionModel.getFromDate());
        String endDate = DateFormatUtil.formatDate(conversionModel.getToDate());
        List<Integer> distributionManagerIds = conversionModel.getDistributionManagers().stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        DistributionManagerBookingStats distributionManagerBookingStats = cultDataBookingRestClient.getNoOfBookingsByDmIds(startDate, endDate, distributionManagerIds);
        totalBookingValue = distributionManagerBookingStats.getNumberOfTransactions();

        return totalBookingValue;
    }

    public Integer getTotalBookingValueByClient(ConversionRateSearchDTO conversionModel){
        String startDate = DateFormatUtil.formatDate(conversionModel.getFromDate());
        String endDate = DateFormatUtil.formatDate(conversionModel.getToDate());
        ClientChannelBookingsDto clientChannelBookings = cultDataBookingRestClient.getClientChannelBookings(conversionModel.getClientId(),startDate, endDate);
        return clientChannelBookings.getNumberOfTransactions();
    }

    public List<DistributionManagerDTO> fetchDistributionManagers(Boolean onlyMapped) {
        List<DistributionManagerDTO> managers = cultDataClient.getCacheDistributionManagers(onlyMapped);
        logger.info("fetchDistributionManagers retrieved: count = {}", managers.size());
        return managers;
    }

    private double calculateAverageResponseTime(double sum, int count) {
        double result = (count > 0 && sum > 0) ? sum / count : 0;
        return formatOneDigitDecimal(result);
    }

    private String calculateSearchVisitorRate(int availabilitySearch, int uniqueVisitors) {
        double result = (availabilitySearch > 0 && uniqueVisitors > 0) ?
                (double) availabilitySearch / uniqueVisitors : 0;
        return formatTwoDigitDecimal(result);
    }

    private String calculateConversionRate(int bookings, int uniqueVisitors) {
        double result = (bookings > 0 && uniqueVisitors > 0) ?
                ((double) bookings / uniqueVisitors) * 100 : 0;
        double roundedValue = formatOneDigitDecimal(result);
        return String.valueOf(roundedValue) + "%";
    }

}