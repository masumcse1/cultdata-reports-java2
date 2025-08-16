package com.reports.CultDataReports.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CultReportController {
    @GetMapping("/web/odpsearchform.html")
    public String odpSearchForm() {
        return "web/odpsearchform"; // Note: no .html extension needed
    }

    @GetMapping("/web/invoicesearchform.html")
    public String invoiceSearchForm() {
        return "web/invoicesearchform"; // Note: no .html extension needed
    }

    @GetMapping("/web/conversionratesearchform.html")
    public String conversionRateSearchForm() {
        return "web/conversionratesearchform"; // Note: no .html extension needed
    }

    @GetMapping("/web/campaignmessagereport.html")
    public String campaignMessageReport() {
        return "web/campaignmessagereport"; // Note: no .html extension needed
    }
}


