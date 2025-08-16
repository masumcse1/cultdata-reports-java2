package com.reports.CultDataReports.integraion;

import com.reports.CultDataReports.exception.DataNotFoundException;
import com.reports.CultDataReports.exception.InvalidCredentialsException;
import com.reports.CultDataReports.responsedto.AccessTokenDirectus;
import com.reports.CultDataReports.responsedto.AccessTokenSupplier;
import com.reports.CultDataReports.responsedto.ResultRdo;
import com.reports.CultDataReports.responsedto.TokenCredentials;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class TokenServiceRestClient {

    @Value("${supplier.api.url}")
    String supplierUrl;

    @Value("${directus.api.url}")
    String directusUrl;

    @Value("${supplier.id.key}")
    String supplierId;

    @Value("${supplier.secret.key}")
    String supplierSecret;

    @Value("${directus.login.email}")
    String email;

    @Value("${directus.login.password}")
    String password;

    public static final String INVALID_CREDENTIALS = "Invalid credentials";

    Logger logger = LoggerFactory.getLogger(TokenServiceRestClient.class);

    public AccessTokenSupplier getTokenFromSuppliers() throws InvalidCredentialsException, DataNotFoundException {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String cleanedBaseUrl = supplierUrl.replaceAll("/$", "");
        String apiUrl = String.format("%s/suppliers/get-token?supplierId=%s&supplierSecret=%s",
                cleanedBaseUrl, supplierId, supplierSecret);

        for (int j = 0; j < 3; j++) {
            try {
                ResponseEntity<AccessTokenSupplier> allDataResponse = template.exchange(
                        apiUrl,
                        HttpMethod.POST,
                        entity,
                        AccessTokenSupplier.class);

                if (allDataResponse.getStatusCodeValue() == HttpStatus.OK.value()) {
                    return Objects.requireNonNull(allDataResponse.getBody());
                } else {
                    Thread.sleep(1000 * j);
                }

            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        logger.error(INVALID_CREDENTIALS);
        throw new InvalidCredentialsException(INVALID_CREDENTIALS);
    }

    public AccessTokenDirectus getTokenFromDirectus() throws InvalidCredentialsException, DataNotFoundException {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TokenCredentials> entity = new HttpEntity<>(new TokenCredentials(email, password), headers);

        String cleanedBaseUrl = directusUrl.replaceAll("/$", "");
        String apiUrl = String.format("%s/auth/login", cleanedBaseUrl);

        for (int j = 0; j < 3; j++) {
            try {
                ResponseEntity<ResultRdo<AccessTokenDirectus>> allDataResponse = template.exchange(
                        apiUrl,
                        HttpMethod.POST,
                        entity,
                        new ParameterizedTypeReference<ResultRdo<AccessTokenDirectus>>() {
                        });

                if (allDataResponse.getStatusCodeValue() == HttpStatus.OK.value()) {
                    return Objects.requireNonNull(allDataResponse.getBody().getData());
                } else {
                    Thread.sleep(1000 * j);
                }

            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        logger.error(INVALID_CREDENTIALS);
        throw new InvalidCredentialsException(INVALID_CREDENTIALS);
    }
}
