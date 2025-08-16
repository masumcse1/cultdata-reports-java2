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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.*;


@Data
@RequiredArgsConstructor
@Service
public class CultDataRestClientForOdpr {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${cultdata.api.key}")
    private String apiKey;

    @Value("${cultdata.api.base-url}")
    private String baseUrl;

    private final Logger logger = LoggerFactory.getLogger(CultDataRestClientForOdpr.class);

    public  List<ReportDto>  getLatestOdprReportsByDmID(Integer distributionManagerID) throws ReportException{
        Map<String, Object> params = new HashMap<>();
        params.put("limit",5000);
        params.put("page",1);

        params.put("cultswitch_distribution_manager_id",distributionManagerID);

        return getLatestOdprReports(params);
    }

    public List<ReportDto> getLatestOdprReportsByClientID(Integer clientID) throws ReportException{
        Map<String, Object> params = new HashMap<>();
        params.put("limit",5000);
        params.put("page",1);

        params.put("client_id",clientID);

        return getLatestOdprReports(params);
    }


    public List<ReportDto> getLatestOdprReports(Map<String, Object> queryParams)throws ReportException {
        String apiUrl = baseUrl + "api/reports/latest";


        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl);

        builder.queryParam("limit", queryParams.get("limit"));
        builder.queryParam("page", queryParams.get("page"));

        builder.queryParamIfPresent("client_id",Optional.ofNullable(queryParams.get("client_id")));
        builder.queryParamIfPresent("cultswitch_distribution_manager_id",  Optional.ofNullable(queryParams.get("cultswitch_distribution_manager_id")));

        String url = builder.toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", apiKey);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ReportResponseDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<ReportResponseDto>() {}
            );

            if (response.getBody() != null && response.getBody().getData() != null) {
                return response.getBody().getData();
            }
        } catch (RestClientException e) {
            logger.error("Failed to fetch reports for client {} from CultDataAPI: {}", Optional.ofNullable(queryParams.get("client_id")), e.toString());
            throw new ReportException("Error while fetching reports from CultDataAPI", e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching reports for client {} from CultDataAPI: {}", Optional.ofNullable(queryParams.get("client_id")), e.toString());
            throw new AppException("Error while fetching reports from CultDataAPI", e);
        }

        return Collections.emptyList();
    }


    public DistributionManagerDTO getDistributionManagersById(Integer dmId) throws ReportException{
        String apiUrl = baseUrl + "api/distribution-manager/" + dmId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", apiKey);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<DistributionManagerDTO> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    requestEntity,
                    DistributionManagerDTO.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
        } catch (RestClientException e) {
            logger.error("Failed to fetch distribution manager with ID {} from CultDataAPI: {}", dmId, e.toString());
            throw new ReportException("Error while fetching distribution manager from CultDataAPI", e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching distribution manager with ID {} from CultDataAPI: {}",
                    dmId, e.toString());
            throw new AppException("Error while fetching distribution manager from CultDataAPI", e);
        }

        return  null;
    }

    public Client6Dto getClientById(Integer clientId)throws ReportException {
        String apiUrl = baseUrl + "api/client/" + clientId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", apiKey);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Client6Dto> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    requestEntity,
                    Client6Dto.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
        } catch (RestClientException e) {
            logger.error("Failed to fetch client with ID {} from CultDataAPI: {}", clientId, e.toString());
            throw new ReportException("Error while fetching client from CultDataAPI", e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching client with ID {} from CultDataAPI: {}",
                    clientId, e.toString());
            throw new AppException("Error while fetching client from CultDataAPI", e);
        }

        throw new ReportException("Client not found with ID: " + clientId);
    }




}