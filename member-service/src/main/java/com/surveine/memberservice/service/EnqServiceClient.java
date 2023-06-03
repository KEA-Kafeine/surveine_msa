package com.surveine.memberservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "enq-service")
public interface EnqServiceClient {
    @GetMapping("/enq-service/m2/{memberId}")
    Long getEnqCountByMemberId(@PathVariable Long memberId);

}
