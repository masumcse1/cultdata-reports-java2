package com.reports.CultDataReports.integraion;

import com.reports.CultDataReports.dto.*;
import com.reports.CultDataReports.exception.AppException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Data
@RequiredArgsConstructor
@Component
public class CultDataRestClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${cultdata.api.key}")
    private  String apiKey ;

    @Value("${cultdata.api.base-url}")
    private String baseUrl;

    Logger logger = LoggerFactory.getLogger(CultDataRestClient.class);

    public List<InvoiceDto> getInvoicesByDistributionManagerIdCultSwitch(String dMID , String startGeneratedDate, String endGeneratedDate) {
        String apiUrl = baseUrl + "api/invoices/distribution-manager/" + dMID;

        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("start_generated_date", startGeneratedDate)
                .queryParam("end_generated_date", endGeneratedDate)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", apiKey);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<InvoiceDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<List<InvoiceDto>>() {}
            );

            if (response.getBody() != null) {
                return response.getBody();
            }
        } catch (RestClientException e) {
            logger.error("Failed to fetch invoices from CultDataAPI: {}", e.toString());
        } catch (Exception e) {
            logger.error("Unexpected error while fetching invoices from CultDataAPI: {}", e.toString());
            throw new AppException("Error while fetching invoices from CultDataAPI", e);
        }

        return Collections.emptyList();
    }

    @Cacheable(value = "distributionManagersCache", key = "'distributionManagerData'")
    public List<DistributionManagerDTO> getCacheDistributionManagers(Boolean only_mapped) {
        logger.info("Fetching fresh distribution managers data");
        return getDistributionManagers(only_mapped);
    }

    public List<DistributionManagerDTO> getDistributionManagers(Boolean only_mapped) {

        logger.info(" Fetch distribution managers data--from CultData api ");

        String apiUrl = baseUrl + "api/distribution-managers";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("limit", 200)
                .queryParam("page", 1);

        if (only_mapped != null) {
            builder.queryParam("only_mapped", only_mapped);
        }

        String url = builder.toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", apiKey);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<DistributionManagerResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<DistributionManagerResponse>() {}
            );

            if (response.getBody() != null && response.getBody().getData() != null) {
                return response.getBody().getData();
            }
        } catch (RestClientException e) {
            logger.error("Failed to fetch distribution managers: {}", e.toString(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching distribution managers: {}", e.toString(), e);
            throw new AppException("Error while fetching distribution managers", e);
        }
        return Collections.emptyList();
    }


    public BookingResponseDto getBookings(String clientId, String channelId, String bookingDate) {
        String apiUrl = baseUrl + "/api/bookings";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("limit", 100)
                .queryParam("page", 1);

        if (clientId != null && !clientId.isEmpty()) {
            builder.queryParam("client_id", clientId);
        }
        if (channelId != null && !channelId.isEmpty()) {
            builder.queryParam("channel_id", channelId);
        }
        if (bookingDate != null && !bookingDate.isEmpty()) {
            builder.queryParam("booking_date", bookingDate);
        }

        String url = builder.toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", apiKey);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<BookingResponseDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<BookingResponseDto>() {}
            );

            return response.getBody();

        } catch (RestClientException e) {
            logger.error("Failed to fetch bookings from CultDataAPI: {}", e.getMessage());

        } catch (Exception e) {
            logger.error("Unexpected error while fetching bookings from CultDataAPI", e);
            throw new AppException("Error while fetching bookings from CultDataAPI", e);
        }

        return null;
    }

}
