package com.reports.CultDataReports.integraion;

import com.reports.CultDataReports.exception.ReportException;
import com.reports.CultDataReports.responsedto.PropertyDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PropertyRestClient {

    @Value("${property.api.url}")
    private String propertyUrl;

    @Value("${property.api.key}")
    private String apiKey;

    @Value("${server.port}")  
    private String serverPort;

    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(PropertyRestClient.class);
    private static final String DATA_NOT_FOUND = "Properties not found for given IDs";

    public List<PropertyDto> getPropertiesByIds(List<Integer> ids) throws ReportException {
        String cleanedBaseUrl = propertyUrl.replaceAll("/$", "")+serverPort;
        String apiUrl = cleanedBaseUrl+"/api/v1/properties/search";

        logger.info("Fetching properties for IDs: {}", ids);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);
        HttpEntity<List<Integer>> entity = new HttpEntity<>(ids, headers);

        for (int retry = 0; retry < 3; retry++) {
            try {
                ResponseEntity<List<PropertyDto>> response = restTemplate.exchange(
                        apiUrl,
                        HttpMethod.POST,
                        entity,
                        new ParameterizedTypeReference<List<PropertyDto>>() {}
                );

                if (response.getStatusCode() == HttpStatus.OK) {
                    return response.getBody();
                } else {
                    Thread.sleep(1000L * retry);
                }
            } catch (Exception e) {
                logger.error("Attempt {} failed: {}", retry + 1, e.getMessage());
                if (retry == 2) {
                    throw new ReportException("Failed to fetch properties after retries: " + e.getMessage());
                }
            }
        }

        logger.error(DATA_NOT_FOUND);
        throw new ReportException(DATA_NOT_FOUND);
    }
}