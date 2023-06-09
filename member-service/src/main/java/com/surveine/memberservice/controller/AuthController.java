package com.surveine.memberservice.controller;

import com.surveine.memberservice.config.Result;
import com.surveine.memberservice.dto.MemberLoginReqDTO;
import com.surveine.memberservice.dto.MemberSignupReqDTO;
import com.surveine.memberservice.security.TokenDTO;
import com.surveine.memberservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    /**
     * au1. 회원가입 -> wsapce-service
     * @param reqDTO
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<Result> signup(@RequestBody MemberSignupReqDTO reqDTO) {
        try {
            authService.createMember(reqDTO);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("회원가입 요청 성공")
                    .build();
            return ResponseEntity.ok().body(result);

        } catch (Exception exception) {
            log.info(exception.getMessage());
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("일시적인 회원가입 오류가 발생했습니다.")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }
}
