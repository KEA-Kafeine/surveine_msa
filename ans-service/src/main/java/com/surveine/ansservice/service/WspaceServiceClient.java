package com.surveine.ansservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("wspace-service")
public interface WspaceServiceClient {
    @GetMapping("/wspace-service/defaultabox/{memberId}")
    Long getMemberDefaultAbox(@PathVariable Long memberId);
}
