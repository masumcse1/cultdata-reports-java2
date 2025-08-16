package com.reports.CultDataReports.dto;

import lombok.Data;
import lombok.ToString;


@ToString
public class DistributionManagerDTO {
    private Integer id;
    private String name;
    private String email;
    private Integer businessUnitId;

    public DistributionManagerDTO() {
    }

    public DistributionManagerDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(Integer businessUnitId) {
        this.businessUnitId = businessUnitId;
    }
}
