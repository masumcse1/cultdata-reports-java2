package com.reports.CultDataReports.responsedto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccessTokenSupplier {

    private String tokenType;
    private String accessToken;
    private int executionTime;

    private int expiresIn;
}