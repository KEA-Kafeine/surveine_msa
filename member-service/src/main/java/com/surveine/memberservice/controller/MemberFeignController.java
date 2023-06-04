package com.surveine.memberservice.controller;

import com.surveine.memberservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberFeignController {
    private final MemberService memberService;
    @GetMapping("/member-service/ws1/{memberId}")
    String getMemberName(@PathVariable Long memberId) {
        String rspString = memberService.getMemberName(memberId);
        return rspString;
    }

    @GetMapping("/member-service/a6/{memberId}")
    String getMemberNameByMemberId(@PathVariable Long memberId) {
        String rspString = memberService.getMemberName(memberId);
        return rspString;
    }
}
