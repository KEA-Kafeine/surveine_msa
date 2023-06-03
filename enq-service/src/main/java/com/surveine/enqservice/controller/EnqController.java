package com.surveine.enqservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.surveine.enqservice.config.Result;
import com.surveine.enqservice.domain.Enq;
import com.surveine.enqservice.dto.EnqRspDTO;
import com.surveine.enqservice.service.EnqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/enq")
public class EnqController {
    private final EnqService enqService;

    /**
     * e1. 설문지 조회
     */
    @GetMapping("/{enqId}")
    public ResponseEntity<Result> getEnq(@PathVariable Long enqId) throws JsonProcessingException {
        EnqRspDTO rspEnq = enqService.getEnq(enqId);
        if(rspEnq != null) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("설문 조회 실패")
                    .result(rspEnq)
                    .build();
            return ResponseEntity.ok(result);
        }else{
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("워크스페이스 호출 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);

        }

    }

}
