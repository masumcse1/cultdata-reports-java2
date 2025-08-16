package com.reports.CultDataReports.mapper;

import java.util.Date;

public interface CustomerMapper {
    Long getId();
    String getFirstname();
    String getLastname();
    String getEmail();
    Date getBirth_date();
    Date getAddmission_date();
}
