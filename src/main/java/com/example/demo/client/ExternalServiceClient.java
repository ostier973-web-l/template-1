package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "externalService", url = "${external.service.url}")
public interface ExternalServiceClient {
    @GetMapping("/status")
    String getStatus();
}
