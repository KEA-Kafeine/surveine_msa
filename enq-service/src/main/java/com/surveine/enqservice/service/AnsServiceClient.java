package com.surveine.enqservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient("ans-service")
public interface AnsServiceClient {

    @PostMapping("/ans-service/ans/{memberId}")
    String getAnsStatus(@RequestBody Map<String, Long> reqMap, @PathVariable Long memberId);

    @GetMapping("/ans-service/ans-count/{enqId}")
    Long getAnsCountByEnqId(@PathVariable Long enqId);
}
