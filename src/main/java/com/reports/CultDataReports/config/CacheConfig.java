package com.reports.CultDataReports.config;


import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${spring.cache.cache-names}")
    private String cacheName;

    @Value("${caffeine.max-size}")
    private int maxSize;

    @Value("${caffeine.expire-after-write-minutes}")
    private int expireAfterWriteMinutes;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(cacheName);
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireAfterWriteMinutes, TimeUnit.MINUTES));
        return cacheManager;
    }
}