package com.bank.star.star.controller;

import com.bank.star.star.DTO.ServiceInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/management")
public class ManagementController {

    @Value("${spring.application.name:star}")
    private String appName;

    @Value("${app.version:0.0.1-SNAPSHOT}")
    private String appVersion;

    private final CacheManager cacheManager;

    public ManagementController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @PostMapping("/clear-caches")
    public String clearCaches() {
        cacheManager.getCacheNames().forEach(cacheName -> {
            if (cacheManager.getCache(cacheName) != null) {
                cacheManager.getCache(cacheName).clear();
            }
        });
        return "Кеш успешно очищен";
    }

    @GetMapping("/info")
    public ServiceInfoResponse getServiceInfo() {
        return new ServiceInfoResponse(appName, appVersion);
    }
}