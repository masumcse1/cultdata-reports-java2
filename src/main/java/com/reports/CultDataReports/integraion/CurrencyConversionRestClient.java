package com.reports.CultDataReports.integraion;


import com.reports.CultDataReports.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class CurrencyConversionRestClient {

    @Value("${currencyconversion.api.base-url}")
    private String currencyConversionApiUrl;

    @Autowired
    private RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(CurrencyConversionRestClient.class);

    public Double convert(String currency, Double amount, String date) throws Exception {
        Map<String, Object> currencyRate = getCurrencyByTypeAndDate(currency, date);
        if (currencyRate == null || !currencyRate.containsKey("rate")) {
            throw new Exception("Currency rate not found");
        }

        Double rate = Double.parseDouble(currencyRate.get("rate").toString());
        return Math.round(amount * rate * 100.0) / 100.0;
    }

    public Map<String, Object> getCurrencyByTypeAndDate(String currency, String date) {
        String url = UriComponentsBuilder
                .fromHttpUrl(currencyConversionApiUrl + "api/currencyPair/" + currency + "/EUR")
                .queryParam("date", date)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<>() {}
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null || responseBody.isEmpty()) {
                return null;
            }
            return responseBody;

        } catch (RestClientException e) {
            logger.error("Failed to fetch currency rate from CurrencyAPI: {}", e.toString());
        } catch (Exception e) {
            logger.error("Unexpected error while fetching currency rate: {}", e.toString());
            throw new AppException("Error while fetching currency rate", e);
        }

        return Collections.emptyMap();
    }

}
