package com.surveine.enqservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.surveine.enqservice.config.Result;
import com.surveine.enqservice.dto.SboxFavDTO;
import com.surveine.enqservice.dto.SboxFavListDTO;
import com.surveine.enqservice.dto.SboxPageDTO;
import com.surveine.enqservice.dto.SboxTmplDTO;
import com.surveine.enqservice.service.SboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    /**
     * s2. 관심 템플릿 리스트 조회 Controller
     */
    @GetMapping("/favlist")
    public ResponseEntity<Result> favList(@RequestHeader Long memberId){
        try{
            SboxFavListDTO rspDTO = sboxService.favList(memberId);
            Result result = Result.builder()
                    .isSuccess(true)
                    .result(rspDTO)
                    .message("관심 템플릿 불러오기 완료")
                    .build();
            return ResponseEntity.ok().body(result);
        }catch (Exception e){
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("관심 템플릿이 없습니다.")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * s3. 개별 템플릿 조회 Controller
     */
    @GetMapping("/{enqId}")
    public ResponseEntity<Result> viewTmpl(@PathVariable Long enqId, @RequestHeader Long memberId)throws JsonProcessingException{
        try{
            SboxTmplDTO rspDTO = sboxService.viewTmpl(enqId, memberId);
            Result result = Result.builder()
                    .isSuccess(true)
                    .result(rspDTO)
                    .message("템플릿 불러오기 완료")
                    .build();
            return ResponseEntity.ok().body(result);
        }catch (Exception e){
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("템플릿이 없습니다.")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * s4. 템플릿 좋아요 상태 변경 Controller
     */
    @PutMapping("/chkfav")
    public ResponseEntity<Result> chkFav(@RequestBody SboxFavDTO reqDTO, @RequestHeader Long memberId){
        try{
            sboxService.chkFav(reqDTO, memberId);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("좋아요 상태 변경 완료")
                    .build();
            return ResponseEntity.ok().body(result);
        }catch (Exception e){
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("좋아요 상태 변경 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * s5. 템플릿 신고 Controller
     */
    @PostMapping("/report")
    public ResponseEntity<Result> reportTmpl(@RequestBody Map<String, Long> reqMap, @RequestHeader Long memberId) throws MessagingException{
        try {
            sboxService.reportTmpl(reqMap, memberId);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("신고 완료")
                    .build();
            return ResponseEntity.ok().body(result);
        }catch (Exception e){
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("신고 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * s6. 내 제작함으로 가져오기 Controller
     */
    @PostMapping("/bring")
    public ResponseEntity<Result> getMyTmpl(@RequestBody Map<String, Long> reqMap, @RequestHeader Long memberId){
        try{
            sboxService.getMyTmpl(reqMap, memberId);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("내 제작함으로 가져오기 완료")
                    .build();
            return ResponseEntity.ok().body(result);
        }catch (Exception e){
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("내 제작함으로 가져오기 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }


}
