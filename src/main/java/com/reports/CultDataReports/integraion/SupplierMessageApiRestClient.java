package com.reports.CultDataReports.integraion;

import com.reports.CultDataReports.exception.ReportException;
import com.reports.CultDataReports.responsedto.CampaignMessageDto;
import com.reports.CultDataReports.responsedto.MessageContentSupplier;
import com.reports.CultDataReports.responsedto.SupplierResultDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Component
public class SupplierMessageApiRestClient {

    @Value("${supplier.api.url}")
    private String supplierUrl;

    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(SupplierMessageApiRestClient.class);
    private static final String DATA_NOT_FOUND = "Data not found";


    public List<CampaignMessageDto> getMessageSentData(String token, Integer campaignId,String campaignName,Boolean permission, String sentFromDate, String sentToDate)  throws ReportException  {
        LocalDate nextDate = LocalDate.now().plusDays(1);
        int page=0;
        MessageContentSupplier messageContentSupplier = getMessageSentList(token,page,campaignId,campaignName,permission, sentFromDate, sentToDate);
        //get total pages in supplier API
        int totalPages = messageContentSupplier.getTotalPages();
        logger.info("Total Pages in supplier API- schedule message :"+totalPages);
        //get required data afrom supplier API
        List<CampaignMessageDto> messageRdos = new ArrayList<>();
        for (int i=0;i<totalPages;i++){
            MessageContentSupplier messagecontent =  getMessageSentList(token,i,campaignId,campaignName,permission, sentFromDate, sentToDate);
            messageRdos.addAll(messagecontent.getContent());
        }
        return messageRdos;
    }

    public MessageContentSupplier getMessageSentList( String token, Integer page, Integer campaignId,
                                                      String campaignName,Boolean permission,
                                                      String sentFromDate, String sentToDate) throws ReportException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("page", page);
        queryParams.put("size", 1000);
        queryParams.put("campaignId", campaignId);           // optional
        queryParams.put("campaignName", campaignName);     // optional
        queryParams.put("permission", permission);          // optional
        queryParams.put("sentFrom", sentFromDate);
        queryParams.put("sentTo", sentToDate);

        return fetchMessageSentList(queryParams, token);
    }

    private MessageContentSupplier fetchMessageSentList(Map<String, Object> queryParams, String token) throws ReportException {
        String cleanedBaseUrl = supplierUrl.replaceAll("/$", "");
        String apiUrl = cleanedBaseUrl + "/message/v2/sent/paging";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("page", queryParams.get("page"))
                .queryParam("size", queryParams.get("size"))
                .queryParam("sentFrom", queryParams.get("sentFrom"))
                .queryParam("sentTo", queryParams.get("sentTo"));

        builder.queryParamIfPresent("campaignId", Optional.ofNullable(queryParams.get("campaignId")));
        builder.queryParamIfPresent("permission", Optional.ofNullable(queryParams.get("permission")));

        Optional.ofNullable(queryParams.get("campaignName"))
                .map(Object::toString)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .ifPresent(val -> builder.queryParam("name", val));

        logger.info("Campaign Message Report Url: {}", builder.toUriString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        for (int retry = 0; retry < 3; retry++) {
            try {
                ResponseEntity<SupplierResultDto<MessageContentSupplier>> response = restTemplate.exchange(
                        builder.build().encode().toUri(),
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<SupplierResultDto<MessageContentSupplier>>() {}
                );

                if (response.getStatusCode() == HttpStatus.OK) {
                    return Objects.requireNonNull(response.getBody()).getResult();
                } else {
                    Thread.sleep(1000L * retry);
                }
            } catch (Exception e) {
                logger.error("Attempt {} failed: {}", retry + 1, e.getMessage());
            }
        }

        logger.error(DATA_NOT_FOUND);
        throw new ReportException(DATA_NOT_FOUND);
    }
}
