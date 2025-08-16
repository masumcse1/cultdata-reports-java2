package com.reports.CultDataReports.integraion;

import com.reports.CultDataReports.dto.*;
import com.reports.CultDataReports.exception.AppException;
import com.reports.CultDataReports.exception.ReportException;
import lombok.Data;
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
import java.util.*;

@Data
@RequiredArgsConstructor
@Component
public class CultDataRestClientForConversion {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${cultdata.api.key}")
    private  String cultDataApiKey ;

    @Value("${cultdata.api.base-url}")
    private String cultDataBaseUrl;

    @Value("${cultdata.api.conversion-page-limit}")
    private int conversionPageLimit;

    @Value("${cultdata.api.cultbooking-neo-channel-id}")
    private int cultbookingNeoChannelId;

    Logger logger = LoggerFactory.getLogger(CultDataRestClientForConversion.class);

    public List<ConversionDTO> getConversionsForAllClient(List<String> distributionManagers,String startDate, String endDate,Boolean excludeTestProperties) throws AppException{
        Map<String, Object> params = new HashMap<>();
        params.put("limit",conversionPageLimit);
        params.put("page",1);
        params.put("channel_id",cultbookingNeoChannelId);

        params.put("cdm_id",distributionManagers);
        params.put("start_date",startDate);
        params.put("end_date",endDate);
        params.put("exclude_test_properties",excludeTestProperties);
        return getConversions(params).getData();
    }

    public List<ConversionDTO> getConversionsByClientID(String clientID,String startDate, String endDate,Boolean excludeTestProperties) throws ReportException{
        Map<String, Object> params = new HashMap<>();
        params.put("limit",conversionPageLimit);
        params.put("page",1);
        params.put("channel_id",cultbookingNeoChannelId);

        params.put("client_id",clientID);
        params.put("start_date",startDate);
        params.put("end_date",endDate);
        params.put("exclude_test_properties",excludeTestProperties);
        return getConversions(params).getData();
    }

    // Need to convert this api to POST method
    public ConoversionResponseDto getConversions(Map<String, Object> queryParams)throws ReportException {
        String apiUrl = cultDataBaseUrl + "api/conversions-with-number-of-bookings-v2";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl);

        builder.queryParam("limit", queryParams.get("limit"));
        builder.queryParam("page", queryParams.get("page"));
        builder.queryParam("channel_id", queryParams.get("channel_id"));

        builder.queryParam("start_date", queryParams.get("start_date"));
        builder.queryParam("end_date", queryParams.get("end_date"));
        builder.queryParamIfPresent("client_id",Optional.ofNullable(queryParams.get("client_id")));
        builder.queryParamIfPresent("cdm_id",  Optional.ofNullable(queryParams.get("cdm_id")));
        builder.queryParamIfPresent("exclude_test_properties",Optional.ofNullable(queryParams.get("exclude_test_properties")));

        String url = builder.toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", cultDataApiKey);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ConoversionResponseDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<ConoversionResponseDto>() {}
            );

            return response.getBody();

        } catch (RestClientException e) {
            logger.error("Failed to fetch conversions from CultDataAPI: {}", e.getMessage(), e);
            throw new ReportException("Error while fetching conversions from CultDataAPI", e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching conversions from CultDataAPI", e);
            throw new AppException("Error while fetching conversions from CultDataAPI", e);
        }


    }



}
