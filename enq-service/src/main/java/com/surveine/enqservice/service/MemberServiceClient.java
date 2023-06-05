package com.surveine.enqservice.service;

import com.surveine.enqservice.dto.MemberDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "member-service")
public interface MemberServiceClient {
    @GetMapping("/member-service/s5/{memberId}")
    Optional<MemberDTO> getMemberDTOById(@PathVariable Long memberId);


}
