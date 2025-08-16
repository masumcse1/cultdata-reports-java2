package com.reports.CultDataReports.controller;

import com.reports.CultDataReports.dto.DistributionManagerDTO;
import com.reports.CultDataReports.dto.InvoiceDto;
import com.reports.CultDataReports.dto.InvoiceSearchDTO;
import com.reports.CultDataReports.mapper.InvoiceMapper;
import com.reports.CultDataReports.service.IInvoiceSearchService;
import com.reports.CultDataReports.util.DateFormatUtil;
import com.reports.CultDataReports.util.DateUtil;
import com.reports.CultDataReports.util.JasperReportUtil;
import com.reports.CultDataReports.util.NumberFormatUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/invoice/api")
public class InvoiceSearchWeb {

    @Autowired
    private IInvoiceSearchService invoiceSearchService;

    @Value("${report.invoice.template-path}")
    private String invoiceReportTemplatePath;

    private static final Logger logger = LoggerFactory.getLogger(InvoiceSearchWeb.class);

    @GetMapping("/distribution-managers")
    public ResponseEntity<List<DistributionManagerDTO>> getDistributionManagers(
            @RequestParam(defaultValue = "true") Boolean onlyMapped) {
        logger.info("Invoice Fetching distribution managers request --start");
        return ResponseEntity.ok(invoiceSearchService.fetchDistributionManagers(onlyMapped));
    }

    @PostMapping("/invoice-result")
    public ResponseEntity<InvoiceSearchResponse> searchInvoices(
            @RequestBody InvoiceSearchDTO dto) {

        logger.info("Invoice search request --start");
        try {
            String startDate = DateFormatUtil.formatDate(dto.getStartGeneratedDate());
            String endDate = DateFormatUtil.formatDate(dto.getEndGeneratedDate());

            List<InvoiceDto> invoices = invoiceSearchService.invoiceSearchByDistributionManagers(
                    dto.getDistributionManagers(),
                    startDate,
                    endDate);

            double totalRevenue = invoices.stream()
                    .filter(inv -> inv.getAmountInEur() != null)
                    .mapToDouble(InvoiceDto::getAmountInEur)
                    .sum();
            String totalRevenueStr = NumberFormatUtil.formatAsEuropeanNumber(totalRevenue);

            logger.info("Invoice search request --end");
            return ResponseEntity.ok(new InvoiceSearchResponse(invoices, totalRevenueStr));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InvoiceSearchResponse(Collections.emptyList(), "0.0"));
        }


    }

    @PostMapping("/export")
    public void exportToExcel(@RequestBody InvoiceSearchDTO dto, HttpServletResponse response) throws Exception {
        try {
        logger.info("Invoice report export  --start");
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String fileName = "Invoice_Report_" + currentDate + ".xlsx";
        byte[] reportBytes = generateInvoiceReportAsExcel(dto);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.getOutputStream().write(reportBytes);
        logger.info("Invoice report export  --done");
        } catch (Exception e) {
            logger.error("Error occurred while exporting invoice report to Excel", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to export invoice report.");
        }
    }

    private byte[] generateInvoiceReportAsExcel(InvoiceSearchDTO request) throws Exception {

        String startDate = DateFormatUtil.formatDate(request.getStartGeneratedDate());
        String endDate = DateFormatUtil.formatDate(request.getEndGeneratedDate());

        List<InvoiceDto> invoices = invoiceSearchService.invoiceSearchByDistributionManagers(
                request.getDistributionManagers(),
                startDate,
                endDate);

        List<InvoiceMapper> invoiceMappers = invoices.stream()
                .map(this::mapInvoiceDtoToMapper)
                .collect(Collectors.toList());

        return JasperReportUtil.exportToExcel(invoiceReportTemplatePath, invoiceMappers, null);
    }

    private InvoiceMapper mapInvoiceDtoToMapper(InvoiceDto invoicedto) {
        InvoiceMapper mapper = new InvoiceMapper();
        mapper.setId(invoicedto.getId());
        mapper.setInvoice_name(invoicedto.getInvoiceName());
        mapper.setClient_id(invoicedto.getClient() != null ? invoicedto.getClient().getId() : null);
        mapper.setClient_name(invoicedto.getClient() != null ? invoicedto.getClient().getName() : null);
        mapper.setDistributionManagerName(invoicedto.getClient() != null ? invoicedto.getClient().getDistributionManagerName(): null);

        if (invoicedto.getGeneratedDate() != null) {
            mapper.setGenerated_date(invoicedto.getGeneratedDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        }

        if (invoicedto.getPeriodFrom() != null) {
            mapper.setPeriod_from(DateUtil.formatWithPattern(invoicedto.getPeriodFrom(), "dd MMM yyyy"));
        }

        if (invoicedto.getPeriodTo() != null) {
            mapper.setPeriod_to(DateUtil.formatWithPattern(invoicedto.getPeriodTo(), "dd MMM yyyy"));
        }

        mapper.setTotal_net(invoicedto.getTotalNet());
        mapper.setCurrency(invoicedto.getCurrency());
        mapper.setAmount_in_eur(invoicedto.getAmountInEur());
        mapper.setConvertedcurrency(invoicedto.getConvertedCurrency());
        return mapper;
    }

    @Data
    @AllArgsConstructor
    private static class InvoiceSearchResponse {
        private List<InvoiceDto> invoices;
        private String totalRevenue;
    }

    public record DistributionManager(Integer id, String name) {}
}

record InvoiceSearchRequest(
        List<Integer> distributionManagers,
        LocalDate startGeneratedDate,
        LocalDate endGeneratedDate
) {}


record InvoiceSearchResponse(
        List<InvoiceDto> invoices,
        String totalRevenue
) {}