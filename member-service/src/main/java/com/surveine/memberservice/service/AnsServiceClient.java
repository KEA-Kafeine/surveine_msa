package com.surveine.memberservice.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "ans-service")
public interface AnsServiceClient {

    Long getAnsCountByMemberId(Long memberId);
}
