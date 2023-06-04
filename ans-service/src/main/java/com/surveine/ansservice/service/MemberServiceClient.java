package com.surveine.ansservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("member-service")
public interface MemberServiceClient {
    @GetMapping("/member-service/a6/{memberId}")
    String getMemberNameByMemberId(@PathVariable Long memberId);
}
