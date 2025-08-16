package com.reports.CultDataReports.mapper;


public class InvoiceMapper {
    private Integer id;
    private String invoice_name;
    private Integer client_id;
    private String generated_date;
    private String period_from;
    private String period_to;
    private Double total_net;
    private String currency;
    private Double amount_in_eur;
    private String convertedcurrency;
    private String client_name;
    private String distributionManagerName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoice_name() {
        return invoice_name;
    }

    public void setInvoice_name(String invoice_name) {
        this.invoice_name = invoice_name;
    }

    public Integer getClient_id() {
        return client_id;
    }

    public void setClient_id(Integer client_id) {
        this.client_id = client_id;
    }

    public String getGenerated_date() {
        return generated_date;
    }

    public void setGenerated_date(String generated_date) {
        this.generated_date = generated_date;
    }

    public String getPeriod_from() {
        return period_from;
    }

    public void setPeriod_from(String period_from) {
        this.period_from = period_from;
    }

    public String getPeriod_to() {
        return period_to;
    }

    public void setPeriod_to(String period_to) {
        this.period_to = period_to;
    }

    public Double getTotal_net() {
        return total_net;
    }

    public void setTotal_net(Double total_net) {
        this.total_net = total_net;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getAmount_in_eur() {
        return amount_in_eur;
    }

    public void setAmount_in_eur(Double amount_in_eur) {
        this.amount_in_eur = amount_in_eur;
    }

    public String getConvertedcurrency() {
        return convertedcurrency;
    }

    public void setConvertedcurrency(String convertedcurrency) {
        this.convertedcurrency = convertedcurrency;
    }


    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getDistributionManagerName() {
        return distributionManagerName;
    }

    public void setDistributionManagerName(String distributionManagerName) {
        this.distributionManagerName = distributionManagerName;
    }
}
