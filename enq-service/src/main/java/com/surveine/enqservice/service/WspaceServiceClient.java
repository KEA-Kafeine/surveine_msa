package com.surveine.enqservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "wspace-service")
public interface WspaceServiceClient {
    @GetMapping("wspace-service/e7/{enqId}")
    Long getCboxIdByEnqId(Long enqId);
}
