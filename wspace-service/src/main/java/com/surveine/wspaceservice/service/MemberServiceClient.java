package com.surveine.wspaceservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "member-service")
public interface MemberServiceClient {
    @GetMapping("/member-service/ws1/{memberId}")
    String getMemberName(@PathVariable Long memberId);
}
