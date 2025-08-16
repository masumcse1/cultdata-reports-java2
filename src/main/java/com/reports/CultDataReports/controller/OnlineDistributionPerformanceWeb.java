package com.reports.CultDataReports.controller;


import com.reports.CultDataReports.dto.Client6Dto;
import com.reports.CultDataReports.dto.DistributionManagerDTO;
import com.reports.CultDataReports.dto.OnlineDistributionPerformanceSearchRequest;
import com.reports.CultDataReports.dto.ReportDto;
import com.reports.CultDataReports.service.IOnlineDistributionPerformanceSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/odp/api")
public class OnlineDistributionPerformanceWeb {

    @Autowired
    private IOnlineDistributionPerformanceSearchService odpSearchService;

    private static final Logger logger = LoggerFactory.getLogger(OnlineDistributionPerformanceWeb.class);

    @GetMapping("/distribution-managers")
    public ResponseEntity<List<DistributionManagerDTO>> getDistributionManagers(
            @RequestParam(defaultValue = "true") Boolean onlyMapped) {

        logger.info("Fetching distribution managers request --start");

        List<DistributionManagerDTO> distributionManagerDTOs = odpSearchService.fetchDistributionManagers(onlyMapped);

        List<DistributionManagerDTO> response = distributionManagerDTOs.stream()
                .map(dto -> new DistributionManagerDTO(
                        dto.getId(),
                        dto.getName() ))
                .collect(Collectors.toList());

        logger.info("Fetching distribution managers request --end");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/odp-result")
    public ResponseEntity<List<ReportDto>> searchOdpReports(
            @RequestBody OnlineDistributionPerformanceSearchRequest request) {

        logger.info("ODP search request --start");

        OnlineDistributionPerformanceSearchRequest searchDTO = new OnlineDistributionPerformanceSearchRequest();
        searchDTO.setClient(request.getClient());
        searchDTO.setDistributionManagers(request.getDistributionManagers());

        List<ReportDto> reportDtos= null;
       if (searchDTO.getClient() == null) {
            reportDtos = odpSearchService.getLatestReportsByDmID(searchDTO);
        } else {
            reportDtos = odpSearchService.getLatestReportsByClientID(searchDTO);
        }

        List<ReportDto> response = reportDtos.stream()
                .filter(report -> report != null)
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        logger.info("ODP search request --end");
        return ResponseEntity.ok(response);
    }

    private ReportDto convertToResponse(ReportDto dto) {
        ReportDto response = new ReportDto();

        Client6Dto client6Dto = new Client6Dto();
        client6Dto.setId(dto.getClient().getId());
        client6Dto.setName(dto.getClient().getName());
        response.setClient(client6Dto);

        response.setDmId(dto.getDmId());
        response.setDmName(dto.getDmName());
        response.setMonth(dto.getMonth());
        response.setPdf(dto.getPdf());
        return response;
    }

}