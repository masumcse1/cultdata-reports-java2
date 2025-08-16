package com.reports.CultDataReports.advice;

import com.reports.CultDataReports.security.ApplicationUser;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CurrentUserAdvice {

    @ModelAttribute("currentUser")
    public ApplicationUser getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof ApplicationUser) {
            return (ApplicationUser) authentication.getPrincipal();
        }
        return null;
    }
}
