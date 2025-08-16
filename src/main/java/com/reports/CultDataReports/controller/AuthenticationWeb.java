package com.reports.CultDataReports.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationWeb {

    @GetMapping("/")
    public String index( Model model) {

        return "web/dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "web/login";
    }
}
