package com.surveine.memberservice.controller;

import com.surveine.memberservice.config.Result;
import com.surveine.memberservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    /**
     * m2. 사용자 관리 페이지 조회
     *
     * @return
     */
    @GetMapping("/profile")
    public ResponseEntity<Result> memberPage() {
        Map<String, Object> rspMap = memberService.getMemberProfile();

        Result result = Result.builder()
                .isSuccess(true)
                .message("조회 성공")
                .result(rspMap)
                .build();

        return ResponseEntity.ok(result);
    }
}
