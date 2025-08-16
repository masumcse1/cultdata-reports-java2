package com.reports.CultDataReports.integraion;

import com.reports.CultDataReports.responsedto.BookingCultSwitchManagerRequest;
import com.reports.CultDataReports.responsedto.ClientChannelBookingsDto;
import com.reports.CultDataReports.responsedto.DistributionManagerBookingStats;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@RequiredArgsConstructor
@Component
public class CultDataBookingRestClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${cultdata.api.key}")
    private  String apiKey ;

    @Value("${cultdata.api.base-url}")
    private String baseUrl;

    @Value("${cultdata.api.cultbooking-neo-channel-id}")
    private int cultbookingNeoChannelId;

    Logger logger = LoggerFactory.getLogger(CultDataBookingRestClient.class);

    public ClientChannelBookingsDto getClientChannelBookings(String client_id, String date_from, String date_to) {
        String apiUrl = baseUrl + "/api/bookings/client/channel";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("date_from", date_from)
                .queryParam("date_to", date_to)
                .queryParam("channel", cultbookingNeoChannelId)
                .queryParam("client_id", client_id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", apiKey);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ClientChannelBookingsDto> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    requestEntity,
                    ClientChannelBookingsDto.class
            );

            return response.getBody();

        } catch (RestClientException e) {
            // Handle specific API exceptions here
            throw new RuntimeException("Failed to fetch client channel bookings from CultDataAPI", e);
        }
    }


    public DistributionManagerBookingStats getNoOfBookingsByDmIds(String date_from, String date_to, List<Integer> distributionManagerIds) {
        String apiUrl = baseUrl + "/api/bookings/cultSwitchManager/channel";

        // Prepare request DTO
        BookingCultSwitchManagerRequest request = new BookingCultSwitchManagerRequest();
        request.setFromDate(date_from);
        request.setToDate(date_to);
        request.setChannel(String.valueOf(cultbookingNeoChannelId));
        request.setCultSwitchDistributionManagerIds(distributionManagerIds);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", apiKey);

        HttpEntity<BookingCultSwitchManagerRequest> requestEntity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<DistributionManagerBookingStats> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    DistributionManagerBookingStats.class
            );

            return response.getBody();

        } catch (RestClientException e) {
            logger.error("Failed to fetch bookings by distribution managers from CultDataAPI", e);
            throw new RuntimeException("Failed to fetch bookings by distribution managers from CultDataAPI", e);
        }
    }
}

