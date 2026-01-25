package com.bank.star.star.controller;

import com.bank.star.star.DTO.ServiceInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/management")
public class ManagementController {

    @Value("${spring.application.name:star}")
    private String appName;

    @Value("${app.version:0.0.1-SNAPSHOT}")
    private String appVersion;

    @PostMapping("/clear-caches")
    public String clearCaches() {
        // В реальном приложении здесь была бы очистка кеша
        // Для POC просто возвращаем сообщение
        return "Кеш успешно очищен";
    }

    @GetMapping("/info")
    public ServiceInfoResponse getServiceInfo() {
        return new ServiceInfoResponse(appName, appVersion);
    }
}