package com.reports.CultDataReports.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Commons {

    public static Sort getOrder(String sortBy, boolean sortDesc) {
        Sort sort;
        if (sortDesc) {
            sort = Sort.by(sortBy).descending();
        } else {
            sort = Sort.by(sortBy);
        }
        return sort;
    }

    public static Integer getPageSize(Integer pageSize) {
        // All records option
        if (pageSize == -1) {
            pageSize = Integer.MAX_VALUE;
        }
        return pageSize;
    }

    public static Date getDateOfDaysBeforeNowIrrespectiveOfTime(Integer days) {
        long now = System.currentTimeMillis();
        long currentDay = now - now % TimeUnit.DAYS.toMillis(1);
        return new Date(currentDay - TimeUnit.DAYS.toMillis(days));
    }

    public static PageRequest getPageRequest(Integer pageNumber, Integer pageSize, String sortBy, boolean sortDesc) {
        Sort sort = getOrder(sortBy, sortDesc);
        pageSize = getPageSize(pageSize);

        return PageRequest.of(pageNumber, pageSize, sort);
    }

    public static PageRequest getPageRequest(Integer pageNumber, Integer pageSize, Sort sort) {
        pageSize = getPageSize(pageSize);
        return PageRequest.of(pageNumber, pageSize, sort);
    }

    public static PageRequest getPageRequest(Integer pageNumber, Integer pageSize) {
        pageSize = getPageSize(pageSize);

        return PageRequest.of(pageNumber, pageSize);
    }
}
