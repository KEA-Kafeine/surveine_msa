package com.surveine.memberservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "wspace-service")
public interface WspaceServiceClient {
    @PostMapping("/wspace-service/au1/{memberId}")
    void createDefaultBoxes(@PathVariable Long memberId);
}
