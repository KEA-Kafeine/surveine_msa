package com.surveine.enqservice.controller;

import com.surveine.enqservice.config.Result;
import com.surveine.enqservice.dto.SboxPageDTO;
import com.surveine.enqservice.service.SboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sbox")
public class SboxController {
    private final SboxService sboxService;
    /**
     * s1. 공유 템플릿 리스트 조회 Controller
     */
    @GetMapping("")
    public ResponseEntity<Result> sboxPage(@RequestHeader Long memberId){
        try{
            SboxPageDTO rspDTO = sboxService.sboxPage(memberId);
            Result result = Result.builder()
                    .isSuccess(true)
                    .result(rspDTO)
                    .message("샌드박스 불러오기 완료")
                    .build();
            return ResponseEntity.ok().body(result);
        }catch (Exception e){
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("공유된 Enq가 없습니다.")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

}
