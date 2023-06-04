package com.surveine.ansservice.controller;

import com.surveine.ansservice.config.Result;
import com.surveine.ansservice.dto.AnsCreateDTO;
import com.surveine.ansservice.service.AnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ans")
public class AnsController {
    private final AnsService ansService;

    /**
     * a1. 개별 응답지 저장
     * @param memberId
     * @param reqDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseEntity<Result> createAns(@RequestHeader Long memberId, @RequestBody AnsCreateDTO reqDTO) {
        try {
            Boolean rspBool = ansService.isAnsExists(memberId, reqDTO.getEnqId());
            if (rspBool) {
                Map<String, Long> rspMap = ansService.createAns(memberId, reqDTO);
                Result result = Result.builder()
                        .isSuccess(true)
                        .message("응답 저장 성공")
                        .result(rspMap)
                        .build();
                return ResponseEntity.ok().body(result);
            } else {
                Result result = Result.builder()
                        .isSuccess(true)
                        .message("응답 저장 실패. 이미 응답하신 설문지입니다.")
                        .build();
                return ResponseEntity.ok().body(result);
            }
        } catch (Exception e) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("응답 저장 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

//    public ResponseEntity<Result> updateAns(@RequestHeader Long memberId, @RequestBody )
}
