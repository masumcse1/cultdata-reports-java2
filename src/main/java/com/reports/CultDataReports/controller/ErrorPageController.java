package com.reports.CultDataReports.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorPageController {

    @GetMapping("/error-404")
    public String error404() {
        return "error/404";
    }
}
