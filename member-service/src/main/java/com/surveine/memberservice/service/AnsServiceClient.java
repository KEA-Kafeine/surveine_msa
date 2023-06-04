package com.surveine.memberservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ans-service")
public interface AnsServiceClient {
    @GetMapping("/ans-service/m1/{memberId}")
    Long getAnsCountByMemberId(@PathVariable Long memberId);
}
